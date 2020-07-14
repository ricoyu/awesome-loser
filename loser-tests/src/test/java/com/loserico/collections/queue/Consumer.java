package com.loserico.collections.queue;

import java.util.concurrent.BlockingQueue;

class Consumer implements Runnable {
	private final BlockingQueue<Long> queue;

	Consumer(BlockingQueue<Long> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				System.out.println("Cosumer尝试从BlockingQueue中take一个元素...");
				Long value = queue.take();
				consume(value);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	void consume(Long x) {
		System.out.println("消费: " + x);
	}
}