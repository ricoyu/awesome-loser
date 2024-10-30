package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 题目描述
 * <p/>
 * 小明每周上班都会拿到自己的工作清单，工作清单内包含n项工作，每项工作都有对应的耗时时间(单位h)和报酬，工作的总报酬为所有已完成工作的报酬之和，那么请你帮小明安排一下工作，保证小明在指定的工作时间内工作收入最大化。
 * <p/>
 * 输入描述: <p/>
 * 输入的第一行为两个正整数T，n。
 * <p/>
 * T代表工作时长(单位h，0<T<1000000)， n代表工作数量(1<n≤3000)。
 * <p/>
 * 接下来是n行，每行包含两个整数t，w。
 * <p/>
 * t代表该工作消耗的时长(单位h，t>0)，w代表该项工作的报酬。
 * <p/>
 * 输出描述: <p/>
 * 输出小明指定工作时长内工作可获得的最大报酬。
 *
 * 用例1
 * <ul>输入:
 *     <li/>40 3
 *     <li/>20 10
 *     <li/>20 20
 *     <li/>20 5
 * </ul>
 *
 * 输出: 30
 * <p/>
 * Copyright: Copyright (c) 2024-09-18 9:36
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MaxEarnings {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入工作时长: ");
		int t = scanner.nextInt();
		System.out.print("请输入工作数量: ");
		int n = scanner.nextInt();
		scanner.nextLine();

		// 创建工作时长和报酬的数组
		int[] times = new int[n];
		int[] rewards = new int[n];

		// 读入每项工作的耗时和报酬
		for (int i = 0; i < n; i++) {
			System.out.print("请输入第"+(i+1)+"项工作的时长和报酬: ");
			String input = scanner.nextLine();
			String[] parts = input.trim().split(" ");
			times[i] = Integer.parseInt(parts[0].trim());
			rewards[i] = Integer.parseInt(parts[1].trim());
		}

		// 调用函数计算最大报酬
	}

	/**
	 * 使用动态规划计算在给定时间内能获得的最大报酬
	 * @param t 总工作时间
	 * @param times 每项工作所需时间数组
	 * @param rewards 每项工作报酬数组
	 * @return 可获得的最大报酬
	 */
	public static int getMaxReward(int t, int[] times, int[] rewards) {
		// dp[j] 表示在时间 j 内能获得的最大报酬
		int[] dp = new int[t + 1];

		// 遍历每项工作
		for (int i = 0; i < times.length; i++) {
			int time = times[i];
			int reward = rewards[i];

			// 倒序遍历，确保每项工作只被计算一次
			for (int j = t; j >=time ; j--) {
				// 如果做这项工作，更新能获得的最大报酬
				dp[j] = Math.max(dp[j], dp[j - time] + reward);
			}
		}

		// 最大报酬存储在 dp[T]
		return dp[t];
	}
}
