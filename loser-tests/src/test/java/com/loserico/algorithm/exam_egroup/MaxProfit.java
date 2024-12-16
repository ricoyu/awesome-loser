package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 贪心的商人
 * 题目描述:
 * 商人经营一家店铺，有number种商品，由于仓库限制每件商品的最大持有数量是item[index]，每种商品的价格是item-price[item_index][day]，
 * 通过对商品的买进和卖出获取利润，请给出商人在days天内能获取的最大的利润。
 * <p/>
 * 注：同一件商品可以反复买进和卖出
 * <p/>
 * 输入描述
 * <p/>
 * 3 第一行输入商品的数量number
 * <br/>
 * 3 第二行输入商品售货天数 days
 * <br/>
 * 4 5 6 第三行输入仓库限制每件商品的最大持有数量
 * <br/>
 * 1 2 3 第一件商品每天的价格
 * <br/>
 * 4 3 2 第二件商品每天的价格
 * <br/>
 * 1 5 3 第三件商品每天的价格
 * <p/>
 * 输出描述
 * <p/>
 * 输出商人在这段时间内的最大利润
 * <p/>
 * 例如：32
 * <p>
 * 示例1
 *
 * <ul>输入:
 *     <li/>3
 *     <li/>3
 *     <li/> 4 5 6
 *     <li/> 1 2 3
 *     <li/> 4 3 2
 *     <li/> 1 5 2
 * </ul>
 * <p>
 * 输出: 32
 * <p>
 * <p>
 * 示例2
 *
 * <ul>输入
 *     <li/>1
 *     <li/>1
 *     <li/>1
 *     <li/>1
 *     <li/>1
 * </ul>
 *
 * <p>
 * 输出: 0
 * Copyright: Copyright (c) 2024-09-09 8:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MaxProfit {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入商品数量: ");
		int number = scanner.nextInt(); //第一行输入商品的数量number
		System.out.print("请输入天数: ");
		int days = scanner.nextInt(); //第二行输入商品售货天数 days
		scanner.nextLine(); //清空scanner缓存

		int[] maxItems = new int[number];
		System.out.print("输入仓库限制每件商品的最大持有数量: ");
		String input = scanner.nextLine(); //第三行输入仓库限制每件商品的最大持有数量
		String[] inputs = input.split(" ");
		if (inputs.length != number) {
			System.out.println("商品数量有误，请重新输入");
			System.exit(1);
		}
		for (int j = 0; j < number; j++) {
			maxItems[j] = Integer.parseInt(inputs[j]);
		}

		/*
		 * 第一件商品每天的价格
		 * 第二件商品每天的价格
		 */
		int[][] prices = new int[number][days];
		for (int i = 0; i < number; i++) {
			System.out.print("输入商品" + (i + 1) + "每天的价格: ");
			input = scanner.nextLine();
			inputs = input.split(" ");
			if (inputs.length != days) {
				System.out.println("商品" + (i + 1) + " 每天价格的天数有误, 请重新输入");
			}
			for (int j = 0; j < days; j++) {
				prices[i][j] = Integer.parseInt(inputs[j]);
			}
		}

		scanner.close(); //关闭扫描器

		System.out.println(maxProfit(prices, maxItems, days, number));
	}

	/**
	 * 获取最大利润
	 *
	 * @param prices   每种商品每天的价格
	 * @param maxItems 每种商品的最大持有数量
	 * @param days     天数
	 * @param number   商品数量
	 * @return
	 */
	public static int maxProfit(int[][] prices, int[] maxItems, int days, int number) {
		int profit = 0; //利润

		for (int i = 0; i < number; i++) {
			int[] pricesPerDay = prices[i]; //该商品每一天的价格
			int maxItem = maxItems[i]; // 该商品的最大持有数量
			int inventory = 0; //该商品的库存

			for (int j = 0; j < days - 1; j++) {
				// 如果明天价格高于今天，买入（直到库存满）
				if (pricesPerDay[j + 1] > pricesPerDay[j]) {
					// 可买入的最大数量
					int maxBuy = Math.min(maxItem - inventory, maxItem);
					inventory += maxBuy;
					profit -= maxBuy * pricesPerDay[j];
				} else if (pricesPerDay[j + 1] < pricesPerDay[j]) {
					// 如果明天价格低于今天，卖出（尽可能多）
					profit += inventory * pricesPerDay[j];
					inventory = 0;
				}
			}

			// 最后一天尽可能卖出所有库存
			profit += inventory * pricesPerDay[days - 1];
		}

		return profit;
	}
}
