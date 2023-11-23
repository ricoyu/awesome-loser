package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-03 10:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ReverseList_206 {
	
	public ListNode reverseList(ListNode head) {
		ListNode preNode = null;
		ListNode currNode = head;
		while (currNode != null) {
			ListNode next = currNode.next;
			currNode.next = preNode;
			preNode = currNode;
			currNode = next;
		}
		
		return preNode;
	}
}
