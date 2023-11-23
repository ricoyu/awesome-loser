package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-08 12:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BalancedBinTree_110 {
	
	public boolean isBalanced(TreeNode root) {
		int depth = depth(root);
		return depth != -1;
	}
	
	public int depth(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int leftDepth = depth(root.left);
		int rightDepth = depth(root.right);
		
		if (leftDepth == -1 || rightDepth == -1 || Math.abs(leftDepth - rightDepth) > 1) {
			return -1;
		}
		//这个节点的高度等于其左右子树高度大的那个+1
		return Math.max(leftDepth, rightDepth) + 1;
	}
}
