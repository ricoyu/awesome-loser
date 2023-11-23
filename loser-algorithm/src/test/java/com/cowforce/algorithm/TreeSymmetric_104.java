package com.cowforce.algorithm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * <p>
 * Copyright: (C), 2022-12-08 8:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TreeSymmetric_104 {
	
	public boolean isSymmetric(TreeNode root) {
		Queue<TreeNode> queue = new LinkedList<>();
		if (root == null) {
			return true;
		}
		TreeNode left = root.left;
		TreeNode right = root.right;
		
		if (left == null && right != null) {
			return false;
		}
		if (left != null && right == null) {
			return false;
		}
		if (left != null && right != null) {
			if (left.val != right.val) {
				return false;
			}
			queue.offer(left);
			queue.offer(right);
		}
		
		while (!queue.isEmpty()) {
			left = queue.poll();
			right = queue.poll();
			
			if (left == null && right != null) {
				return false;
			}
			if (left != null && right == null) {
				return false;
			}
			if (left == null && right == null) {
				continue;
			}
			if (left != null && right != null) {
				if (left.val != right.val) {
					return false;
				}
				queue.offer(left.left);
				queue.offer(right.right);
				queue.offer(left.right);
				queue.offer(right.left);
			}
		}
		
		return true;
	}
}
