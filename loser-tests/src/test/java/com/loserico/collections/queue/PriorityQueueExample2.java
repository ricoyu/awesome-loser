package com.loserico.collections.queue;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 使用Comparator来自定义优先级的例子
 * <p/>
 * Copyright: Copyright (c) 2024-09-25 11:40
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PriorityQueueExample2 {

	public static void main(String[] args) {
		PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(String::length));

		// 添加元素
		priorityQueue.offer("Java");
		priorityQueue.offer("C");
		priorityQueue.offer("Python");
		priorityQueue.offer("Kotlin");

		// 打印并移除元素，按优先级顺序（长度从短到长）
		while (!priorityQueue.isEmpty()) {
			System.out.println(priorityQueue.poll()); // 输出: C, Java, Kotlin, Python
		}
	}
}
