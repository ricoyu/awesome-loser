package com.loserico.algorithm.list;

/**
 * <p>
 * Copyright: (C), 2022-06-27 7:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DoubleLinkedList {
	
	private DNode head; //头节点
	private DNode tail; //尾节点
	
	public DoubleLinkedList() {
		head = null;
		tail = null;
	}
	
	public void insertHead(Object data) {
		DNode newNode = new DNode(data);
		if (head == null) {
			this.head = newNode;
			tail = newNode;
		} else {
			newNode.next = head;
			head.prev = newNode;
			head = newNode;
		}
	}
	
	public void delteHead() {
		if (head == null) { //没有数据
			return;
		}
		if (head.next == null) { //就一个节点
			tail = null;
		} else {
			head.next.prev = null;
		}
		head = head.next;
	}
}

class DNode {
	Object value; //值
	DNode next; //下一个指针
	DNode prev; //上一个指针
	
	DNode(Object value) {
		this.value = value;
		this.next = null;
	}
}
