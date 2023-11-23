package com.cowforce.algorithm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2022-12-07 8:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InorderTraversalTree_144 {
	
	public List<Integer> preorderTraversalLoop(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		Deque<TreeNode> stack = new LinkedList<>();
		while (root != null || !stack.isEmpty()) {
			while (root != null) {
				result.add(root.val);
				stack.push(root);
				root = root.left;
			}
			root = stack.pop();
			root = root.right;
		}
		
		return result;
	}
	public List<Integer> preorderTraversal(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		accessTree(root, result);
		return result;
	}
	
	public void accessTree(TreeNode root, List<Integer> result) {
		if (root == null) {
			return;
		}
		result.add(root.val);
		accessTree(root.left, result);
		accessTree(root.right, result);
	}
}
