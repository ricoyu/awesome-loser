package com.loserico.algorithm.tree;

/**
 * <p>
 * Copyright: (C), 2022-07-06 16:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BinarySearchTree {
	
	public BinarySearchTree(int data) {
		TreeNode root = new TreeNode(data, null, null);
	}
	
	public void insert(TreeNode root, int data) {
		if (data < root.data) {
			if (root.left == null) {
				TreeNode node = new TreeNode(data, null, null);
				root.left = node;
			} else {
				insert(root.left, data);
			}
		} else {
			if (root.right == null) {
				TreeNode node = new TreeNode(data, null, null);
				root.right = node;
			}
			insert(root.right, data);
		}
	}
	
	public void find(TreeNode root, int data) {
		if (root.data == data) {
			System.out.println(data);
			return;
		} else if(data < root.data) {
			find(root.left, data);
		} else if (data > root.data) {
			find(root.right, data);
		}
	}
	
	class TreeNode {
		private int data;
		private TreeNode left;
		private TreeNode right;
		
		public TreeNode(int data, TreeNode left, TreeNode right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}
	}
}
