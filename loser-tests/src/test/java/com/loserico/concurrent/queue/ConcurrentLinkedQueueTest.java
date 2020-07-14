package com.loserico.concurrent.queue;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020/3/9 11:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ConcurrentLinkedQueueTest {
	
	@Test
	public void testOffer() {
		java.util.concurrent.ConcurrentLinkedQueue<Integer> queue = new java.util.concurrent.ConcurrentLinkedQueue<>();
		queue.offer(1);
		queue.offer(2);
		
		Integer i = null;
		while ((i = queue.poll()) != null) {
			System.out.println(i);
		}
	}
}
