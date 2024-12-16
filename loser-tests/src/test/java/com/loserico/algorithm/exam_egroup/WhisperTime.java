package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 给定一个二叉树，每个节点上站一个人，节点数字表示父节点到该节点传递悄悄话需要花费的时间。
 * <p/>
 * 初始时，根节点所在位置的人有一个悄悄话想要传递给其他人，求二叉树所有节点上的人都接收到悄悄话花费的时间。
 * <p/>
 * 输入描述 <br/>
 * 给定二叉树
 * <p/>
 * 0 9 20 -1 -1 15 7 -1 -1 -1 -1 3 2
 * <p/>
 * 输出描述 <br/>
 * 返回所有节点都接收到悄悄话花费的时间
 * <p/>
 * 38
 * <p/>
 * 示例1 <br/>
 * 输入：<br/>
 * 0 9 20 -1 -1 15 15 7 -1 -1 -1 -1 3 2
 * <p/>
 * 输出：<br/>
 * 38
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-09-21 12:32
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class WhisperTime {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 5; i++) {
			System.out.print("请输入二叉树: ");
			String line = scanner.nextLine();
			String[] parts = line.trim().split(" ");
			int[] arr = new int[parts.length];
			for (int j = 0; j < parts.length; j++) {
				arr[j] = Integer.parseInt(parts[j]);
			}

			TreeNode root = buildTree(arr);
			System.out.println(calculateTotalTime(root, 0));
		}
	}
	static class TreeNode {
		int time;
		TreeNode left;
		TreeNode right;

		public TreeNode(int time) {
			this.time = time;
			this.left = this.right = null;
		}
	}

	public static TreeNode insert(TreeNode node, int time) {
		if (node == null) {
			return new TreeNode(time);
		}
		if (time < node.time) {
			node.left = insert(node.left, time);
		} else {
			node.right = insert(node.right, time);
		}
		return node;
	}

	public static TreeNode buildTree(int[] arr) {
		TreeNode root = null;
		for (int time : arr) {
			if (time != -1) {
				root = insert(root, time);
			}
		}
		return root;
	}

	public static int calculateTotalTime(TreeNode node, int currentTime) {
		if (node == null) {
			return 0;
		}

		int leftTime = calculateTotalTime(node.left, currentTime);
		int rightTime = calculateTotalTime(node.right, currentTime);
		currentTime += node.time;

		return currentTime + leftTime + rightTime;
	}
}

