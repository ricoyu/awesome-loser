package com.loserico.collections;

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class LinkedListTest {

	@Test
	public void testSyncedLinkedList() {
		List<?> syncList = Collections.synchronizedList(new LinkedList<>());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testQueueImpl() {
		Queue<String> queue = new LinkedList<>();
		queue.add("a");
		queue.add("b");
		queue.add("c");
		System.out.println("poll() " + queue.poll());//获取并删除头一个元素
		System.out.println("poll() " + queue.poll());
		
		System.out.println("peek() " + queue.peek());//获取但不删除头元素
		System.out.println("peek() " + queue.peek());
		
		System.out.println("element() " + queue.element());//获取但不删除头元素，如果queue为空，抛exception
		queue.poll();
		queue.element();
	}
}
