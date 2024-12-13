package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 买卖股票的最佳时机含冷冻期
 * <p/>
 * 给定一个整数数组prices，其中第  prices[i] 表示第 i 天的股票价格 。
 * <p/>
 * 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
 * <p/>
 * 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
 * <p/>
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p/>
 * 示例 1:
 * <p/>
 * 输入: prices = [1,2,3,0,2] <br/>
 * 输出: 3 <br/>
 * 解释: 对应的交易状态为: [买入, 卖出, 冷冻期, 买入, 卖出] <br/>
 * <p/>
 * 示例 2:
 * <p/>
 * 输入: prices = [1] <br/>
 * 输出: 0
 * <p/>
 * <p>
 * 这个问题可以通过动态规划来解决。我们可以定义一个状态 dp[i][j]，表示在第 i 天，处于状态 j 时的最大利润。状态 j 有三个可能的值：<p/>
 * 1. 0：表示今天没有持有股票。 <br/>
 * 2. 1：表示今天持有股票。 <br/>
 * 3. 2：表示今天处于冷冻期。 <br/>
 *
 * <ul>状态转移方程：
 *     <li/>dp[i][0]：当天没有持有股票，可能来自于前一天没有持有股票（dp[i-1][0]）或者前一天处于冷冻期（dp[i-1][2]）并且今天卖出。
 *     <li/>dp[i][1]：当天持有股票，可能来自于前一天没有持有股票（dp[i-1][0]）并且今天买入，或者前一天持有股票（dp[i-1][1]）并且今天不操作。
 *     <li/>dp[i][2]：当天处于冷冻期，可能来自于前一天持有股票（dp[i-1][1]）并且今天卖出。
 * </ul>
 * <ul>初始化：
 *     <li/>dp[0][0] = 0：第一天没有股票，利润为 0。
 *     <li/>dp[0][1] = -prices[0]：第一天持有股票，利润为负的第一个价格。
 *     <li/>dp[0][2] = 0：第一天不能处于冷冻期，利润为 0。
 * </ul>
 * <p>
 * 结果： 最终结果是 max(dp[n-1][0], dp[n-1][2])，因为我们可以选择在最后一天不持有股票或者处于冷冻期。
 * <p/>
 * Copyright: Copyright (c) 2024-12-11 10:06
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StockProfitWithCooldown {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字对: ");
		String input = scanner.nextLine();
		int[] prices = Arrays.parseOneDimensionArray(input);
		System.out.println(maxProfit(prices));
	}

	public static int maxProfit(int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}

		int n = prices.length;

		// dp[i][0] 表示第 i 天没有持有股票
		// dp[i][1] 表示第 i 天持有股票
		// dp[i][2] 表示第 i 天处于冷冻期
		int[][] dp = new int[n][3];

		// 初始化第一天的状态
		dp[0][0] = 0; // 第一天没有持有股票
		dp[0][1] = -prices[0]; // 第一天持有股票
		dp[0][2] = 0; // 第一天不能处于冷冻期

		for (int i = 1; i < n; i++) {
			// dp[i][0]: 第 i 天没有持有股票
			/*
			 * dp[i-1][0]：如果前一天没有持有股票（即第 i-1 天也是不持有股票的状态），那么今天继续不持有股票，利润就是前一天的利润，保持不变。
			 * dp[i-1][2]：如果前一天处于冷冻期（即第 i-1 天卖出并进入冷冻期），那么今天也不持有股票，
			 * 利润就是前一天的利润（因为冷冻期后才能买入），所以今天可以恢复为不持有股票的状态。
			 */
			dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][2]);

			// 这行代码是计算 第 i 天持有股票 时的最大利润。我们可以从两种不同的状态过渡到这个状态：
			//dp[i-1][0] - prices[i]：如果前一天没有持有股票（即第 i-1 天是没有持股的状态），那么今天我们买入股票，利润就是前一天没有持股的利润减去今天买入股票的价格（即今天买入的成本）。
			// 因此，我们需要减去今天的价格 prices[i] 来计算出今天持有股票后的利润。
			//dp[i-1][1]：如果前一天已经持有股票（即第 i-1 天是持股的状态），那么今天我们继续持有股票，利润就是前一天持股的利润不变。
			dp[i][1] = Math.max(dp[i - 1][0] - prices[i], dp[i - 1][1]);

			// dp[i][2]: 第 i 天处于冷冻期
			dp[i][2] = dp[i - 1][1] + prices[i];
		}

		// 最终结果是最后一天不持有股票或者处于冷冻期的最大值
		return Math.max(dp[n - 1][0], dp[n - 1][2]);
	}
}
