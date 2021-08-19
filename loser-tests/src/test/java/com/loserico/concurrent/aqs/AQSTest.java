package com.loserico.concurrent.aqs;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2021-07-18 11:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class AQSTest {
	
	/**
	 * 公平锁
	 */
	@Test
	public void testFairLock() {
		ReentrantLock lock = new ReentrantLock(true);
		try {
			lock.lock();
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 公平锁
	 */
	@SneakyThrows
	@Test
	public void testFairLock2() {
		ReentrantLock lock = new ReentrantLock(true);
		Thread t1 = new Thread(() -> {
			try {
				lock.lock();
				log.info("线程[{}]加锁成功!", Thread.currentThread().getName());
			} finally {
				lock.unlock();
			}
		}, "线程1");
		Thread t2 = new Thread(() -> {
			try {
				lock.lock();
				log.info("线程[{}]加锁成功!", Thread.currentThread().getName());
			} finally {
				lock.unlock();
			}
		}, "线程2");
		Thread t3 = new Thread(() -> {
			try {
				lock.lock();
				log.info("线程[{}]加锁成功!", Thread.currentThread().getName());
			} finally {
				lock.unlock();
			}
		}, "线程3");
		
		t1.start();
		t2.start();
		t3.start();
		Thread.currentThread().join();
		log.info("done");
	}
	
	@Test
	public void testUnfairLock() {
		Lock lock = new ReentrantLock(false);
		try {
			lock.lock();
		} finally {
			lock.unlock();
		}
	}
	
	
	/**
	 * 公平锁
	 */
	@SneakyThrows
	@Test
	public void testFairUnLock() {
		ReentrantLock lock = new ReentrantLock(true);
		Thread t1 = new Thread(() -> {
			try {
				lock.lock();
				log.info("线程[{}]加锁成功!", Thread.currentThread().getName());
			} finally {
				lock.unlock();
			}
		}, "线程1");
		Thread t2 = new Thread(() -> {
			try {
				lock.lock();
				log.info("线程[{}]加锁成功!", Thread.currentThread().getName());
			} finally {
				lock.unlock();
			}
		}, "线程2");
		Thread t3 = new Thread(() -> {
			try {
				lock.lock();
				log.info("线程[{}]加锁成功!", Thread.currentThread().getName());
			} finally {
				lock.unlock();
			}
		}, "线程3");
		
		t1.start();
		t2.start();
		t3.start();
		Thread.currentThread().join();
		log.info("done");
	}
	
	@SneakyThrows
	@Test
	public void testPark() {
		Thread t1 = new Thread(() -> {
			try {
				int i = 0;
				boolean parked = false;
				for (; ; ) {
					if (i % 500 == 0) {
						if (!parked) {
							log.info("我在干活");
						} else {
							log.info("我又开始干活");
						}
						LockSupport.park(this);
						parked = true;
					}
					i++;
				}
			} catch (Throwable e) {
				log.info("捕获到Unpark异常了", e);
			}
		}, "线程1");
		
		t1.start();
		MILLISECONDS.sleep(500);
		LockSupport.unpark(t1);
		Thread.currentThread().join();
	}
}
