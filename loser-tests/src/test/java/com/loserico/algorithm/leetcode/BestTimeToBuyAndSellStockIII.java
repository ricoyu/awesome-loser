package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 买卖股票的最佳时机 III
 * <p/>
 * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
 * <p/>
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 两笔 交易。
 * <p/>
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p/>
 * 示例 1:
 * <p/>
 * 输入：prices = [3,3,5,0,0,3,1,4] <br/>
 * 输出：6 <br/>
 * 解释：在第 4 天（股票价格 = 0）的时候买入，在第 6 天（股票价格 = 3）的时候卖出，这笔交易所能获得利润 = 3-0 = 3 。 <br/>
 * 随后，在第 7 天（股票价格 = 1）的时候买入，在第 8 天 （股票价格 = 4）的时候卖出，这笔交易所能获得利润 = 4-1 = 3 。 <br/>
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：prices = [1,2,3,4,5] <br/>
 * 输出：4 <br/>
 * 解释：在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。 <br/>
 * 注意你不能在第 1 天和第 2 天接连购买股票，之后再将它们卖出。 <br/>
 * 因为这样属于同时参与了多笔交易，你必须在再次购买前出售掉之前的股票。 <br/>
 *
 * <ul>动态规划状态定义：
 *     <li/>用 dp[i][k][0] 表示在第 i 天，最多完成 k 次交易，手上 没有股票 时的最大利润。
 *     <li/>用 dp[i][k][1] 表示在第 i 天，最多完成 k 次交易，手上 持有股票 时的最大利润。
 * </ul>
 *
 * <ul>状态转移方程：
 *     <li/>如果手上没有股票： dp[i][k][0]=max(dp[i−1][k][0],dp[i−1][k][1]+prices[i])
 *     <ul>
 *      <li/>第 i 天不做任何操作：保持前一天没有股票的状态，利润为 dp[i-1][k][0]。
 *      <li/>第 i 天卖出股票：前一天持有股票，今天卖出，利润为 dp[i-1][k][1] + prices[i]。
 *     </ul>
 *     <li/>如果手上有股票： dp[i][k][1]=max(dp[i−1][k][1],dp[i−1][k-1][0]−prices[i])
 *     <ul>
 *      <li/>第 i 天不做任何操作：保持前一天持有股票的状态，利润为 dp[i-1][k][1]。
 *      <li/>第 i 天买入股票：前一天没有股票且完成了 k-1 次交易，今天买入，利润为 dp[i-1][k-1][0] - prices[i]。
 *     </ul>
 * </ul>
 * <ul>初始状态：
 *     <li/>dp[0][k][0] = 0（第 0 天没有股票，利润为 0）
 *     <li/>dp[0][k][1] = -prices[0]（第 0 天买入股票，利润为 -prices[0]）
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-12-22 11:07
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BestTimeToBuyAndSellStockIII {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字对: ");
		String input = scanner.nextLine();
		int[] prices = Arrays.parseOneDimensionArray(input);
		System.out.println(maxProfit(prices));
	}

	public static int maxProfit(int[] prices) {
		// 边界条件：如果没有价格或天数少于两天，直接返回 0
		if (prices == null || prices.length < 2) {
			return 0;
		}

		int maxTransactions = 2;
		int n = prices.length;

		// 定义 dp 数组
		// dp[i][k][0] 表示第 i 天，最多进行 k 次交易，手上无股票的最大利润
		// dp[i][k][1] 表示第 i 天，最多进行 k 次交易，手上持有股票的最大利润
		int[][][] dp = new int[n][maxTransactions + 1][2];

		// 初始化
		for (int k = 0; k <= maxTransactions; k++) {
			dp[0][k][0] = 0;// 第 0 天无股票的利润为 0
			dp[0][k][1] = -prices[0]; // 第 0 天持有股票的利润为 -prices[0]（买入股票）
		}

		// 动态规划填表
		for (int i = 1; i < n; i++) {
			for (int k = 1; k <= maxTransactions; k++) {
				// 第 i 天无股票的最大利润
				dp[i][k][0] = Math.max(dp[i - 1][k][0], dp[i - 1][k][1] + prices[i]);
				// 第 i 天持有股票的最大利润
				dp[i][k][1] = Math.max(dp[i - 1][k][1], dp[i - 1][k - 1][0] - prices[i]);
			}
		}


		// 返回最后一天，最多完成 2 笔交易，且手上无股票的最大利润
		return dp[n - 1][maxTransactions][0];
	}
}
