package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 买卖股票的最佳时机 II
 * <p/>
 * 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
 * <p/>
 * 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。你也可以先购买，然后在 同一天 出售。
 * <p/>
 * 返回 你能获得的 最大 利润 。
 *
 * 示例 1：
 * <p/>
 * 输入：prices = [7,1,5,3,6,4] <br/>
 * 输出：7 <br/>
 * 解释：在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5 - 1 = 4。 <br/>
 * 随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 这笔交易所能获得利润 = 6 - 3 = 3。 <br/>
 * 最大总利润为 4 + 3 = 7 。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：prices = [1,2,3,4,5] <br/>
 * 输出：4 <br/>
 * 解释：在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5 - 1 = 4。 <br/>
 * 最大总利润为 4 。
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：prices = [7,6,4,3,1] <br/>
 * 输出：0 <br/>
 * 解释：在这种情况下, 交易无法获得正利润，所以不参与交易可以获得最大利润，最大利润为 0。
 *
 * <p/>
 * 请用Java实现并给出详细的解题思路, Java代码中要加入详细的注释以解释清楚代码逻辑, 要给类取一个合理的类名
 * <p/>
 * 在这个问题中，我们的目标是确定股票买卖的最佳时机，以便最大化利润。根据题目描述，我们可以在任何一天买入或卖出股票，并且在同一天买入后即可卖出。
 * 因此，解决方案的关键在于捕捉所有上升趋势中的利润。每次价格上升时，即股票价值从一天到另一天增加，我们都认为是一次盈利的机会。
 * <p/>
 * 我们可以通过简单的一次遍历来实现这一策略，只需判断当前的价格是否比前一天高，如果是，就将差价加到总利润中。
 * 这种策略的时间复杂度为O(n)，其中n是价格数组的长度，空间复杂度为O(1)。
 * <p/>
 * Copyright: Copyright (c) 2024-11-08 8:38
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StockProfitCalculator {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for(int i = 0; i < 2; i++) {
			System.out.print("请输入股票价格: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] prices = new int[parts.length];
			for (int j = 0; j < parts.length; j++) {
				prices[j] = Integer.parseInt(parts[j].trim());
			}
			System.out.println(maxProfit(prices));
		}
	}

	public static int maxProfit(int[] prices) {
		// 初始化利润为0
		int maxProfit = 0;

		// 遍历价格数组，从第二天开始比较
		for(int i = 1; i < prices.length; i++) {
		  if (prices[i] > prices[i-1]) {
			  // 如果第i天的价格比第i-1天高，则视为盈利机会
			  maxProfit+= prices[i] - prices[i-1];
		  }
		}

		// 返回计算出的最大利润
		return maxProfit;
	}
}
