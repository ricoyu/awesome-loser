package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 最长递增子序列
 * <p>
 * 给你一个整数数组 nums, 找到其中最长严格递增子序列的长度。<br/>
 * 子序列 是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
 * <p>
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
 * <p>
 * 为了解决这个问题，我们可以使用一个动态规划的方法。思路是使用一个数组 dp 来存储每个位置结尾的最长递增子序列的长度。具体算法如下：
 * <ol>
 *     <li/>初始化一个和输入数组 nums 等长的数组 dp, 并将所有元素初始化为1, 因为每个元素至少可以单独成为一个递增子序列。
 *     <li/>对于数组 nums 的每个元素, 遍历其之前的所有元素, 检查是否可以将当前元素添加到之前的递增子序列中。
 *     <li/>如果当前元素 nums[j] 大于之前某元素 nums[i]（其中 i < j），那么就可以将当前元素加入由 nums[i] 结尾的递增子序列中，更新 dp[j] 为 dp[i] + 1 和现有的 dp[j]
 *     中的较大值。
 *     <li/>最后，dp 数组中的最大值即为整个数组的最长递增子序列的长度。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-11-21 8:47
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
		String input = scanner.nextLine().trim();
		int[] nums = Arrays.parseOneDimensionArray(input);
		scanner.close();

		System.out.println(lengthOfLIS(nums));
	}

	public static int lengthOfLIS(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}

		//dp数组, dp[i]表示以nums[i]结尾的最长递增子序列的长度
		int[] dp = new int[nums.length];
		// 初始情况, 每个元素自身至少可以构成长度为1的递增子序列
		for (int i = 0; i < nums.length; i++) {
			dp[i] = 1;
		}

		// 填充dp数组
		for (int j = 1; j < nums.length; j++) {
			for (int i = 0; i < j; i++) {
				// 如果nums[j]可以接在nums[i]后面形成递增序列
				if (nums[j] > nums[i]) {
					// 更新dp[i], 取dp[j] + 1和现有的dp[i]的最大值
					dp[j] = Math.max(dp[j], dp[i] + 1);
				}
			}
		}

		// 遍历dp数组，找到最大值
		int max = 0;
		for (int i = 0; i < dp.length; i++) {
			if (dp[i] > max) {
				max = dp[i];
			}
		}

		return max;
	}
}
