package com.loserico.algorithm_msb.linked_list;

public class ReverseLinkedList {

    public static void main(String[] args) {
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);

        a.next = b;
        b.next = c;

        print(a);
        reverse(a);
        print(c);
    }

    public static void print(Node head) {
        System.out.print("{");
        while (head != null) {
            System.out.print(head.data);
            head = head.next;
            if (head != null) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    public static void reverse(Node head) {
        Node prev = null;
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
    }

    static class Node {
        private int data;

        private Node next;

        public Node(int data) {
            this.data = data;
        }
    }
}
