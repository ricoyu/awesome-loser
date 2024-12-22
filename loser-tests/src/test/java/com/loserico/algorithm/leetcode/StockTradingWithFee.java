package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 买卖股票的最佳时机含手续费
 * <p>
 * 给定一个整数数组 prices，其中 prices[i]表示第 i 天的股票价格 ；整数 fee 代表了交易股票的手续费用。
 * <p>
 * 你可以无限次地完成交易，但是你每笔交易都需要付手续费。如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
 * <p>
 * 返回获得利润的最大值。
 * <p>
 * 注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费。
 * <p>
 * 示例 1：
 * <p>
 * 输入：prices = [1, 3, 2, 8, 4, 9], fee = 2 <br/>
 * 输出：8 <br/>
 * 解释：能够达到的最大利润: <br/>
 * 在此处买入 prices[0] = 1 <br/>
 * 在此处卖出 prices[3] = 8 <br/>
 * 在此处买入 prices[4] = 4 <br/>
 * 在此处卖出 prices[5] = 9 <br/>
 * 总利润: ((8 - 1) - 2) + ((9 - 4) - 2) = 8
 * <p>
 * 示例 2：
 * <p>
 * 输入：prices = [1,3,7,5,10,3], fee = 3 <br/>
 * 输出：6
 * <p>
 * 这个问题可以通过动态规划的方式来解决。我们定义两个状态，一个是持有股票时的最大利润，另一个是不持有股票时的最大利润。
 *
 * <ul>状态定义：
 *     <li/>cash[i]: 第i天结束时，不持有股票的最大利润。
 *     <li/>hold[i]: 第i天结束时，持有股票的最大利润。
 * </ul>
 * <ul>状态转移方程：
 *     <li/>如果第i天结束时不持有股票，那么可能是前一天也不持有，或者是前一天持有但在今天卖出了股票：<br/>
 *          cash[i]=max(cash[i−1],hold[i−1]+prices[i]−fee)
 *     <li/>如果第i天结束时持有股票，那么可能是前一天已经持有，或者是前一天不持有但在今天买入了股票： <br/>
 *          hold[i]=max(hold[i−1],cash[i−1]−prices[i])
 * </ul>
 * <ul>初始状态：
 *     <li/>cash[0] = 0：第一天不持有股票，利润为0。
 *     <li/>hold[0] = -prices[0]：第一天买入股票，利润为负的股票价格。
 * </ul>
 * 结果是cash[n-1]，即最后一天不持有股票时的最大利润。
 * <p>
 * 下面的代码中使用了空间优化的技巧，即只用两个变量（cash和hold）来存储状态，而不是用数组来存储每一天的状态。这样可以节约空间，但逻辑上依然符合动态规划的原理。 <p/>
 * Java代码中的优化：<p/>
 * Java代码中并未显示地使用数组，而是通过两个变量cash和hold在每一天更新这两个状态。这在实现上更节约内存，逻辑上与前面描述的动态规划方法是一致的。 <br/>
 * 每一次循环中，cash和hold的更新实际上就对应了上述的状态转移方程。代码中的newCash和newHold是为了确保在更新cash和hold时使用的是未被更新的值，避免互相影响。
 * <p/>
 * Copyright: Copyright (c) 2024-12-19 9:21
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StockTradingWithFee {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字对: ");
		String input = scanner.nextLine();
		int[] prices = Arrays.parseOneDimensionArray(input);
		System.out.print("请输入手续费: ");
		int fee = scanner.nextInt();
		System.out.println(maxProfit(prices, fee));
	}

	public static int maxProfit(int[] prices, int fee) {
		if (prices == null || prices.length == 0) {
			return 0;
		}

		// 初始化状态
		int n = prices.length;
		int cash = 0; // 第一天结束时不持有股票的利润
		int hold = -prices[0]; // 第一天结束时持有股票的利润

		// 动态规划遍历每一天的状态
		for (int i = 1; i < n; i++) {
			// 更新不持有股票的最大利润：可能是前一天也不持有，或者前一天持有今天卖出
			int newCash = Math.max(cash, hold + prices[i] - fee);
			// 更新持有股票的最大利润：可能是前一天已持有，或者前一天不持有今天买入
			int newHold = Math.max(hold, cash - prices[i]);
			cash = newCash;
			hold = newHold;
		}

		// 最后一天不持有股票的利润即为最大利润
		return cash;
	}
}
