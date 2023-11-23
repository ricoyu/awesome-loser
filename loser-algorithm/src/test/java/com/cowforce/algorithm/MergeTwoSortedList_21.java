package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-01 10:18
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MergeTwoSortedList_21 {
	
	public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
		if (list1 == null) {
			return list2;
		}
		if (list2 == null) {
			return list1;
		}
		ListNode l1 = list1;
		ListNode l2 = list2;
		ListNode head = new ListNode();
		ListNode p = head;
		while (l1 != null && l2 != null) {
			if (l1.val <= l2.val) {
				p.next = l1;
				l1 = l1.next;
			} else {
				p.next = l2;
				l2 = l2.next;
			}
			p = p.next;
		}
		
		if (l1 != null) {
			p.next = l1;
		}
		if (l2 != null) {
			p.next = l2;
		}
		
		return head.next;
	}
	
	public ListNode mergeTwoLists2(ListNode list1, ListNode list2) {
		if (list1 == null) {
			return list2;
		}
		if (list2 == null) {
			return list1;
		}
		
		if (list1.val < list2.val) {
			list1.next = mergeTwoLists2(list1.next, list2);
			return list1;
		}else {
			list2.next = mergeTwoLists2(list1, list2.next);
			return list2;
		}
	}
}

class ListNode {
	int val;
	ListNode next;
	
	ListNode() {
	}
	
	ListNode(int val) {
		this.val = val;
	}
	
	ListNode(int val, ListNode next) {
		this.val = val;
		this.next = next;
	}
}
