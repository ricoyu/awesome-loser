package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 下降路径最小和
 * <p/>
 * 给你一个 n x n 的 方形 整数数组 matrix ，请你找出并返回通过 matrix 的下降路径 的 最小和 。
 * <p/>
 * 下降路径 可以从第一行中的任何元素开始，并从每一行中选择一个元素。在下一行选择的元素和当前行所选元素最多相隔一列（即位于正下方或者沿对角线向左或者向右的第一个元素）。
 * 具体来说，位置 (row, col) 的下一个元素应当是 (row + 1, col - 1)、(row + 1, col) 或者 (row + 1, col + 1) 。
 * <p/>
 * 示例 1：<br/>
 * 输入：matrix = [[2,1,3],[6,5,4],[7,8,9]] <br/>
 * 输出：13 <br/>
 * <p/>
 * <p>
 * 示例 2： <br/>
 * 输入：matrix = [[-19,57],[-40,-5]] <br/>
 * 输出：-59 <br/>
 *
 * <ol>解题思路:
 *     <li/>初始化状态: 我们将一个和输入相同尺寸的dp数组用于存储到当前元素为止的最小下降路径和。
 *          初始化dp数组的最后一行与输入数组的最后一行相同，因为最后一行的元素下面没有其他元素，所以它们的下降路径和就是它们自己的值。
 *     <li/>状态转移: 从倒数第二行开始向上计算每个元素的最小下降路径和。对于dp数组中的每个元素dp[i][j]，其值由下一行的三个可能位置的元素决定（注意边界条件）：
 *     <li/>dp[i+1][j-1]（左下角元素，如果存在的话）
 *     <li/>dp[i+1][j]（正下方元素）
 *     <li/>dp[i+1][j+1]（右下角元素，如果存在的话）
 *     <li/>对于每个元素，我们将它的值更新为它自己的值加上上述三个元素中的最小值。
 *     <li/>结果输出: dp数组的第一行包含了从顶部到底部的所有可能的最小下降路径和。最终答案即为这一行的最小值。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-10-27 12:08
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MinFallingPathSum {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入矩阵行列数: ");
		int n = scanner.nextInt();
		scanner.nextLine();
		int[][] matrix = new int[n][n];
		for (int i = 0; i < n; i++) {
			System.out.print("请输入第" + (i + 1) + "行元素: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] row = new int[n];
			for (int j = 0; j < n; j++) {
				row[j] = Integer.parseInt(parts[j].trim());
				matrix[i] = row;
			}
		}

		System.out.println(minFallingPathSum(matrix));
	}

	public static int minFallingPathSum(int[][] matrix) {
		int n = matrix.length;

		// 创建dp数组并初始化最后一行
		int[][] dp = new int[n][n];
		for (int i = 0; i < n; i++) {
			dp[n - 1][i] = matrix[n - 1][i];
		}

		// 从倒数第二行开始向上计算每个元素的最小下降路径和
		for (int i = n - 2; i >= 0; i--) {
			for (int j = 0; j < n; j++) {
				// 正下方元素
				int min = dp[i + 1][j];
				// 左下方元素
				if (j > 0) {
					min = Math.min(min, dp[i + 1][j - 1]);
				}
				// 右下方元素
				if (j < n - 1) {
					min = Math.min(min, dp[i + 1][j + 1]);
				}
				// 更新当前位置的最小路径和
				dp[i][j] = matrix[i][j] + min;
			}
		}

		// 在dp数组的第一行找到最小值
		int minSum = Integer.MAX_VALUE;
		for (int i = 0; i < n; i++) {
			minSum = Math.min(minSum, dp[0][i]);
		}

		return minSum;
	}
}
