package com.loserico.algorithm.list;

/**
 * <p>
 * Copyright: (C), 2022-06-27 7:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LoserLinkedListTest {
	
	public static void main(String[] args) {
		LoserLinkedList list = new LoserLinkedList();
		list.insert("hello", 0);
		list.insert("world", 1);
		list.insert("rico", 2);
		list.delete(1);
		list.print();
	}
}
