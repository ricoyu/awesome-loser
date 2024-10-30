package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 使用最小花费爬楼梯 <p/>
 *
 * 给你一个整数数组 cost ，其中 cost[i] 是从楼梯第 i 个台阶向上爬需要支付的费用。一旦你支付此费用，即可选择向上爬一个或者两个台阶。
 * <p/>
 * 你可以选择从下标为 0 或下标为 1 的台阶开始爬楼梯。
 * <p/>
 * 请你计算并返回达到楼梯顶部的最低花费。
 * <p/>
 * 示例 1：
 * <br/>
 * 输入：cost = [10,15,20] <br/>
 * 输出：15 <br/>
 * 解释：最低花费是从 cost[1] 开始，然后走两步即可到阶梯顶，一共花费 15 。
 * <p/>
 * 示例 2：
 * <br/>
 * 输入：cost = [1,100,1,1,1,100,1,1,100,1] <br/>
 * 输出：6 <br/>
 * 解释：最低花费方式是从 cost[0] 开始，逐个经过那些 1 ，跳过 cost[3] ，一共花费 6 。
 * <p/>
 *
 * 你将从下标为 0 的台阶开始。
 *  <ul>
 *     <li/>- 支付 1 ，向上爬两个台阶，到达下标为 2 的台阶。
 *     <li/>- 支付 1 ，向上爬两个台阶，到达下标为 4 的台阶。
 *     <li/>- 支付 1 ，向上爬两个台阶，到达下标为 6 的台阶。
 *     <li/>- 支付 1 ，向上爬一个台阶，到达下标为 7 的台阶。
 *     <li/>- 支付 1 ，向上爬两个台阶，到达下标为 9 的台阶。
 *     <li/>- 支付 1 ，向上爬一个台阶，到达楼梯顶部。
 * </ul>
 * 总花费为 6 。
 * <p/>
 * 这个问题是一个典型的动态规划问题，其中我们需要找到一个达到楼梯顶端的最小花费路径。核心思想是利用一个数组来存储到达每个台阶所需的最小花费。
 * <p/>
 * 动态规划的定义 <br/>
 * 定义一个数组 dp，其中 dp[i] 表示到达第 i 个台阶的最小花费。
 * <p/>
 * 状态转移方程 <br/>
 * 为了到达第 i 个台阶，你可以从第 i-1 个台阶上来，或者从第 i-2 个台阶上来。因此，到达第 i 个台阶的花费可以通过以下方式得到： <br/>
 * 从第 i-1 台阶上来的总花费：dp[i-1] + cost[i-1] <br/>
 * 从第 i-2 台阶上来的总花费：dp[i-2] + cost[i-2]
 * <p/>
 * 我们取这两个值中的最小值，即为 dp[i] 的值。
 * <p/>
 * 初始条件 <br/>
 * 从台阶 0 或 1 开始，无需花费，因此 dp[0] = 0 和 dp[1] = 0。
 * <p/>
 * 计算目标 <br/>
 * 我们需要计算 dp[n] 的值，其中 n 是楼梯顶的下标。这里的 n 应当是 cost 数组长度加1（因为顶部是从最后一个台阶上去的）。
 * <p/>
 * Copyright: Copyright (c) 2024-10-23 10:11
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MinCostClimbingStairs {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入第"+(i+1)+"个cost数组: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] costs = new int[parts.length];
			for (int j = 0; j < costs.length; j++) {
				costs[j] = Integer.parseInt(parts[j].trim());
			}
			System.out.println(minCostClimbingStairs(costs));
		}
	}

	public static int minCostClimbingStairs(int[] cost) {
		int n = cost.length;
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return cost[0];
		}
		// dp 数组用于存储到达每个台阶的最小花费
		int[] dp = new int[n+1];

		dp[0] = 0; //从台阶 0 开始，初始花费就是 cost[0]。
		dp[1] = 0; //从台阶 1 开始，初始花费就是 cost[1]。

		// 从第2个台阶开始计算到达每个台阶的最小花费
		for(int i = 2; i <= n; i++) {
		  dp[i] = Math.min(dp[i-1]+cost[i-1], dp[i-2]+cost[i-2]);
		}

		return dp[n];
	}
}
