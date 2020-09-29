package com.loserico.concurrent.blockingDueue;

import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020-09-29 16:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BlockingQueueExample {
	
	@SneakyThrows
	public static void main(String[] args) {
		BlockingQueue queue = new ArrayBlockingQueue(1024);
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		
		new Thread(producer).start();
		new Thread(consumer).start();
		
		TimeUnit.SECONDS.sleep(4);
	}
	
	private static class Producer implements Runnable {
		
		private BlockingQueue queue;
		
		public Producer(BlockingQueue queue) {
			this.queue = queue;
		}
		
		@Override
		public void run() {
			try {
				queue.put("1");
				TimeUnit.MILLISECONDS.sleep(1000);
				queue.put("2");
				TimeUnit.MILLISECONDS.sleep(1000);
				queue.put("3");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class Consumer implements Runnable {
		
		private BlockingQueue queue = null;
		
		public Consumer(BlockingQueue queue) {
			this.queue = queue;
		}
		
		@Override
		public void run() {
			try {
				System.out.println(queue.take());
				System.out.println(queue.take());
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
