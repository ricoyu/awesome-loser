package com.cowforce.algorithm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2022-12-06 9:56
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InorderTraversalTree_94 {
	
	public List<Integer> inorderTraversalLoop(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		Deque<TreeNode> stack = new LinkedList<>();
		while (root != null || !stack.isEmpty()) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			root = stack.pop();
			result.add(root.val);
			root = root.right;
		}
		return result;
	}
	
	public List<Integer> traverseWithLoop(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		Deque<TreeNode> stack = new LinkedList<>();
		while (root != null || !stack.isEmpty()) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			root = stack.pop();
			result.add(root.val);
			root = root.right;
		}
		return result;
	}
	public List<Integer> inorderTraversal(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		accessTree(root, result);
		return result;
	}
	
	public void accessTree(TreeNode root, List<Integer> result) {
		if (root == null) {
			return;
		}
		accessTree(root.left, result);
		result.add(root.val);
		accessTree(root.right, result);
	}
}


class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;
	
	TreeNode() {
	}
	
	TreeNode(int val) {
		this.val = val;
	}
	
	TreeNode(int val, TreeNode left, TreeNode right) {
		this.val = val;
		this.left = left;
		this.right = right;
	}
}
