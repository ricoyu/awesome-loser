package com.loserico.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2023-09-06 10:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UnlockTest {
	
	public static void main(String[] args) {
		Lock lock = new ReentrantLock();
		new Thread(() -> {
			lock.lock();
			try {
				SECONDS.sleep(1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}).start();
		System.out.println("主线程释放锁");
		lock.unlock();
	}
}
