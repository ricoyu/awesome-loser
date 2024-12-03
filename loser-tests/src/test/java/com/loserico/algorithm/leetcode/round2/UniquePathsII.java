package com.loserico.algorithm.leetcode.round2;

import java.util.Scanner;

public class UniquePathsII {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入行数: ");
		int m = scanner.nextInt();
		System.out.print("请输入列数: ");
		int n = scanner.nextInt();
		scanner.nextLine();
		int[][] obstacleGrid = new int[m][n];
		for (int i = 0; i < m; i++) {
			System.out.print("请输入第" + (i + 1) + "行数据: ");
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

		//如果起点或者终点被阻塞，则直接返回0
		if (obstacleGrid[0][0] == 1 || obstacleGrid[m - 1][n - 1] == 1) {
			return 0;
		}

		//创建dp数组
		int[][] dp = new int[m][n];

		//初始化起点
		dp[0][0] = 1;

		//初始化第一列
		for (int i = 1; i < m; i++) {
			dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i - 1][0];
		}

		//初始化第一行
		for (int i = 1; i < n; i++) {
			dp[0][i] = obstacleGrid[0][i] == 1 ? 0 : dp[0][i - 1];
		}

		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				dp[i][j] = obstacleGrid[i][j] == 1 ? 0 : dp[i - 1][j] + dp[i][j - 1];
			}
		}

		return dp[m-1][n-1];
	}
}
