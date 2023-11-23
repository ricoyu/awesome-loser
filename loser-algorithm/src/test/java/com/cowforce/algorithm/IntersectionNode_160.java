package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-03 9:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IntersectionNode_160 {
	
	public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
		ListNode pA = headA;
		ListNode pB = headB;
		while (pA != pB) {
			pA = pA == null? headB : pA.next;
			pB = pB == null? headA : pB.next;
		}
		
		return pA;
	}
}

