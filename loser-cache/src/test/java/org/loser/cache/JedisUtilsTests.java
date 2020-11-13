package org.loser.cache;

import com.loserico.cache.JedisUtils;
import com.loserico.cache.concurrent.Lock;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2019/10/25 17:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JedisUtilsTests {
	
	@Test
	public void testWarmUp() {
		try {
			Class.forName("com.loserico.cache.JedisUtils");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSet() {
		JedisUtils.set("k1", "aaa");
		Assert.assertEquals("aaa", JedisUtils.get("k1"));
		System.out.println(JedisUtils.get("k1"));
	}
	
	@Test
	public void testSetWithExpire() {
		Boolean success = JedisUtils.set("k1", "v1", 1, TimeUnit.MINUTES);
		System.out.println(success);
	}
	
	@Test
	public void testSetNX() {
		Boolean success = JedisUtils.setnx("k2", "v2", 1, TimeUnit.MINUTES);
		System.out.println(success);
	}
	
	@Test
	public void testSubscribe() {
	}
	
	@SneakyThrows
	@Test
	public void testIncrWithExpire() {
		Long value = JedisUtils.incr("retryCount", 1, TimeUnit.MINUTES);
		System.out.println(value);
		TimeUnit.SECONDS.sleep(20);
		System.out.println(JedisUtils.incr("retryCount", 1, TimeUnit.MINUTES));
	}
	
	@Test
	public void testPipelined() {
		List<Object> users = JedisUtils.pipeline((pipeline) -> {
			for (int i = 0; i < 10; i++) {
				pipeline.lpop("users");
			}
		});
		users.forEach(System.out::println);
	}
	
	/*@SneakyThrows
	public static void main(String[] args) {
		JedisPubSub jedisPubSub = JedisUtils.subscribe("channel:test", (channel, message) -> {
			log.info(message);
		});
		TimeUnit.SECONDS.sleep(10);
		JedisUtils.unsubscribe(jedisPubSub, "channel:test");
		log.info("UnSubscribed");
	}*/
	
	public static void main(String[] args) {
		Runnable task = () -> {
			Lock lock = JedisUtils.blockingLock("lock1");
			try {
				lock.lock();
				log.info(Thread.currentThread().getName() + " locked");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			} finally {
				if (lock.locked()) {
					lock.unlock();
				}
				System.out.println("任务完成");
				
			}
		};
		
		Thread t1 = new Thread(task, "t1");
		Thread t2 = new Thread(task, "t2");
		
		t1.start();
		t2.start();
		
	}
	
/*	public static void main(String[] args) {
		Lock lock = JedisUtils.blockingLock("lock1");
		try {
			lock.lock();
			log.info(Thread.currentThread().getName() + " locked");
			
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (true) {
				throw new RuntimeException();
			}
		} finally {
			if (lock.locked()) {
				lock.unlock();
			}
			System.out.println("任务完成");
			
		}
	}*/
	
}
