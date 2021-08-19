package com.loserico.concurrent.thread;

/**
 * <p>
 * Copyright: (C), 2021-07-30 16:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ThreadYeldTest {
	
	public static void main(String[] args) {
		Runnable task = () -> {
			int counter = 0;
			while (counter < 2) {
				System.out.println(Thread.currentThread().getName());
				counter++;
				Thread.yield();
			}
		};
		
		Thread t1 = new Thread(task, "线程1");
		Thread t2 = new Thread(task, "线程2");
		
		t1.start();
		t2.start();
	}
}
