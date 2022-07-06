package com.loserico.algorithm.tree;

import lombok.Data;

/**
 * <p>
 * Copyright: (C), 2022-07-06 10:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BinaryTree {
	
	public static void main(String[] args) {
		TreeNode dNode = new TreeNode('d', null, null);
		TreeNode hNode = new TreeNode('h', null, null);
		TreeNode kNode = new TreeNode('k', null, null);
		TreeNode cNode = new TreeNode('c', hNode, kNode);
		TreeNode gNode = new TreeNode('g', dNode, null);
		TreeNode bNode = new TreeNode('b', null, cNode);
		TreeNode fNode = new TreeNode('f', gNode, null);
		TreeNode eNode = new TreeNode('e', null, fNode);
		TreeNode aNode = new TreeNode('a', bNode, eNode);
		System.out.println("前序遍历");
		pre(aNode);
		System.out.println("中序遍历");
		mid(aNode);
		System.out.println("后序遍历");
		last(aNode);
	}
	
	/**
	 * 前序遍历
	 */
	public static void pre(TreeNode root) {
		print(root);
		if (root.getLeft() != null) {
			pre(root.getLeft());
		}
		if (root.getRight() != null) {
			pre(root.getRight());
		}
	}
	
	public static void mid(TreeNode root) {
		if (root.getLeft() != null) {
			pre(root.getLeft());
		}
		print(root);
		
		if (root.getRight() != null) {
			pre(root.getRight());
		}
	}
	
	public static void last(TreeNode root) {
		if (root.getLeft() != null) {
			pre(root.getLeft());
		}
		if (root.getRight() != null) {
			pre(root.getRight());
		}
		print(root);
		
	}
	
	public static void print(TreeNode node) {
		System.out.println(node.getData());
	}
}
	
@Data
class TreeNode {
	private char data;
	private TreeNode left;
	private TreeNode right;
	
	public TreeNode(char data, TreeNode left, TreeNode right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}
}


