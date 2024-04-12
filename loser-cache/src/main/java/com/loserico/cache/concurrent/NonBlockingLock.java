package com.loserico.cache.concurrent;

import com.loserico.cache.JedisUtils;
import com.loserico.cache.exception.OperationNotSupportedException;
import com.loserico.cache.utils.KeyUtils;
import com.loserico.common.lang.concurrent.LoserThreadFactory;
import com.loserico.common.lang.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 非阻塞锁
 * 这个锁是线程独有的, 不要作为共享对象
 * <p>
 * Copyright: Copyright (c) 2018-07-16 10:32
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 */
public class NonBlockingLock implements Lock{
	
	private static final Logger log = LoggerFactory.getLogger(NonBlockingLock.class);
	
	private static final int NCPUS = Runtime.getRuntime().availableProcessors();
	
	/**
	 * The number of times to spin before blocking in timed waits.
	 * The value is empirically derived -- it works well across a
	 * variety of processors and OSes. Empirically, the best value
	 * seems not to vary with number of CPUs (beyond 2) so is just
	 * a constant.
	 */
	private static final int maxTimedSpins = (NCPUS < 2) ? 0 : 32;
	
	/**
	 * 锁的模板
	 */
	private static final String LOCK_FORMAT = "loser:nblk:%s:lock";
	
	/**
	 * 解锁channel模板
	 */
	private static final String NOTIFY_CHANNEL_FORMAT = "loser:blk:%s:lock:channel";
	
	/**
	 * 解锁后在该channel上通知等待线程可以获取锁了
	 */
	private String notifyChannel;
	
	/**
	 * 这是commons-spring模块中的类
	 */
	private static final String TRANSACTION_EVENTS_CLASS_NAME = "com.loserico.common.spring.transaction.TransactionEvents";

	private String key;

	private String requestId;

	private boolean locked;
	
	private ScheduledExecutorService watchDog = null;
	
	/**
	 * 当前线程自旋获取锁失败后, 会先订阅notifyChannel, 然后进入阻塞状态;
	 * 如果拿到锁的线程解锁, 会发布一条消息, 此时本线程被唤醒再次尝试获取锁
	 */
	private JedisPubSub subscribe = null;
	
	/**
	 * 锁默认30秒过期
	 */
	private int defaultTimeout = 30;
	
	/**
	 * transactionEventsInstance是否已经通过反射获取过
	 */
	private boolean transactionEventsInitialized = false;
	private Object transactionEventsInstance;

	public NonBlockingLock(String key) {
		KeyUtils.requireNonBlank(key);
		this.key = String.format(LOCK_FORMAT, key);
		this.notifyChannel = String.format(NOTIFY_CHANNEL_FORMAT, key);
		this.requestId =Thread.currentThread().getName() + UUID.randomUUID().toString();
	}
	
	@Override
	public void lock() {
		//自旋计数
		int i = 0;
		String threadName = Thread.currentThread().getName();
		
		/*
		 * 尝试第一次加锁, 加锁成功则启动watchDog并返回
		 */
		boolean locked = JedisUtils.setnx(key, requestId, defaultTimeout, TimeUnit.SECONDS);
		if (locked) {
			log.debug(">>>>>> {} 获取锁成功 <<<<<<", threadName);
			this.locked = true;
			startWatchDog();
			return;
		}
	}

	@Override
	public void unlock() {
		if (locked) {
			boolean unlockSuccess = JedisUtils.unlock(key, requestId);
			String threadName = Thread.currentThread().getName();
			if (!unlockSuccess) {
				/*
				 * 关掉看门狗
				 */
				stopWatchDog();
				throw new OperationNotSupportedException("解锁失败了哟");
			}
			log.debug(">>>>>> {} 解锁成功 <<<<<<", threadName);
			locked = false;
			/*
			 * 关掉看门狗
			 */
			stopWatchDog();
			log.debug(">>>>>> {} shutdown Watch dog <<<<<<", threadName);
		} else {
			throw new OperationNotSupportedException("你还没获取到锁哦");
		}
	}

	@Override
	public void unlockAnyway() {
		if (this.transactionEventsInitialized) {
			throw new OperationNotSupportedException("unlockAnyway()只能调一次");
		}
		if (locked) {
			//调用这个类的instance()方法, 如果这个类不在classpath下会直接抛异常提示找不到这个类, 所以这里不抛异常肯定能找到一个对象
			transactionEventsInstance = ReflectionUtils.invokeStatic(TRANSACTION_EVENTS_CLASS_NAME, "instance");
			this.transactionEventsInitialized = true;
			ReflectionUtils.invokeMethod(transactionEventsInstance, "afterCompletion", () -> JedisUtils.unlock(key, requestId));
		} else {
			throw new OperationNotSupportedException("你还没获取到锁哦");
		}
	}
	
	/**
	 * 定时刷新锁的过期时间
	 * 注意通过Idea debug的时候, 断点Suspend要设为Thread级别, 不然watchDog线程不会运行, 导致锁一会就失效了
	 */
	private void startWatchDog() {
		if (watchDog == null) {
			watchDog = new ScheduledThreadPoolExecutor(1, new LoserThreadFactory("Loser Cache key renewval watch dog"));
		}
		watchDog.scheduleAtFixedRate(() -> {
			//如果key已经过期了, 那么watchDog就不用再去刷新key过期时间了
			boolean isSuccess = JedisUtils.expire(key, defaultTimeout, TimeUnit.SECONDS);
			if (!isSuccess) {
				log.debug("Key {} already expired, Watch dog stop refresh", key);
				watchDog.shutdown();
				watchDog = null;
			} else {
				log.debug("Watch dog refresh lock {} timeout to default {} seconds", key, defaultTimeout);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	private void stopWatchDog() {
		if (watchDog != null && !watchDog.isShutdown()) {
			watchDog.shutdown();
			watchDog = null;
		}
	}
	
	@Override
	public boolean locked() {
		return locked;
	}

}
