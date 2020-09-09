package com.loserico.cache.concurrent;

import com.loserico.cache.JedisUtils;
import com.loserico.cache.exception.OperationNotSupportedException;
import com.loserico.cache.utils.KeyUtils;
import com.loserico.common.lang.utils.ReflectionUtils;

import java.util.UUID;

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
	
	/**
	 * 锁的模板
	 */
	private static final String LOCK_FORMAT = "loser:nblk:%s:lock";

	/**
	 * 这是commons-spring模块中的类
	 */
	private static final String TRANSACTION_EVENTS_CLASS_NAME = "com.loserico.common.spring.transaction.TransactionEvents";

	private String key;

	private String requestId;

	private boolean locked;

	/**
	 * transactionEventsInstance是否已经通过反射获取过
	 */
	private boolean transactionEventsInitialized = false;
	private Object transactionEventsInstance;

	public NonBlockingLock(String key) {
		KeyUtils.requireNonBlank(key);
		this.key = String.format(LOCK_FORMAT, key);
		this.requestId = UUID.randomUUID().toString();
	}
	
	@Override
	public void lock() {
		this.locked = JedisUtils.lock(key, requestId);
	}

	@Override
	public void unlock() {
		if (locked) {
			boolean unlockSuccess = JedisUtils.unlock(key, requestId);
			if (!unlockSuccess) {
				throw new OperationNotSupportedException("解锁失败了哟");
			}
			locked = false;
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

	@Override
	public boolean locked() {
		return locked;
	}

}
