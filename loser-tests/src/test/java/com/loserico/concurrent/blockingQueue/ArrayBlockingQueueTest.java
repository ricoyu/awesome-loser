package com.loserico.concurrent.blockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2020-09-29 15:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ArrayBlockingQueueTest {
	
	private static BlockingQueue blockingQueue = new ArrayBlockingQueue(12);
	
	public static void main(String[] args) throws InterruptedException {
		new Thread(() -> {
			int i =1;
			while (true) {
				try {
					blockingQueue.poll(1000, MILLISECONDS);
				} catch (InterruptedException e) {
				}
				System.out.println(Thread.currentThread().getName()+"在时间点: "+System.currentTimeMillis() +"拉取: "+i+++"次");
			}
		}, "线程1").start();
		new Thread(() -> {
			int i =1;
			while (true) {
				try {
					blockingQueue.poll(500, MILLISECONDS);
					
				} catch (InterruptedException e) {
				}
				System.out.println(Thread.currentThread().getName()+"在时间点: "+System.currentTimeMillis() +"拉取: "+i+++"次");
			}
		}, "线程2").start();
		Thread.currentThread().join();
	}
}
