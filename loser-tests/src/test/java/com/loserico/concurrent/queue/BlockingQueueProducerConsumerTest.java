package com.loserico.concurrent.queue;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020/3/24 9:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BlockingQueueProducerConsumerTest {
	
	static class Sstup {
		
		public static void main(String[] args) {
			BlockingQueue queue = new ArrayBlockingQueue(4);
			Producer producer = new Producer(queue);
			Consumer consumer1 = new Consumer(queue);
			Consumer consumer2 = new Consumer(queue);
			new Thread(producer, "producer").start();
			new Thread(consumer1, "consumer1").start();
			new Thread(consumer2, "consumer2").start();
		}
	}
	
	static class Producer implements Runnable {
		
		private final BlockingQueue queue;
		
		public Producer(BlockingQueue queue) {
			this.queue = queue;
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					queue.put(produce());
					TimeUnit.SECONDS.sleep(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public Object produce() {
			LocalDateTime now = LocalDateTime.now();
			System.out.println(Thread.currentThread().getName() + " put " + now);
			return now;
		}
	}
	
	static class Consumer implements Runnable {
		
		private final BlockingQueue queue;
		
		public Consumer(BlockingQueue queue) {
			this.queue = queue;
		}
		
		@SneakyThrows
		@Override
		public void run() {
			while (true) {
				Object object = queue.take();
				System.out.println(Thread.currentThread().getName() + " take " + object);
			}
		}
	}
}
