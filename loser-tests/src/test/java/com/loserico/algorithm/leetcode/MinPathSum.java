package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 最小路径和
 * <p/>
 * 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * <p/>
 * 说明：每次只能向下或者向右移动一步。
 * <p/>
 * 示例 1：<br/>
 * 输入：grid = [[1,3,1],[1,5,1],[4,2,1]] <br/>
 * 输出：7 <br/>
 * 解释：因为路径 1→3→1→1→1 的总和最小。
 * <p/>
 * 示例 2：
 * <br/>
 * 输入：grid = [[1,2,3],[4,5,6]] <br/>
 * 输出：12
 * <p/>
 * 这个问题可以通过使用动态规划(Dynamic Programming, DP)的方法来解决。
 * 动态规划的基本思想是利用已解决的子问题的解来构建原问题的解。对于此问题，
 * 我们可以定义一个与原网格同样大小的二维数组 dp，其中 dp[i][j] 表示从左上角到达网格中 (i, j) 位置时的最小路径和。
 * <p/>
 * 动态规划状态转移方程: <br/>
 * 对于每一个格子 (i, j)，你只能从 (i-1, j) 或者 (i, j-1) 这两个位置移动过来。因此，状态转移方程可以表示为： dp[i][j]=min(dp[i−1][j],dp[i][j−1])+grid[i][j]
 * <p/>
 * <ul>边界条件:
 *     <li/>对于第一行 dp[0][j]，因为只能从左边过来，所以其值为 dp[0][j-1] + grid[0][j]。
 *     <li/>对于第一列 dp[i][0]，因为只能从上面过来，所以其值为 dp[i-1][0] + grid[i][0]。
 * </ul>
 * Copyright: Copyright (c) 2024-10-25 9:43
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MinPathSum {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入网格行数: ");
		int m = scanner.nextInt();
		System.out.print("请输入网格列数: ");
		int n = scanner.nextInt();
		scanner.nextLine();
		int[][] grid = new int[m][n];
		for(int i = 0; i < m; i++) {
		  System.out.print("请输入第"+(i+1)+" 行数据: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] data = new int[n];
			for(int j = 0; j < n; j++) {
			  data[j] = Integer.parseInt(parts[j].trim());
			}
			grid[i] = data;
		}

		System.out.println(minPathSum(grid));
	}

	public static int minPathSum(int[][] grid) {
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}

		int m = grid.length;
		int n = grid[0].length;

		int[][] dp = new int[m][n];

		// 初始化左上角的起点
		dp[0][0] = grid[0][0];

		// 初始化第一行的数据，只能从左边来
		for (int i = 1; i < n; i++) {
			dp[0][i] = dp[0][i - 1] + grid[0][i];
		}

		// 初始化第一列的数据，只能从上面来
		for (int i = 1; i < m; i++) {
			dp[i][0] = dp[i - 1][0] + grid[i][0];
		}

		// 根据状态转移方程计算dp数组的其余部分
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
			}
		}

		// 最右下角的dp值就是从左上角到右下角的最小路径和
		return dp[m - 1][n - 1];
	}
}
