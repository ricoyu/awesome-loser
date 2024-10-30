package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1。
 * <p/>
 * 你可以认为每种硬币的数量是无限的。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：coins = [1, 2, 5], amount = 11 <br/>
 * 输出：3  <br/>
 * 解释：11 = 5 + 5 + 1
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：coins = [2], amount = 3 <br/>
 * 输出：-1 <br/>
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：coins = [1], amount = 0 <br/>
 * 输出：0 <br/>
 * <p/>
 * 示例 4：
 * <p/>
 * 输入：coins = [1], amount = 1 <br/>
 * 输出：1
 * <p/>
 * 示例 5：
 * <p/>
 * 输入：coins = [1], amount = 2 <br/>
 * 输出：2
 * <p/>
 * Copyright: Copyright (c) 2024-09-18 20:05
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CoinChange {

	public static void main(String[] args) {
		System.out.print("请输入总金额: ");
		Scanner scanner = new Scanner(System.in);
		int totalAmount = scanner.nextInt();
		scanner.nextLine();
		System.out.print("请输入硬币面额: ");
		String input = scanner.nextLine();
		String[] parts = input.trim().split(" ");
		int[] coins = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			coins[i] = Integer.parseInt(parts[i].trim());
		}
		System.out.println(coinChange2(coins, totalAmount));
	}

	public static int coinChange(int[] coins, int amount) {
		// 动态规划数组，dp[i] 表示组成金额 i 所需的最少硬币数
		int[] dp = new int[amount + 1];
		// 初始化 dp 数组，默认值为 amount + 1，这个值大于任何可能的硬币数，表示无解状态
		for (int i = 1; i <= amount; i++) {
			dp[i] = amount + 1;
		}

		// base case：0元需要0个硬币
		dp[0] = 0;

		// 外循环遍历所有金额，从1到amount
		for (int i = 1; i <= amount; i++) {
			// 内循环遍历所有硬币面额
			for (int coin : coins) {
				if (i >= coin) {// 只有当硬币面额不大于当前金额时才进行处理
					// 状态转移方程：尝试使用这枚硬币，看是否能减少所需的硬币数量
					dp[i] = Math.min(dp[i], dp[i - coin] + 1);
				}
			}
		}

		// 检查 dp[amount] 的值，如果仍然是初始值，则说明没有有效解
		return dp[amount] > amount ? -1 : dp[amount];
	}

	public static int coinChange2(int[] coins, int amount) {
		if (amount == 0) {
			return 0;
		}
		if (amount < 0) {
			return -1;
		}

		int result = Integer.MAX_VALUE;
		for (int coin : coins) {
			//计算子问题的结果
			int subProblem = coinChange2(coins, amount - coin);
			if (subProblem == -1) {
				continue;
			} else {
				result = Math.min(result, subProblem + 1);
			}
		}

		return result == Integer.MAX_VALUE ? -1 : result;
	}

}
