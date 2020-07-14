package com.loserico.concurrent.locksupport;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020/3/31 13:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MutexTest {
	
	public static void main(String[] args) {
		FIFOMutex mutex = new FIFOMutex();
		for (int i = 0; i < 3; i++) {
			new Thread(() -> {
				try {
					TimeUnit.MILLISECONDS.sleep(new Random(1000).nextLong());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mutex.lock();
				System.out.println(Thread.currentThread().getName() + " 加锁成功!");
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " 解锁!");
				mutex.unlock();
			}, "线程"+i).start();
		}
	}
}
