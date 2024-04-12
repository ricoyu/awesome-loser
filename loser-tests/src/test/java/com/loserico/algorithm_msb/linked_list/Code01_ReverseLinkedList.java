package com.loserico.algorithm_msb.linked_list;

public class Code01_ReverseLinkedList {

    public static void main(String[] args) {
        Node a = new Node(2);
        Node b = new Node(2);
        Node c = new Node(2);
        Node d = new Node(2);
        Node e = new Node(2);
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        printList(a);
//        System.out.println("");
//        Node node = reverseList(a);
//        printList(node);

        Node head = removeNum(a, 2);
        printList(head);
    }

    public static Node removeNum(Node head, int num) {
        //先把head移动到第一个不等于numde节点上
        while (head != null && head.value == num) {
            head = head.next;
        }

        Node pre = null;
        Node cur = head;
        while (cur != null) {
            if (cur.value != num) {
                pre = cur;
                cur = cur.next;
            } else {
                cur = cur.next;
                pre.next = cur;
            }
        }

        return head;
    }

    public static Node reverseList(Node head) {
        Node pre = null;
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    public static Node removeSpecifiedNum(Node head, int num) {
        // 特别处理头节点等于给定值的情况
        if (head != null && head.value == num) {
            return head = head.next;
        }

        Node pre = null;
        Node cur = head;

        while (cur != null) {
            if (cur.value == num) {
                if (pre != null) {
                    pre.next = cur.next;
                }
            } else {
                pre = cur;
            }
            cur = cur.next;
        }

        return head;
    }

   /* public static Node reverseLinkedList(Node head) {
        Node pre = null;
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }*/

    public static void printList(Node node) {
        if (node == null) {
            return;
        }
        while (node.next != null) {
            System.out.print(node.value);
            System.out.print(" --> ");
            node = node.next;
        }
        System.out.print(node.value);
        System.out.println("");
    }
}
