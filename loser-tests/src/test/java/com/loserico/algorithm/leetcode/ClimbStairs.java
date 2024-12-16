package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 爬楼梯 <p/>
 * <p>
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。 <p/>
 * <p>
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？ <p/>
 * <p>
 * 示例 1：
 * <p/>
 * 输入：n = 2 <br/>
 * 输出：2 <p/>
 * 解释：有两种方法可以爬到楼顶。 <p/>
 * 1 阶 + 1 阶 <br/>
 * 2 阶 <p/>
 * 示例 2：
 * <br/>
 * 输入：n = 3 <br/>
 * 输出：3 <p/>
 * 解释：有三种方法可以爬到楼顶。 <p/>
 * 1 阶 + 1 阶 + 1 阶 <br/>
 * 1 阶 + 2 阶 <br/>
 * 2 阶 + 1 阶  <br/>
 * <p/>
 * Copyright: Copyright (c) 2024-10-22 8:45
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ClimbStairs {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入n: ");
		int n = scanner.nextInt();
		System.out.println(climbStairs(n));
	}

	public static int climbStairs(int n) {
		// 如果楼梯阶数小于等于1，则直接返回n（因为没有阶梯或只有一阶时的方法数就是阶梯数自身）
		if (n <= 1) {
			return 1;
		}

		// 创建动态规划数组，用于保存每个阶梯对应的爬法数量
		int[] dp = new int[n + 1];
		// 初始化前两个阶梯的爬法数量
		dp[1] = 1;
		dp[2] = 2;

		// 从第3阶开始，应用状态转移方程，计算每个阶梯的爬法数量
		for (int i = 3; i <= n; i++) {
			dp[i] = dp[i - 1] + dp[i - 2];
		}

		// 返回达到最顶层阶梯的爬法数量
		return dp[n];
	}
}
