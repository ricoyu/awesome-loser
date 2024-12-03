package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 接雨水
 * <p/>
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：height = [0,1,0,2,1,0,1,3,2,1,2,1] <br/>
 * 输出：6 <br/>
 * 解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水。 <br/>
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：height = [4,2,0,3,2,5] <br/>
 * 输出：9
 * <p/>
 * 我们可以通过“双指针”方法来解决这个“接雨水”问题。该方法的基本思想是从两端向中间遍历，通过维护左边和右边的最大高度来计算雨水。
 * <p/>
 * <ol>
 *     <li/>初始化双指针和最大高度：用两个指针 left 和 right 分别指向数组的两端。我们还需要两个变量 leftMax 和 rightMax 来记录当前指针位置的左侧和右侧的最大高度。
 *     <li/>遍历和更新指针：
 *          比较 leftMax 和 rightMax: <br/>
 *              如果 leftMax 小于 rightMax，说明左边相对较矮，此时可以确定 left 位置的接水量，由 leftMax 减去 height[left]。然后将 left 指针向右移动。 <br/>
 *              如果 rightMax 小于或等于 leftMax，说明右边相对较矮，可以确定 right 位置的接水量，由 rightMax 减去 height[right]。然后将 right 指针向左移动。 <br/>
 *     <li/>继续遍历直到 left 和 right 相遇：在这个过程中不断累计雨水量，直到两指针相遇，最终得到总的雨水量。
 * </ol>
 * <p>
 * <p>
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-11-15 8:28
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RainWaterTrap {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.print("请输入数组: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] height = new int[parts.length];
			for (int j = 0; j < parts.length; j++) {
				height[j] = Integer.parseInt(parts[j].trim());
			}
			System.out.println(trap(height));
		}
	}

	public static int trap(int[] height) {
		if (height == null || height.length == 0) {
			// 如果数组为空或长度为0，则无法接到雨水
			return 0;
		}

		int left = 0;
		int right = height.length - 1;
		int leftMax = 0; // 左侧最大高度初始化
		int rightMax = 0; // 右侧最大高度初始化
		int waterTrapped = 0; // 用于存储接到的雨水总量

		while (left < right) {
			// 判断哪一侧较低，从较低的一侧计算雨水量
			if (height[left] < height[right]) {
				// 如果左侧柱子高度小于右侧，则从左侧计算雨水量
				if (height[left] > leftMax) {
					leftMax = height[left];
				} else {
					// 计算当前位置能接到的雨水量，并累加到总量中
					waterTrapped += leftMax - height[left];
				}
				// 移动左指针向右
				left++;
			} else {
				// 如果右侧柱子高度小于等于左侧，则从右侧计算雨水量
				if (height[right] >= rightMax) {
					// 更新右侧最大高度
					rightMax = height[right];
				} else {
					// 计算当前位置能接到的雨水量，并累加到总量中
					waterTrapped += rightMax - height[right];
				}
				// 移动右指针向左
				right--;
			}
		}
		return waterTrapped;
	}
}
