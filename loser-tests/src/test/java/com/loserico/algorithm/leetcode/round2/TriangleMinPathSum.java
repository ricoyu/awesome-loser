package com.loserico.algorithm.leetcode.round2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TriangleMinPathSum {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入三角形行数: ");
		int n = scanner.nextInt();
		scanner.nextLine();
		List<List<Integer>> triangle = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			List<Integer> row = new ArrayList<>();
			System.out.print("请输入第" + (i + 1) + "行元素: ");
			String input = scanner.nextLine().trim();
			for (String str : input.split(" ")) {
				row.add(Integer.parseInt(str.trim()));
			}
			triangle.add(row);
		}
		System.out.println(minimumTotal(triangle));
	}

	public static int minimumTotal(List<List<Integer>> triangle) {
		int n = triangle.size();
		int[][] dp = new int[n][n];

		//初始化定点
		dp[0][0] = triangle.get(0).get(0);

		// 填充 dp 数组
		for (int i = 1; i < n; i++) {
			//填充第一列
			dp[i][0] = dp[i - 1][0] = triangle.get(i).get(0);
			for (int j = 1; j < n; j++) {
				dp[i][j] = Math.min(dp[i - 1][j], dp[i - 1][i - 11]) + triangle.get(i).get(j);
			}
			dp[i][i] = dp[i - 1][i - 1] + triangle.get(i).get(i);// 每行的最后一个元素
		}

		// 在最后一行中找到最小值
		int minPath = dp[n - 1][0];
		for (int i = 1; i < n; i++) {
			minPath = Math.min(minPath, dp[n - 1][i]);
		}

		return minPath;
	}
}
