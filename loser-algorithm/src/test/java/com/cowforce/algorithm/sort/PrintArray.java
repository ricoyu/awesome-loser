package com.cowforce.algorithm.sort;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2022-12-08 12:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PrintArray {
	
	public final static int[] SRC = {86, 39, 77, 23, 32, 45, 58, 63, 93, 4, 37, 22};
	
	public static void print(int[] array) {
		for (int i : array) {
			System.out.print(i + "  ");
		}
		System.out.println("");
	}
	
	public static void printObject(List<Integer> array) {
		for (int i : array) {
			System.out.print(i + "  ");
		}
		System.out.println("");
	}
	
	public static void printIndex(int[] array, int begin, int end) {
		for (int i = begin; i <= end; i++) {
			System.out.print(array[i] + "  ");
		}
		System.out.println("");
	}
	
	/**
	 * 左子树是 2*i+1, 右子树是 2*(i+1)
	 *
	 * @param nums
	 * @param rootIndex
	 * @return
	 */
	public static int getTreeDepth(int[] nums, int rootIndex) {
		if (rootIndex < nums.length) {
			return nums == null ? 0 : (1 + Math.max(getTreeDepth(nums, 2 * rootIndex + 1), getTreeDepth(nums, 2 * (rootIndex + 1))));
		}
		return 0;
	}
	
	private static void writeArray(int[] nums, int nodeIndex, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
		// 保证输入的树不为空
		if (nodeIndex < 0 || nodeIndex >= nums.length) {
			return;
		}
		// 先将当前节点保存到二维数组中
		res[rowIndex][columnIndex] = String.valueOf(nums[nodeIndex]);
		
		// 计算当前位于树的第几层
		int currLevel = ((rowIndex + 1) / 2);
		// 若到了最后一层，则返回
		if (currLevel == treeDepth) {
			return;
		}
		// 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
		int gap = treeDepth - currLevel - 1;
		
		// 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
		int left = 2 * nodeIndex + 1;
		if (left < nums.length) {
			res[rowIndex + 1][columnIndex - gap] = "/";
			writeArray(nums, left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
		}
		
		// 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
		int right = 2 * (nodeIndex + 1);
		if (right < nums.length) {
			res[rowIndex + 1][columnIndex + gap] = "\\";
			writeArray(nums, right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
		}
	}
	
	/**
	 * 用树形结构打印这棵树, 如
	 * <pre>
	 *             35
	 *          /     \
	 *       63          48
	 *     /   \       /   \
	 *   9       86  24      53
	 *  /
	 * 11
	 * </pre>
	 *
	 * @param nums
	 */
	public static void printTree(int[] nums) {
		if (nums == null || nums.length == 0) {
			System.out.println("EMPTY!");
			return;
		}
		// 得到树的深度
		int treeDepth = getTreeDepth(nums, 0);
		
		// 最后一行的宽度为2的（n - 1）次方乘3，再加1
		// 作为整个二维数组的宽度
		int arrayHeight = treeDepth * 2 - 1;
		int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
		// 用一个字符串数组来存储每个位置应显示的元素
		String[][] res = new String[arrayHeight][arrayWidth];
		// 对数组进行初始化，默认为一个空格
		for (int i = 0; i < arrayHeight; i++) {
			for (int j = 0; j < arrayWidth; j++) {
				res[i][j] = " ";
			}
		}
		
		// 从根节点开始，递归处理整个树
		// res[0][(arrayWidth + 1)/ 2] = (char)(root.val + '0');
		writeArray(nums, 0, 0, arrayWidth / 2, res, treeDepth);
		
		// 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
		for (String[] line : res) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < line.length; i++) {
				sb.append(line[i]);
				if (line[i].length() > 1 && i <= line.length - 1) {
					i += line[i].length() > 4 ? 2 : line[i].length() - 1;
				}
			}
			System.out.println(sb.toString());
		}
	}
}
