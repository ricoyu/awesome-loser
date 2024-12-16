package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 买卖股票的最佳时机
 * <p/>
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。 <p/>
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。 <p/>
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。 <p/>
 * <p>
 * 示例 1：
 * <p/>
 * 输入：[7,1,5,3,6,4] <br/>
 * 输出：5 <br/> <br/>
 * 解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
 * 注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：prices = [7,6,4,3,1] <br/>
 * 输出：0 <br/>
 * 解释：在这种情况下, 没有交易完成, 所以最大利润为 0。
 * <p/>
 * 请用Java实现并给出详细的解题思路, Java代码中要加入详细的注释以解释清楚代码逻辑, 要给类取一个合理的类名
 * <p/>
 * 为了找到最大利润，我们需要关注两个主要的值：迄今为止遇到的最低价格和当前价格与之前最低价格的差价。
 * <p/>
 * 这里的关键在于，我们需要遍历数组，对于每个价格，我们计算如果在这一天卖出的话可能得到的利润（当前价格减去之前的最低价格），同时更新之前遇到的最低价格。
 * 这样，我们可以确保买入价格是在卖出价格之前。
 *
 * <ol>以下是详细的解题思路以及Java实现：
 *     <li/>初始化：我们需要两个变量，minPrice 表示迄今为止的最低价格，初始设置为Integer.MAX_VALUE作为一个非常大的数；
 *          maxProfit 表示迄今为止的最大利润，初始设置为0。
 *     <li/>遍历数组：对于数组中的每一个价格，我们首先检查是否更新minPrice。
 *     <li/>计算潜在利润：对于当前价格，计算如果今天卖出的话，利润为多少（当前价格 - minPrice）
 *     <li/>更新最大利润：如果这个潜在利润比我们之前记录的maxProfit大，那么就更新maxProfit
 *     <li/>返回结果：遍历完成后，maxProfit即为我们所能获得的最大利润
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-11-07 12:06
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StockTrading {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int j = 0; j < 2; j++) {
			System.out.print("请输入股票价格: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] prices = new int[parts.length];
			for (int i = 0; i < parts.length; i++) {
				prices[i] = Integer.parseInt(parts[i].trim());
			}
			System.out.println(maxProfit(prices));
		}
	}

	public static int maxProfit(int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}

		int minPrice = Integer.MAX_VALUE; // 迄今为止的最低价格
		int maxProfit = 0; // 迄今为止的最大利润

		for (int price : prices) {
			// 如果当前价格比之前记录的最低价格还低，更新最低价格
			if (price < minPrice) {
				minPrice = price;
			} else if ((price - minPrice) > maxProfit) {
				// 计算当前价格与最低价格之间的差价，如果这个差价大于之前的最大利润，更新最大利润
				maxProfit = (price - minPrice);
			}
		}

		return maxProfit;
	}
}
