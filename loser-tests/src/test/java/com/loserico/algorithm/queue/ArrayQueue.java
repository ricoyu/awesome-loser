package com.loserico.algorithm.queue;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于数组实现的队列
 * <p>
 * Copyright: (C), 2022-06-29 16:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ArrayQueue<T> {
	
	private Object[] data;   //数据
	
	private int head = 0; // 头
	
	private int tail = 0; // 尾
	
	private int n; // 数组的大小
	
	public ArrayQueue(int cap) {
		this.n = cap;
		data = new Object[n];
	}
	
	public void push(T elem) {
		//首先要判断队列是否已经满了
		if (tail == n) {
			log.info("队列满了");
			if (move2Head(elem)) {
				data[tail++] = elem;
			}
			return;
		}
		data[tail++] = elem;
	}
	
	private boolean move2Head(T elem) {
		//表示队列没有pop元素, 队列头部没有空出位置来, 不需要移动
		if (head == 0) {
			log.info("队列头部没有空位置, 不需要移动");
			return false;
		}
		log.info("队列头部有空位置, 可以移动");
		//队列满了就把后面的元素往前移动, 因为前面有元素pop后空出位置来了
		for (int i = head; i < tail; i++) {
			data[i-head] = data[i];
		}
		tail = tail - head;
		head = 0;
		return true;
	}
	
	/**
	 * 数组的问题是出队后空间浪费
	 * @return
	 */
	public T pop() {
		//出队的时候要判断队列是否已经空了
		if (isEmpty()) {
			return null;
		}
		T elem = (T)data[head];
		data[head] = null;
		head++;
		return elem;
	}
	
	public boolean isEmpty() {
		return head == tail;
	}
}
