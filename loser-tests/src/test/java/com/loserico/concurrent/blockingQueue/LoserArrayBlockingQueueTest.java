package com.loserico.concurrent.blockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * Copyright: (C), 2020-12-10 17:12
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LoserArrayBlockingQueueTest {
	
	public static void main(String[] args) {
		BlockingQueue<Long> q = new LoserArrayBlockingQueue<>(1024);
		LoserArrayBlockingQueueTest.Producer p = new LoserArrayBlockingQueueTest.Producer(q);
		LoserArrayBlockingQueueTest.Consumer c1 = new LoserArrayBlockingQueueTest.Consumer(q);
		LoserArrayBlockingQueueTest.Consumer c2 = new LoserArrayBlockingQueueTest.Consumer(q);
		new Thread(p).start();
		new Thread(c1).start();
		new Thread(c2).start();
	}
	
	static class Producer implements Runnable {
		private final BlockingQueue<Long> queue;
		private AtomicLong atomicLong = new AtomicLong(0);
		
		Producer(BlockingQueue<Long> q) {
			queue = q;
		}
		
		public void run() {
			try {
				while (true) {
					queue.put(produce());
				}
			} catch (InterruptedException ex) {
			}
		}
		
		Long produce() {
			long value = atomicLong.getAndIncrement();
			System.out.println("生产: " + value);
			return value;
		}
	}
	
	static class Consumer implements Runnable {
		private final BlockingQueue<Long> queue;
		
		Consumer(BlockingQueue<Long> q) {
			queue = q;
		}
		
		public void run() {
			try {
				while (true) {
					consume(queue.take());
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		void consume(Long x) {
			System.out.println("消费: " + x);
		}
	}
}
