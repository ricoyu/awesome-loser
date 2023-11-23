package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-04 11:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class KthToLast_02_02 {
	
	public int kthToLast(ListNode head, int k) {
		ListNode tmp = head;
		ListNode kthNode = head;
		
		for (int i = 1; i < k; i++) {
			tmp = tmp.next;
		}
		
		while (tmp.next != null) {
			tmp = tmp.next;
			kthNode = kthNode.next;
		}
		
		return kthNode.val;
	}
}
