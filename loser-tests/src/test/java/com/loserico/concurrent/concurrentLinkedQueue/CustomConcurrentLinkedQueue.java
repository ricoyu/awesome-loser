package com.loserico.concurrent.concurrentLinkedQueue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * Copyright: (C), 2020-12-10 16:21
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CustomConcurrentLinkedQueue<E> {
	
	private Node<E> tail;
	
	public CustomConcurrentLinkedQueue() {
		
	}
	
	public boolean offer(E e) {
		Node<E> node = new Node<>(e, new AtomicReference<>(null));
		while (true) {
			Node<E> curTail = tail;
			AtomicReference<Node<E>> curNext = curTail.next;
		}
	}
	
	private static class Node<E> {
		
		private volatile E item;
		private AtomicReference<Node<E>> next;
		
		public Node(E item, AtomicReference<Node<E>> next) {
			this.item = item;
			this.next = next;
		}
	}
}
