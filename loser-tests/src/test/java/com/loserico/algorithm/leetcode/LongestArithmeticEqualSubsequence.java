package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 最长等差数列
 * <p/>
 * 给你一个整数数组 nums，返回 nums 中最长等差子序列的长度。
 * <p/>
 * 回想一下，nums 的子序列是一个列表 nums[i1], nums[i2], ..., nums[ik] ，且 0 <= i1 < i2 < ... < ik <= nums.length - 1。并且如果 seq[i+1]
 * - seq[i]( 0 <= i < seq.length - 1) 的值都相同，那么序列 seq 是等差的。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：nums = [3,6,9,12] <br/>
 * 输出：4 <br/>
 * 解释： <br/>
 * 整个数组是公差为 3 的等差数列。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：nums = [9,4,7,2,10] <br/>
 * 输出：3 <br/>
 * 解释： <br/>
 * 最长的等差子序列是 [4,7,10]。
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：nums = [20,1,15,3,10,5,8] <br/>
 * 输出：4 <br/>
 * 解释： <br/>
 * 最长的等差子序列是 [20,15,10,5]。
 * <p/>
 * 要解决这个问题，我们可以使用动态规划来找出数组中最长的等差子序列的长度。基本思想是，我们可以使用一个哈希表来存储对于每个元素 nums[i]，以不同的公差 d 结尾的等差子序列的最大长度。
 *
 * <ol>解题思路
 *     <li/>初始化：为每个元素 nums[i] 创建一个哈希表 dp[i]，其中 dp[i] 的键是公差 d，值是以 nums[i] 结尾并且公差为 d 的最长等差子序列的长度。
 *     <li/>双重遍历：对于数组中的每一对元素 nums[i] 和 nums[j] （其中 j > i），计算它们之间的差 diff = nums[j] - nums[i]。
 *     <li/>更新：对于每个差值 diff，如果 dp[i] 中已经有了公差为 diff 的记录，那么将 dp[j][diff] 更新为 dp[i][diff] + 1。如果没有，则初始化为
 *     2（因为至少有两个数，nums[i] 和 nums[j]）。
 *     <li/>结果：整个数组中，dp[j][diff] 的最大值就是答案。
 * </ol>
 * Copyright: Copyright (c) 2024-11-30 9:39
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestArithmeticEqualSubsequence {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字对: ");
		String input = scanner.nextLine().trim();
		int[] nums = Arrays.parseOneDimensionArray(input);
		System.out.println(longestArithSeqLength(nums));
	}

	public static int longestArithSeqLength(int[] nums) {
		int n = nums.length;
		int longest = 0;

		Map<Integer, Integer>[] dp = new HashMap[n];
		for (int i = 0; i < n; i++) {
			dp[i] = new HashMap<>();
		}

		// 双重遍历数组中的每对元素
		for (int j = 1; j < n; j++) {
			for (int i = 0; i < j; i++) {
				int diff = nums[j] - nums[i];// 计算公差
				// 如果dp[i][diff]存在则取其值+1，否则从1开始计数+1
				int len = dp[i].getOrDefault(diff, 1) + 1;
				// 更新dp[j][diff]为最大长度
				dp[j].put(diff, Math.max(dp[j].getOrDefault(diff, 0), len));
				// 更新最大长度
				longest = Math.max(longest, dp[j].get(diff));
			}
		}
		return longest;
	}
}
