package com.cowforce.algorithm;

/**
 * 给你一个链表的头节点 head ，判断链表中是否有环。
 *
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 
 * 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。
 * 注意：pos 不作为参数进行传递 。仅仅是为了标识链表的实际情况。
 * <p>
 * Copyright: (C), 2022-12-02 9:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CycleList_141 {
	
	public boolean hasCycle(ListNode head) {
		ListNode fastPtr = head;
		ListNode slowPtr = head;
		
		while (fastPtr != null && fastPtr.next != null) {
			fastPtr = fastPtr.next.next;
			slowPtr = slowPtr.next;
			if (fastPtr == slowPtr) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasCycle3(ListNode head) {
		ListNode index = head;
		while (index!= null) {
			if (index.val >100000) {
				return true;
			} else {
				index.val = 100001;
			}
			index = index.next;
		}
		return false;
	}
}
