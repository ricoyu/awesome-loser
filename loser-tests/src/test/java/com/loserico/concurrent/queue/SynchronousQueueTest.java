package com.loserico.concurrent.queue;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020-12-07 14:16
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SynchronousQueueTest {
	
	public static void main(String[] args) {
		final SynchronousQueue<String> synchronousQueue = new SynchronousQueue<String>();
		
		SynchronousQueueProducer queueProducer = new SynchronousQueueProducer(synchronousQueue);
		new Thread(queueProducer, "Producer").start();
		
		SynchronousQueueConsumer queueConsumer1 = new SynchronousQueueConsumer(synchronousQueue);
		new Thread(queueConsumer1, "Consumer1").start();
		
		SynchronousQueueConsumer queueConsumer2 = new SynchronousQueueConsumer(synchronousQueue);
		new Thread(queueConsumer2, "Consumer2").start();
	}
	
	static class SynchronousQueueProducer implements Runnable {
		
		private SynchronousQueue<String> blockingQueue;
		
		final Random random = new Random();
		
		public SynchronousQueueProducer(SynchronousQueue<String> queue) {
			this.blockingQueue = queue;
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					String data = UUID.randomUUID().toString();
					System.out.println(Thread.currentThread().getName() + " Put: " + data);
					blockingQueue.put(data);
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static class SynchronousQueueConsumer implements Runnable {
		
		protected SynchronousQueue<String> blockingQueue;
		
		public SynchronousQueueConsumer(SynchronousQueue<String> queue) {
			this.blockingQueue = queue;
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					String data = blockingQueue.take();
					System.out.println(Thread.currentThread().getName() + " take(): " + data);
					TimeUnit.MILLISECONDS.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
