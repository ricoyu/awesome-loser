package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 买卖股票的最佳时机 IV
 * <p/>
 * 给你一个整数数组 prices 和一个整数 k ，其中 prices[i] 是某支给定的股票在第 i 天的价格。
 * <p/>
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。也就是说，你最多可以买 k 次，卖 k 次。
 * <p/>
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：k = 2, prices = [2,4,1] <br/>
 * 输出：2 <br/>
 * 解释：在第 1 天 (股票价格 = 2) 的时候买入，在第 2 天 (股票价格 = 4) 的时候卖出，这笔交易所能获得利润 = 4-2 = 2 。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：k = 2, prices = [3,2,6,5,0,3] <br/>
 * 输出：7 <br/>
 * 解释：在第 2 天 (股票价格 = 2) 的时候买入，在第 3 天 (股票价格 = 6) 的时候卖出, 这笔交易所能获得利润 = 6-2 = 4 。 <br/>
 * 随后，在第 5 天 (股票价格 = 0) 的时候买入，在第 6 天 (股票价格 = 3) 的时候卖出, 这笔交易所能获得利润 = 3-0 = 3
 * <p/>
 * 请用Java实现并给出详细的解题思路, Java代码中要加入详细的注释以解释清楚代码逻辑, 要给类取一个合理的类名
 * <p/>
 * 对于比较难以理解的, 需要技巧性的代码逻辑, 请在Java注释之外详细举例说明
 * <p/>
 * 如果是动态规划, 我希望你能就动态规划方程举例说明
 * <p/>
 * 我们可以使用动态规划来解决这个问题。定义状态 dp[i][j] 表示在第 i 天结束时，最多进行了 j 次交易时的最大利润。
 * <ul>为了处理买入和卖出的状态，我们可以进一步细化状态：
 *     <li/>dp[i][j][0] 表示在第 i 天结束时，最多进行了 j 次交易，且当前不持有股票时的最大利润。
 *     <li/>dp[i][j][1] 表示在第 i 天结束时，最多进行了 j 次交易，且当前持有股票时的最大利润。
 * </ul>
 * 状态转移方程:
 * <ul>不持有股票的状态 (dp[i][j][0])
 *     <li/>前一天也不持有股票：dp[i-1][j][0]
 *     <li/>前一天持有股票，今天卖出：dp[i-1][j][1] + prices[i]
 *     <li/>取两者中的最大值。
 * </ul>
 * <ul>持有股票的状态 (dp[i][j][1])
 *     <li/>前一天也持有股票：dp[i-1][j][1]
 *     <li/>前一天不持有股票，今天买入：dp[i-1][j-1][0] - prices[i]
 *     <li/>取两者中的最大值。
 * </ul>
 * <ul>初始条件
 *     <li/>dp[0][j][0] = 0：在第 0 天结束时，不持有股票，利润为 0。
 *     <li/>dp[0][j][1] = -prices[0]：在第 0 天结束时，持有股票，利润为 -prices[0]。
 * </ul>
 * 最终结果: <br/>
 * 最终的结果是 dp[n-1][k][0]，即在最后一天结束时，最多进行了 k 次交易且不持有股票时的最大利润。
 * <p/>
 * Copyright: Copyright (c) 2025-01-19 11:38
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BestTimeToBuyAndSellStockIV {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入prices数组: ");
			String input = scanner.nextLine();
			int[] prices = Arrays.parseOneDimensionArray(input);
			System.out.print("请输入k: ");
			int k = scanner.nextInt();
			System.out.println(maxProfit(prices, k));
			scanner.nextLine();
		}
	}

	public static int maxProfit(int[] prices, int k) {
		if (prices == null || prices.length == 0 || k <= 0) {
			return 0;
		}

		int n = prices.length;

		// 如果 k 大于等于 n/2，相当于可以进行无限次交易
		if (k >= n / 2) {
			int maxProfit = 0;
			for (int i = 1; i < n; i++) {
				if (prices[i] > prices[i - 1]) {
					maxProfit += prices[i] - prices[i - 1];
				}
			}
			return maxProfit;
		}

		// 定义动态规划数组
		int[][][] dp = new int[n][k + 1][2];

		// 初始化
		for (int j = 0; j < k; j++) {
			dp[0][j][0] = 0; // 第 0 天不持有股票
			dp[0][j][1] = -prices[0]; // 第 0 天持有股票
		}

		for (int i = 1; i < n; i++) {
			for (int j = 1; j <= k; j++) {
				// 不持有股票的状态
				/*
				 * dp[i - 1][j][0]：
				 * 表示在第 i-1 天结束时，已经进行了最多 j 次交易，且不持有股票。
				 * 在第 i 天，我们没有进行任何操作（既不买入也不卖出），因此交易次数仍然是 j。
				 * 这里不需要减少交易次数，因为 没有发生新的交易。
				 */
				dp[i][j][0] = Math.max(dp[i - 1][j][0], dp[i - 1][j][1] + prices[i]);
				// 持有股票的状态
				/*
				 * dp[i - 1][j][1] + prices[i]：
				 * 表示在第 i-1 天结束时，已经进行了最多 j 次交易，且持有股票。
				 * 在第 i 天，我们卖出了股票，完成了一次交易。
				 * 由于卖出股票是完成一次交易，但交易次数已经在 j 中体现，因此不需要减少交易次数。
				 */
				dp[i][j][1] = Math.max(dp[i - 1][j][1], dp[i - 1][j - 1][0] - prices[i]);
			}
		}

		// 返回最终结果
		return dp[n - 1][k][0];
	}
}
