package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 打家劫舍
 * <p/>
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * <p/>
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
 * <p/>
 * 示例 1：
 * <br/>
 * 输入：[1,2,3,1] <br/>
 * 输出：4 <p/>
 * 解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。 <br/>
 * 偷窃到的最高金额 = 1 + 3 = 4 。
 * <p/>
 * <p>
 * 示例 2：
 * <br/>
 * 输入：[2,7,9,3,1] <br/>
 * 输出：12 <p/>
 * 解释：偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。 <br/>
 * 偷窃到的最高金额 = 2 + 9 + 1 = 12 。
 * <p/>
 * 这个问题是一个经典的动态规划问题，可以通过定义一个状态数组来解决。具体的思路是定义一个数组 dp，其中 dp[i] 表示到第 i 个房屋时不触发警报所能偷窃到的最大金额。
 * <p/>
 * 动态规划方程 <p/>
 * 对于每个房屋 i，有两种选择：
 * <ol>
 *     <li/>偷窃当前房屋：那么不能偷窃前一个房屋 i-1，所以偷窃总金额为 dp[i-2] + nums[i]（nums[i] 是当前房屋的金额）。
 *     <li/>不偷窃当前房屋：那么总金额为 dp[i-1]。
 * </ol>
 * <p>
 * 因此，状态转移方程为： dp[i]=max(dp[i−1],dp[i−2]+nums[i])
 * <p/>
 * 初始化条件
 * <ul>
 *     <li/>dp[0] = nums[0]，因为只有一个房屋时只能偷这一个。
 *     <li/>如果房屋数量大于1，则 dp[1] = max(nums[0], nums[1])。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-10-24 9:27
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class HouseRobber {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入第" + (i + 1) + "个数组: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] nums = new int[parts.length];
			for (int j = 0; j < nums.length; j++) {
				nums[j] = Integer.parseInt(parts[j].trim());
			}
			System.out.println(rob(nums));
		}
	}

	public static int rob(int[] nums) {
		if (nums == null || nums.length == 0) {
			// 如果没有房屋，则偷窃金额为0
			return 0;
		}
		if (nums.length == 1) {
			// 只有一个房屋，只能偷窃这一个
			return nums[0];
		}

		// 初始化动态规划数组
		int[] dp = new int[nums.length];
		dp[0] = nums[0];
		dp[1] = Math.max(nums[0], nums[1]);

		// 根据状态转移方程计算dp数组
		for (int i = 2; i < nums.length; i++) {
			dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
		}

		// 最后一个元素的值即为可以偷窃到的最大金额
		return dp[nums.length - 1];
	}
}
