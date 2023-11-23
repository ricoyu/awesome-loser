package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-03 11:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IsPalindrome_234 {
	
	public boolean isPalindrome(ListNode head) {
		ListNode fast = head, slow = head;
		while (fast != null && fast.next != null) {
			fast = fast.next.next;
			slow = slow.next;
		}
		
		//如果链表是奇数个结点，把正中的归到左边
		if (fast != null) {
			slow = slow.next;
		}
		slow = reverse(slow);
		fast = head;
		
		while (slow != null) {
			if (fast.val != slow.val) {
				return false;
			}
			fast = fast.next;
			slow = slow.next;
		}
		return true;
	}
	
	public ListNode reverse(ListNode head) {
		ListNode pre = null;
		ListNode curr = head;
		while (curr != null) {
			ListNode next = curr.next;
			curr.next = pre;
			pre = curr;
			curr = next;
		}
		return pre;
	}
}
