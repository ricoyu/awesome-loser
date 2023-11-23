package com.cowforce.algorithm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2022-12-07 10:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PostOrderTraversalTree_145 {
	
	public List<Integer> postorderTraversal(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		accessTree(root, result);
		return result;
	}
	
	public void accessTree(TreeNode root, List<Integer> result) {
		if (root == null) {
			return;
		}
		accessTree(root.left, result);
		accessTree(root.right, result);
		result.add(root.val);
	}
	
	public List<Integer> postorderTraversalLoop(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		Deque<TreeNode> stack = new LinkedList<>();
		TreeNode preAccessed = null;
		while (root != null || !stack.isEmpty()) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			
			root = stack.pop();
			if (root.right == null || root.right == preAccessed) {
				result.add(root.val);
				preAccessed = root;
				root = null;
			} else {
				stack.push(root);
				root = root.right;
			}
		}
		return result;
	}
}
