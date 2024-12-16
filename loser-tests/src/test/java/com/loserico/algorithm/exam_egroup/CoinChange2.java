package com.loserico.algorithm.exam_egroup;

import java.util.Arrays;
import java.util.Scanner;

public class CoinChange2 {

	private static int[] memo;

	public static void main(String[] args) {
		for (int j = 0; j < 3; j++) {
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
			System.out.println(coinChange(coins, totalAmount));
		}
	}

	public static int coinChange(int[] coins, int amount) {
		memo = new int[amount + 1];
		//memo全部初始化为特殊值
		Arrays.fill(memo, amount+1);
		return dp(coins, amount);
	}

	private static int dp(int[] coins, int amount) {
		if (amount ==0) return 0;
		if (amount <0) return -1;

		//查备忘录, 防止重复计算
		if (memo[amount] != (amount+1)) return memo[amount];

		int result = Integer.MAX_VALUE;
		for (int coin : coins) {
			//计算子问题的结果
			int suProblem = dp(coins, amount - coin);
			//子问题无解则绕过
			if (suProblem == -1) continue;
			//在子问题中选择最优解, 然后加1
			result = Math.min(result, suProblem + 1);
		}
		//把结果存入备忘录
		memo[amount] = (result == Integer.MAX_VALUE) ? -1 : result;
		return memo[amount];
	}
}
