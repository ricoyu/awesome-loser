package com.loserico.collections.queue;

import java.util.PriorityQueue;

/**
 * 演示 PriorityQueue
 * <p/>
 * Copyright: Copyright (c) 2024-09-25 11:36
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PriorityQueueExample {

	public static void main(String[] args) {
		PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();

		priorityQueue.offer(5);
		priorityQueue.offer(1);
		priorityQueue.offer(3);

		// 打印并移除元素，按优先级顺序
		while (!priorityQueue.isEmpty()) {
			System.out.println(priorityQueue.poll());;
		}
	}
}
