package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-08 12:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InverseTree_226 {
	
	public TreeNode invertTree(TreeNode root) {
		invert(root);
		return root;
	}
	
	public void invert(TreeNode root) {
		if (root == null) {
			return;
		}
		if (root.left == null && root.right == null) {
			return;
		}
		invert(root.left);
		invert(root.right);
		TreeNode tmp = root.left;
		root.left = root.right;
		root.right = tmp;
	}
}
