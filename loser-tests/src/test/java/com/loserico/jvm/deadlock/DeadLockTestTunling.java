package com.loserico.jvm.deadlock;

/**
 * 死锁后用jstack -l pid来查看
 * <p>
 * Copyright: (C), 2019 2019-09-22 10:52
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DeadLockTestTunling {
	
	private static Object lock1 = new Object();
	private static Object lock2 = new Object();
	
	public static void main(String[] args) {
		new Thread(() -> {
			synchronized (lock1) {
				System.out.println("thread1 begin");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock2) {
					System.out.println("thread1 end");
				}
			}
		}).start();
		
		new Thread(() -> {
			synchronized (lock2) {
				System.out.println("thread2 begin");
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock1) {
					System.out.println("thread2 end");
				}
			}
		}).start();
		
		System.out.println("main thread end");
	}
}
