package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 轮转数组
 * <p>
 * 给定一个整数数组 nums，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。
 * <p>
 * 示例 1:
 * <p>
 * 输入: nums = [1,2,3,4,5,6,7], k = 3 <br/>
 * 输出: [5,6,7,1,2,3,4] <br/>
 * 解释: <br/>
 * 向右轮转 1 步: [7,1,2,3,4,5,6] <br/>
 * 向右轮转 2 步: [6,7,1,2,3,4,5] <br/>
 * 向右轮转 3 步: [5,6,7,1,2,3,4] <br/>
 * <p>
 * 示例 2:
 * <p>
 * 输入：nums = [-1,-100,3,99], k = 2 <br/>
 * 输出：[3,99,-1,-100] <br/>
 * 解释: <br/>
 * 向右轮转 1 步: [99,-1,-100,3] <br/>
 * 向右轮转 2 步: [3,99,-1,-100] <br/>
 * <p>
 * <p>
 * 在处理数组的轮转问题时，可以采用几种不同的方法来实现。
 * 以下是一种比较直接的解法，即通过使用额外的空间来简化问题的理解和实现。
 * 该方法的基本思想是创建一个新数组，将原始数组的元素按照轮转后的顺序复制到新数组中，
 * 然后再将新数组的内容复制回原数组。虽然这种方法的空间复杂度较高（O(n)），但它简单易懂，适合用来说明问题的解决思路。
 * <p/>
 * Copyright: Copyright (c) 2024-11-06 11:27
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RotateArray {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.print("请输入数组nums1: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] nums = new int[parts.length];
			for (int j = 0; j < parts.length; j++) {
				nums[j] = Integer.parseInt(parts[j].trim());
			}
			System.out.print("请输入数字k: ");
			int k = scanner.nextInt();
			scanner.nextLine();
			rotate(nums, k);
			Arrays.print(nums);
		}
	}

	public static void rotate(int[] nums, int k) {
		if (nums == null || nums.length <= 1 || k < 0) {
			return;
		}

		int n = nums.length;

		// 确保k不大于数组长度
		k = k % n;

		if (k == 0) {
			return;
		}

		// 创建一个新数组用于存放轮转后的元素
		int[] rotated = new int[n];

		// 计算新的起始索引，并从这里开始填充新数组
		for (int i = 0; i < n; i++) {
			// 新位置 = (当前索引 + k) % 数组长度
			rotated[(i + k) % n] = nums[i];
		}

		// 将新数组的内容复制回原数组
		System.arraycopy(rotated, 0, nums, 0, n);
	}
}
