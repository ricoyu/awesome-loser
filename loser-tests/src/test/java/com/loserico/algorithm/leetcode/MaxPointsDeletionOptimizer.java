package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 删除并获得点数
 * <p/>
 * 给你一个整数数组 nums ，你可以对它进行一些操作。
 * <p/>
 * 每次操作中，选择任意一个 nums[i] ，删除它并获得 nums[i] 的点数。之后，你必须删除所有等于 nums[i] - 1 和 nums[i] + 1 的元素。
 * <p/>
 * 开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：nums = [3,4,2] <br/>
 * 输出：6 <p/>
 * 解释： <br/>
 * 删除 4 获得 4 个点数，因此 3 也被删除。 <br/>
 * 之后，删除 2 获得 2 个点数。总共获得 6 个点数。
 * <br/>
 * 示例 2：
 * <br/>
 * 输入：nums = [2,2,3,3,3,4] <br/>
 * 输出：9 <p/>
 * 解释： <p/>
 * 删除 3 获得 3 个点数，接着要删除两个 2 和 4 。 <br/>
 * 之后，再次删除 3 获得 3 个点数，再次删除 3 获得 3 个点数。 <br/>
 * 总共获得 9 个点数。
 * <p/>
 * <p>
 * 这个问题中，你需要在每次操作中删除数组中的一个元素 nums[i]，并且得到该元素的点数（即该元素的值）。
 * 删除该元素后，所有等于 nums[i] - 1 和 nums[i] + 1 的元素也必须被删除。目标是最大化通过这些操作获得的总点数。
 *
 * <ul>解题思路的深入解析:
 *     <li/>首先，考虑将问题转化为一种更易于操作的形式。我们将输入数组 nums 转换为一个点数累计数组 sum。
 *     其中，sum[v] 表示所有值为 v 的元素的总点数。这个转换使得问题从处理具体的元素列表变为处理每个数值及其累计点数。
 *     <li/>使用动态规划解决问题。我们定义一个数组 dp，其中 dp[v] 表示在处理到数值 v 时可以获得的最大点数。
 *     <li/>有两种情况：
 *     <li/>不删除 当前数字 v：这种情况下，最大点数仅取决于前一个数字的最大点数，即 dp[v-1]。
 *     <li/>删除当前数字 v：删除 v 后，v-1 和 v+1 都不能选。因此，如果选择了删除 v，那么应该加上 v 的所有点数，再加上处理到 v-2 为止的最大点数，即 dp[v-2] + sum[v]。
 *     <li/>结合以上两种选择，我们可以得到状态转移方程：dp[v]=max(dp[v−1],dp[v−2]+sum[v])
 *     <li/>这个方程意味着，对于每个数字 v，我们可以选择保留前一个数字的最大点数（不删除 v），或者是删除 v 并加上从 v-2 传递过来的最大点数。
 *     <li/>初始化 dp[0] = sum[0]，因为只有一个数字时，最大点数就是该数字的所有点数。
 *     <li/>如果存在多于一个数字，则 dp[1] = \max(sum[0], sum[1])，选择 0 和 1 中点数较高的一个，因为它们不能同时被选择。
 *     <li/>遍历整个 dp 数组来填充所有的值，最终 dp 数组的最后一个元素（即 dp[maxNum]，其中 maxNum 是数组中的最大值）将是整个问题的答案。
 * </ul>
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-10-24 16:27
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MaxPointsDeletionOptimizer {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.print("请输入第"+(i+1)+"个cost数组: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] nums = new int[parts.length];
			for(int j = 0; j < parts.length; j++) {
			  nums[j] = Integer.parseInt(parts[j].trim());
			}
			System.out.println(deleteAndEarn(nums));
		}
	}

	public static int deleteAndEarn(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}

		// 找出数组中的最大值，为了建立大小合适的 sum 数组
		int maxNum = 0;
		for (int num : nums) {
			maxNum = Math.max(maxNum, num);
		}

		// 创建 sum 数组，并累计每个数字的总点数
		int[] sum = new int[maxNum + 1];
		for (int num : nums) {
			sum[num] += num;
		}

		// 初始化 dp 数组
		int[] dp = new int[maxNum + 1];
		dp[0] = sum[0];
		if (maxNum > 0) {
			dp[1] = Math.max(sum[0], sum[1]);
		}

		// 填充 dp 数组
		for (int i = 2; i <= maxNum; i++) {
			dp[i] = Math.max(dp[i - 1], dp[i - 2] + sum[i]);
		}

		// 返回可以获得的最大点数
		return dp[maxNum];
	}
}
