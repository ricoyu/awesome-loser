package com.loserico.concurrent.queue;

import java.util.concurrent.BlockingQueue;

/**
 * <p>
 * Copyright: (C), 2019/12/2 17:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NumbersConsumer implements Runnable {
	
	private BlockingQueue<Integer> queue;
	private int poisonPill;
	
	public NumbersConsumer(BlockingQueue<Integer> queue, int poisonPill) {
		this.queue = queue;
		this.poisonPill = poisonPill;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				Integer number = null; //队列为空则一直阻塞
				number = queue.take();
				if (number.equals(poisonPill)) {
					return;
				}
				System.out.println(Thread.currentThread().getName() + " result: " + number);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
