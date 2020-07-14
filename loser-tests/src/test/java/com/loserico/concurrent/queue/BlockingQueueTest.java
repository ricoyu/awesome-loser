package com.loserico.concurrent.queue;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BlockingQueue分有界和无界两种队列
 * 无界队列
 * 1: LinkedBlockingDeque
 * <p>
 * Copyright: (C), 2019/12/2 17:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BlockingQueueTest {
	
	@Test
	public void testUnboundedBlockingQueue() {
		/*
		 * LinkedBlockingQueue默认设置capacity为Integer.MAX_VALUE
		 * 因为无界, 添加元素永远不会阻塞
		 * 无界队列的消费端需要尽快消费, 不然很容易OOM
		 */
		BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
	}
	
	@Test
	public void testBoundedQueue() throws InterruptedException {
		/*
		 * 有界队列就是在new的时候传一个capacity设置好容量
		 */
		BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(10);
		for (int i = 0; i < 10; i++) {
			String e = "e" + i;
			System.out.println(blockingQueue.offer(e));;
			//blockingQueue.add(e);
			System.out.println(e);
		}
		//blockingQueue.add("a");
		//System.out.println(blockingQueue.offer("a"));
		blockingQueue.put("a");
		System.out.println("done");
	}
}
