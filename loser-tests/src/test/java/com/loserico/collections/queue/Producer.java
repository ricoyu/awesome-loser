package com.loserico.collections.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

class Producer implements Runnable {
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
		return atomicLong.getAndIncrement();
	}
}