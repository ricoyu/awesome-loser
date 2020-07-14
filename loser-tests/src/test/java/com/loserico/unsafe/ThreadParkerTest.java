package com.loserico.unsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * Copyright: (C), 2019/11/22 13:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ThreadParkerTest {
	
	public static void main(String[] args) {
		Thread t = new Thread(() -> {
			System.out.println("thread " + Thread.currentThread().getName() + " is running...");
			LockSupport.park();
			System.out.println("thread " + Thread.currentThread().getName() + " is over");
		}, "线程1");
		
		t.start();
		
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		LockSupport.unpark(t);
		System.out.println("main thread is over");
	}
}
