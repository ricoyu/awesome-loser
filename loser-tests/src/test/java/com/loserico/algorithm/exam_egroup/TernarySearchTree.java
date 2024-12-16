package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 定义构造三又搜索树规则如下: 每个节点都存有一个数，当插入一个新的数时，从根节点向下寻找，直到找到一个合适的空节点插入查找的规则是:
 * <p/>
 * 1.如果数小于节点的数减去500，则将数插入节点的左子树
 * <p/>
 * 2.如果数大于节点的数加上500，则将数插入节点的右子树
 * <p/>
 * 3.否则，将数插入节点的中子树
 * <p/>
 * 给你一系列数，请按以上规则，按顺序将数插入树中，构建出一棵三叉搜索树，最后输出树的高度。
 * <ul>输入描述:
 *     <li/>第一行为一个数N，表示有N个数，1<=N<=10000
 *     <li/>第二行为N个空格分隔的整数，每个数的范围为[1,10000]
 * </ul>
 * 输出描述: 输出树的高度(根节点的高度为1)
 * <p/>
 * 示例1:
 * <p/>
 * 输入:
 * <pre> {@code
 * 5
 * 5000 2000 5000 8000 1800
 * }</pre>
 * <p>
 * 输出: 3
 * <p/>
 * 示例2:
 * <p/>
 * 输入:
 * <pre> {@code
 * 3
 * 5000 4000 3000
 * }</pre>
 * <p>
 * 输出: 3
 * <p/>
 * Copyright: Copyright (c) 2024-09-16 11:13
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */

class TreeNode1 {
	int value;
	TreeNode1 left, middle, right;

	public TreeNode1(int value) {
		this.value = value;
		this.left = this.middle = this.right = null;
	}

}

public class TernarySearchTree {

	private TreeNode1 root;

	public TernarySearchTree() {
		root = null;
	}

	public static void main(String[] args) {
		TernarySearchTree tree = new TernarySearchTree();
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字n: ");
		int n = scanner.nextInt();
		scanner.nextLine();
		System.out.print("请输入" + n + " 个数字, 空格隔开: ");
		String line = scanner.nextLine();
		String[] parts = line.trim().split(" ");
		for (int j = 0; j < parts.length; j++) {
			int value = Integer.parseInt(parts[j]);
			tree.insert(value);
		}

		scanner.close();
		System.out.println("树的高度为: " + tree.getHeight());
	}

	public void insert(int value) {
		this.root = insert(root, value);
	}

	public TreeNode1 insert(TreeNode1 node, int value) {
		// 如果节点为空，创建一个新节点
		if (node == null) {
			return new TreeNode1(value);
		}

		// 根据规则递归地将值插入到左、中或右子树
		if (value < node.value - 500) {
			node.left = insert(node.left, value);
		} else if (value > node.value + 500) {
			node.right = insert(node.right, value);
		} else {
			node.middle = insert(node.middle, value);
		}
		return node;
	}

	public int getHeight() {
		return getHeight(root);
	}

	private int getHeight(TreeNode1 node) {
		if (node == null) {
			return 0;
		}

		// 获取三个子树中的最大高度，并加上当前节点
		int leftHeight = getHeight(node.left);
		int moddileHeight = getHeight(node.middle);
		int rightHeight = getHeight(node.right);
		return 1 + Math.max(leftHeight, Math.max(moddileHeight, rightHeight));
	}
}
