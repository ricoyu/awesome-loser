package com.loserico.algorithm.queue;

/**
 * <p>
 * Copyright: (C), 2022-07-01 10:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ArrayQueueTest {
	
	public static void main(String[] args) {
		ArrayQueue queue = new ArrayQueue(3);
		for (int i = 0; i < 3; i++) {
			queue.push(i);
		}
		System.out.println(queue.pop());;
		queue.push("hi");
	}
}
