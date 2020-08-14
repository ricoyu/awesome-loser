package com.loserico.jvm;

import java.util.HashSet;

/**
 * <p>
 * Copyright: (C), 2020-08-11 9:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Arthas {
	
	private static HashSet hashSet = new HashSet();
	
	public static void main(String[] args) {
		cpuHigh();
		deadThread();
		addHashSetThread();
	}
	
	/**
	 * 不断的向 hashSet 集合添加数据
	 */
	public static void addHashSetThread() {
		new Thread(() -> {
			int count = 0;
			while (true) {
				try {
					hashSet.add("count" + count);
					Thread.sleep(1000);
					count++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "addHashSetThread").start();
	}
	
	/**
	 * 线程空转, CPU使用率高
	 */
	public static void cpuHigh() {
		new Thread(() -> {
			while (true) {
				
			}
		}, "cpuHighThread").start();
	}
	
	/**
	 * 模拟死锁
	 */
	public static void deadThread() {
		//创建资源
		Object resourceA = new Object();
		Object resourceB = new Object();
		
		//创建线程
		Thread threadA = new Thread(() -> {
			synchronized (resourceA) {
				System.out.println(Thread.currentThread() + " get ResourceA");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println(Thread.currentThread() + "waiting get resourceB");
				synchronized (resourceB) {
					System.out.println(Thread.currentThread() + " get resourceB");
				}
			}
		}, "threadA");
		
		Thread threadB = new Thread(() -> {
			synchronized (resourceB) {
				System.out.println(Thread.currentThread() + " get ResourceB");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println(Thread.currentThread() + "waiting get resourceA");
				synchronized (resourceA) {
					System.out.println(Thread.currentThread() + " get resourceA");
				}
			}
		}, "threadB");
		
		threadA.start();
		threadB.start();
	}
}
