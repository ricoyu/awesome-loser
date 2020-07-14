package com.loserico.concurrent.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 * Copyright: (C), 2019/12/2 17:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NumberProducerConsumerTest {
	
	private static final int BOUND = 10;
	private static final int N_PRODUCERS = 4;
	private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
	private static int poisonPill = Integer.MAX_VALUE;
	private static int poisonPillPerProducer = N_CONSUMERS / N_PRODUCERS;
	private static int mod = N_CONSUMERS % N_PRODUCERS;
	
	public static void main(String[] args) {
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(BOUND);
		for (int i = 0; i < N_PRODUCERS; i++) {
			new Thread(new NumberProducer(queue, poisonPill, poisonPillPerProducer)).start();
		}
		
		for (int i = 0; i < N_CONSUMERS; i++) {
			new Thread(new NumbersConsumer(queue, poisonPill)).start();
		}
		
		new Thread(new NumberProducer(queue, poisonPill, poisonPillPerProducer + mod)).start();
	}
}
