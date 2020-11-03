package com.loserico.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Class that acts as a priority queue but has a fixed size.
 * When the maximum number of elements is reached the lowest/highest element
 * will be removed.
 * 
 * <p>
 * Copyright: (C), 2020-09-30 10:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FixedSizePriorityQueue<T> {
	
	private TreeSet<T> inner;
	
	private int maxSize;
	
	public FixedSizePriorityQueue(Comparator<T> comparator, int maxSize) {
		this.inner = new TreeSet<>(comparator);
		this.maxSize = maxSize;
	}
	
	public FixedSizePriorityQueue<T> add(T element) {
		inner.add(element);
		if (inner.size() > maxSize) {
			inner.pollLast();
		}
		
		return this;
	}
	
	public FixedSizePriorityQueue<T> remove(T element) {
		if (inner.contains(element)) {
			inner.remove(element);
		}
		
		return this;
	}
	
	public Iterator<T> iterator() {
		return inner.iterator();
	}
	
	@Override
	public String toString() {
		return "FixedSizePriorityQueue{QueueContents=" + inner;
	}
}
