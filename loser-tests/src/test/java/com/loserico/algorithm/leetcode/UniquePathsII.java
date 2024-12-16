package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 不同路径 II
 * <p/>
 * 给定一个 m x n 的整数数组 grid。一个机器人初始位于 左上角（即 grid[0][0]）。机器人尝试移动到 右下角（即 grid[m - 1][n - 1]）。机器人每次只能向下或者向右移动一步。
 * <p/>
 * 网格中的障碍物和空位置分别用 1 和 0 来表示。机器人的移动路径中不能包含 任何 有障碍物的方格。
 * <p/>
 * 返回机器人能够到达右下角的不同路径数量。
 * <p/>
 * 测试用例保证答案小于等于 2 * 109。
 * <p/>
 * 示例 1：<br/>
 * 输入：obstacleGrid = [[0,0,0],[0,1,0],[0,0,0]] <br/>
 * 输出：2 <p/>
 * 解释：3x3 网格的正中间有一个障碍物。 <br/>
 * 从左上角到右下角一共有 2 条不同的路径： <br/>
 * 1. 向右 -> 向右 -> 向下 -> 向下 <br/>
 * 2. 向下 -> 向下 -> 向右 -> 向右
 * <p/>
 * 示例 2： <br/>
 * 输入：obstacleGrid = [[0,1],[0,0]] <br/>
 * 输出：1
 * <p/>
 * 在这个问题中，我们可以用一个二维数组dp来存储从起点到网格中每个点的路径数量。 <p/>
 * <ol>步骤:
 *     <li/>初始化边界条件：如果起点或终点有障碍物，则直接返回0，因为无法开始或结束。
 *     <li/>处理边界的第一行和第一列：对于第一行和第一列，如果当前格子没有障碍物，且之前的格子有路径，则设置当前格子的路径数量为1。如果遇到障碍物，该格子及之后的格子均不可达。
 *     <li/>填充dp表：对于其他位置，如果没有障碍物，其路径数等于它上方和左方两个格子的路径数之和。如果有障碍物，路径数为0。
 *     <li/>返回结果：dp[m-1][n-1]存储从起点到终点的路径数量
 * </ol>
 * Copyright: Copyright (c) 2024-10-25 10:41
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class UniquePathsII {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入行数: ");
		int m = scanner.nextInt();
		System.out.print("请输入列数: ");
		int n = scanner.nextInt();
		scanner.nextLine();
		int[][] obstacleGrid = new int[m][n];
		for(int i = 0; i < m; i++) {
			System.out.print("请输入第"+(i+1)+"行数据: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] row = new int[parts.length];
			for (int j = 0; j < n; j++) {
				row[j] = Integer.parseInt(parts[j].trim());
			}
			obstacleGrid[i] = row;
		}

		System.out.println(uniquePathsWithObstacles(obstacleGrid));
	}

	public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
		int m = obstacleGrid.length;
		int n = obstacleGrid[0].length;

		// 如果起点或终点被阻塞，则直接返回0
		if (obstacleGrid[0][0] == 1 || obstacleGrid[m - 1][n - 1] == 1) {
			return 0;
		}

		int[][] dp = new int[m][n];
		// 初始化起点
		dp[0][0] = 1;

		// 初始化第一列，只有当之前没有障碍物时，当前位置才可达
		for (int i = 1; i < m; i++) {
			dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i - 1][0];
		}

		// 初始化第一行，同第一列的逻辑
		for (int i = 1; i < n; i++) {
			dp[0][i] = obstacleGrid[0][i] == 1 ? 0 : dp[0][i - 1];
		}

		// 计算dp数组中其他位置的路径数量
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (obstacleGrid[i][j] == 1) {
					dp[i][j] = 0; // 障碍物位置的路径数为0
				} else {
					dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
				}
			}
		}

		return dp[m - 1][n - 1];
	}
}
