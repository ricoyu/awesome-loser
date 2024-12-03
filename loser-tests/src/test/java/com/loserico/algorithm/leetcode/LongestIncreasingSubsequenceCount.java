package com.loserico.algorithm.leetcode;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 最长递增子序列的个数
 * <p/>
 * 给定一个未排序的整数数组 nums ， 返回最长递增子序列的个数 。 <p/>
 * 注意 这个数列必须是 严格 递增的。 <p/>
 * <p>
 * 示例 1:
 * <p/>
 * 输入: [1,3,5,4,7] <br/>
 * 输出: 2 <br/>
 * 解释: 有两个最长递增子序列，分别是 [1, 3, 4, 7] 和[1, 3, 5, 7]。
 * <p/>
 * 示例 2:
 * <p/>
 * 输入: [2,2,2,2,2] <br/>
 * 输出: 5 <br/>
 * 解释: 最长递增子序列的长度是1，并且存在5个子序列的长度为1，因此输出5。
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-11-26 9:15
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestIncreasingSubsequenceCount {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数组元素: ");
		String input = scanner.nextLine().trim();
		String[] parts = input.split(",");
		int[] nums = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			nums[i] = Integer.parseInt(parts[i].trim());
		}
		scanner.close();

		System.out.println(findNumberOfLIS(nums));
	}

	public static int findNumberOfLIS(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}

		int n = nums.length;
		int[] lengths = new int[n];
		int[] counts = new int[n];

		Arrays.fill(lengths, 1);
		Arrays.fill(counts, 1);

		int maxLength = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				if (nums[j] < nums[i]) {
					if (lengths[j] + 1 > lengths[i]) {
						lengths[i] = lengths[j] + 1;
						counts[i] = counts[j];
					} else if (lengths[j] + 1 == lengths[i]) {
						counts[i] += counts[j];
					}
				}
			}
			maxLength = Math.max(maxLength, lengths[i]);
		}

		int count = 0;
		for (int i = 0; i < n; i++) {
			if (lengths[i] == maxLength) {
				count += counts[i];
			}
		}

		return count;
	}
}
