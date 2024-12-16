package com.loserico.algorithm.leetcode.round3;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 最长递增子序列
 *
 * 示例 1：
 * <p>
 * 输入：nums = [10,9,2,5,3,7,101,18] <br/>
 * 输出：4 <br/>
 * 解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
 * <p>
 * 示例 2：
 * <p>
 * 输入：nums = [0,1,0,3,2,3] <br/>
 * 输出：4
 * <p>
 * 示例 3：
 * <p>
 * 输入：nums = [7,7,7,7,7,7,7] <br/>
 * 输出：1
 * <p/>
 * Copyright: Copyright (c) 2024-12-13 9:09
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestIncreasingSubsequence {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数组元素: ");
		String input = scanner.nextLine();
		int[] nums = Arrays.parseOneDimensionArray(input);
		System.out.println(lengthOfLIS(nums));
	}

	public static int lengthOfLIS(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}

		int n = nums.length;
		int[] dp = new int[n];

		// 初始化dp数组, 每个元素自身至少可以构成长度为1的递增子序列
		for (int i = 0; i < n; i++) {
			dp[i] = 1;
		}

		for (int i = 1; i < n; i++) {
			for (int j = 0; j < i; j++) {
				if (nums[i] > nums[j]) {
					dp[i] = Math.max(dp[i], dp[j] + 1);
				}
			}
		}

		int max = 0;
		for (int i = 0; i < dp.length; i++) {
			if (dp[i] > max) {
				max = dp[i];
			}
		}

		return max;
	}
}
