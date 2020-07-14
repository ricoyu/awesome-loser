package com.loserico.concurrent.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 模拟BlockingQueue的生产者
 * <p>
 * Copyright: (C), 2019/12/2 17:27
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NumberProducer implements Runnable{
	
	private BlockingQueue<Integer> numbersQueue;
	private int poisonPill;
	private int poisonPillPerProducer;
	
	public NumberProducer(BlockingQueue<Integer> numbersQueue, int poisonPill, int poisonPillPerProducer) {
		this.numbersQueue = numbersQueue;
		this.poisonPill = poisonPill;
		this.poisonPillPerProducer = poisonPillPerProducer;
	}
	
	@Override
	public void run() {
		try {
			generateNumbers();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private void generateNumbers() throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			numbersQueue.put(ThreadLocalRandom.current().nextInt(100));
		}
		for (int j = 0; j < poisonPillPerProducer; j++) {
			numbersQueue.put(poisonPill);
		}
	}
}
