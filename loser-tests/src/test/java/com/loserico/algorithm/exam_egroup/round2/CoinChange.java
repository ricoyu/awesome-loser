package com.loserico.algorithm.exam_egroup.round2;

import java.util.Scanner;

/**
 * 给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额, 返回 -1。
 * <p/>
 * 你可以认为每种硬币的数量是无限的。
 *
 * <pre> {@code
 * 输入：coins = [1, 2, 5], amount = 11
 * 输出：3
 * 解释：11 = 5 + 5 + 1
 * }</pre>
 * <p/>
 * Copyright: Copyright (c) 2024-09-20 21:03
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CoinChange {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int j = 0; j < 10; j++) {
			System.out.print("请输入总金额: ");
			int amount = scanner.nextInt();
			System.out.print("请输入硬币面额: ");
			scanner.nextLine();
			String input = scanner.nextLine();
			String[] parts = input.trim().split(" ");
			int[] coins = new int[parts.length];
			for (int i = 0; i < parts.length; i++) {
				coins[i] = Integer.parseInt(parts[i].trim());
			}
			int[] memo = new int[amount+1];

			System.out.println("结果: " + coinCount2(memo, coins, amount));
		}

		scanner.close();
	}

	public static int coinCount(int[] coins, int amount) {
		if (amount == 0) {
			return 0;
		}
		if (amount < 0) {
			return -1;
		}
		int result = Integer.MAX_VALUE;
		for (int coin : coins) {
			int subResult = coinCount(coins, amount - coin);
			if (subResult == -1) continue;
			result = Math.min( subResult+1, result);
		}
		return result == Integer.MAX_VALUE ? -1 : result;
	}

	public static int coinCount2(int[] memo, int[] coins, int amount) {
		if (amount == 0) {
			return 0;
		}
		if (amount < 0) {
			return -1;
		}
		if (memo[amount] != 0) {
			return memo[amount];
		}
		int result = Integer.MAX_VALUE;
		for (int coin : coins) {
			int subResult = coinCount(coins, amount - coin);
			if (subResult == -1) continue;
			result = Math.min( subResult+1, result);
		}
		result = result == Integer.MAX_VALUE ? -1 : result;
		memo[amount] = result;
		return result;
	}
}
