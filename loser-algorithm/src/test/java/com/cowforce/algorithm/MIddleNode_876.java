package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-04 10:56
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MIddleNode_876 {
	
	public ListNode middleNode(ListNode head) {
		ListNode slow = head;
		ListNode fast = head;
		
		while (fast!= null &&fast.next!= null) {
			fast = fast.next.next;
			slow = slow.next;
		}
		
		return slow;
	}
}
