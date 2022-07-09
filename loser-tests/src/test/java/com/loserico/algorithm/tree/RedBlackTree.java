package com.loserico.algorithm.tree;

/**
 * <p>
 * Copyright: (C), 2022-07-07 11:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RedBlackTree {
	
	private final int R = 0;
	
	private final int B = 1;
	
	private final Node nil = new Node(-1);
	
	private Node root = nil;
	
	private class Node {
		int key = -1;
		int color = B;
		Node left = nil;
		Node right = nil;
		Node parent = nil;
		
		public Node(int key) {
			this.key = key;
		}
		
		@Override
		public String toString() {
			return "Node [key=" + key + ", color=" + color + ", left=" + left.key + ", right=" + right.key + ", p="
					+ parent.key + "]" + "\r\n";
		}
	}
	
	public void printTree(Node node) {
		if (node == nil) {
			return;
		}
		printTree(node.left);
		System.out.println(node.toString());
		printTree(node.right);
	}
	
	private void insert(Node node) {
		Node temp = root;
		/*
		 * 根节点是空节点的话新插入的节点直接变成根节点, 根节点是黑色的
		 */
		if (root == nil) {
			root = node;
			node.color = B;
			node.parent = nil;
		} else {
			node.color = R; //新插入的节点是红色的
			while (true) {
				if (node.key < temp.key) {
					/*
					 * temp一直延左子树往下走, 直到temp指向一个没有左子树的节点, 新插入的节点直接作为其左节点
					 */
					if (temp.left == nil) {
						temp.left = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.left;
					}
				} else if (node.key >= temp.key) {
					if (temp.right == nil) {
						temp.right = node;
						node.parent = temp;
						break;
					} else {
						temp = temp.right;
					}
				}
			}
			fixTree(node);
		}
	}
	
	private void fixTree(Node node) {
	}
}
