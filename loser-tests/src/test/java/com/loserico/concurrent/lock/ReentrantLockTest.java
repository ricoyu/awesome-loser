package com.loserico.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Copyright: (C), 2020-7-23 0023 11:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ReentrantLockTest {
	
	/*
	 * 可重入是同一个线程或者锁之后可以重复再获取锁, 对于其他线程来说是排它锁
	 */
	public static void main(String[] args) {
		Lock lock = new ReentrantLock();
		Thread t1 = new Thread(() -> {
			lock.lock();
			System.out.println("这是线程1在执行");
			lock.unlock();
		});
		Thread t2 = new Thread(() -> {
			lock.lock();
			System.out.println("这是线程2在执行");
			lock.unlock();
		});
		t1.start();
		t2.start();
	}
}
