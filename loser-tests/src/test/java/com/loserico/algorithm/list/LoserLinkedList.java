package com.loserico.algorithm.list;

import lombok.extern.slf4j.Slf4j;

import static com.loserico.json.jackson.JacksonUtils.toJson;

/**
 * 单链表
 * <p>
 * Copyright: (C), 2022-06-26 16:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class LoserLinkedList {
	
	private Node head;
	
	private int size;
	
	public void insertHead(Object data) { //O(1)
		Node node = new Node(data);
		node.next = head;
		head = node;
	}
	
	/**
	 * 插入链表的中间位置
	 */
	public void insert(Object data, int position) { //O(n)
		if (head == null) {
			insertHead(data);
			return;
		} else {
			Node cur = head;
			for (int i = 1; i < position; i++) {
				cur = cur.next;
				if (cur == null) {
					cur = new Node(null);
				}
			}
			
			Node newNode = new Node(data);
			newNode.next = cur.next; //新加的节点的next指向后面的节点, 保证不断链
			cur.next = newNode; //当前节点的next指向新加的节点
		}
	}
	
	public void delweteHead() {
		head = head.next;
	}
	
	public void delete(int position) {
		if (position == 0) {
			delweteHead();
		} else {
			Node curr = head;
			for (int i = 1; i < position; i++) { //注意这里i要从1开始
				curr = curr.next;
			}
			curr.next = curr.next.next;
		}
	}
	
	public void print() {
		Node curr = head;
		while (curr != null) {
			log.info(toJson(curr.value));
			curr = curr.next;
		}
	}
	
	public void find(Object value) { //O(n)
		Node cur = head;
		while (cur != null) {
			if (cur.value == value) {
				log.info("Found");
				break;
			}
			cur = cur.next;
		}
	}
}

class Node {
	Object value; //值
	Node next; //下一个指针
	
	Node(Object value) {
		this.value = value;
		this.next = null;
	}
}
