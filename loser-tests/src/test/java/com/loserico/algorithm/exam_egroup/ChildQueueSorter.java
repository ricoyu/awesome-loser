package com.loserico.algorithm.exam_egroup;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 题目描述
 * <p/>
 * 现在有一队小朋友，他们高矮不同，我们以正整数数组表示这一队小朋友的身高，如数组{5,3,1,2,3}
 * <p/>
 * 我们现在希望小朋友排队，以“高”“矮”“高”“矮”顺序排列，每一个“高”位置的小朋友要比相邻的位置高或者相等；每一个“矮”位置的小朋友要比相邻的位置矮或者相等；
 * <p/>
 * 要求小朋友们移动的距离和最小，第一个从“高”位开始排，输出最小移动距离即可。
 * <p/>
 * 例如，在示范小队{5,3,1,2,3}中，{5, 1, 3, 2, 3}是排序结果。{5, 2, 3, 1, 3} 虽然也满足“高”“矮”“高”“矮”顺序排列，但小朋友们的移动距离大，所以不是最优结果。
 * <p/>
 * 移动距离的定义如下所示：第二位小朋友移到第三位小朋友后面，移动距离为1，若移动到第四位小朋友后面，移动距离为2；
 * <p/>
 * 输入描述: <br/>
 * 排序前的小朋友，以英文空格的正整数： 4 3 5 7 8
 * <br/>
 * 注：小朋友<100个
 * <p/>
 * 输出描述:
 * <br/>
 * 排序后的小朋友，以英文空格分割的正整数： 4 3 7 5 8
 * <ul>用例1:
 *     <li/>输入: 4 1 3 5 2
 *     <li/>输出：4 1 5 2 3
 * </ul>
 *
 * <ul>用例2:
 *     <li/>输入: 1 1 1 1 1
 *     <li/>输出: 1 1 1 1 1
 * </ul>
 * 说明：相邻位置可以相等
 *
 * <ul>用例3:
 *     <li/>输入: xxx
 *     <li/>输出: []
 * </ul>
 * <p>
 * 说明：出现非法参数情况，返回空数组
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-09-13 16:01
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ChildQueueSorter {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		for (int i = 0; i < 3; i++) {
			System.out.print("请输入排序前的小朋友: ");
			String children = scanner.nextLine();

			System.out.println("排序后的小朋友: " + sortChildren(children));
		}
		scanner.close();
	}

	public static String sortChildren(String children) {
		if (children == null || children.trim().isEmpty()) {
			return "[]";
		}

		String[] parts = children.trim().split(" ");
		int[] heights = new int[parts.length];

		try {
			for (int i = 0; i < parts.length; i++) {
				heights[i] = Integer.parseInt(parts[i]);
			}
		} catch (NumberFormatException e) {
			return "[]";
		}
		if (heights.length == 0) {
			return "[]";
		}

		return format(optimalHeightSequence(heights));
	}

	public static int[] optimalHeightSequence(int[] heights) {
		// 先复制原数组并排序
		int[] sortedHeights = heights.clone();
		Arrays.sort(sortedHeights);

		int[] result = new int[heights.length];
		int smallIndex = 0;
		int largeIndex = (heights.length + 1) / 2; // 从中间向后的元素为高位

		// 填充结果数组
		for (int i = 0; i < heights.length; i++) {
			if (i % 2 == 0) {// 偶数索引填大值
				if (largeIndex < sortedHeights.length) {
					result[i] = sortedHeights[largeIndex++];
				} else if (smallIndex < largeIndex) { // 防止 largeIndex 越界
					result[i] = sortedHeights[smallIndex++];
				}
			} else {// 奇数索引填小值
				if (smallIndex < largeIndex) {
					result[i] = sortedHeights[smallIndex++];
				} else if (largeIndex < sortedHeights.length) { // 处理小值已用完的情况
					result[i] = sortedHeights[largeIndex++];
				}
			}
		}

		return result;
	}

	public static String format(int[] heights) {
		StringBuilder sb = new StringBuilder();
		for (int height : heights) {
			sb.append(height).append(" ");
		}
		return sb.toString().trim();
	}
}
