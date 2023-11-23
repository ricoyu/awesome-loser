package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-02 9:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DetectCycleBegin_142 {
	
	public ListNode detectCycle(ListNode head) {
		if (head == null) {
			return null;
		}
		ListNode slow = head;
		ListNode fast = head;
		boolean existCycle = false;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast) {
				existCycle = true;
				break;
			}
		}
		
		if (existCycle) {
			slow = head;
			while (slow != fast) {
				slow = slow.next;
				fast = fast.next;
			}
			return slow;
		}
		return null;
	}
}
