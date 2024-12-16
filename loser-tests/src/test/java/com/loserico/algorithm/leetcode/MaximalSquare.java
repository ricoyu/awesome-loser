package com.loserico.algorithm.leetcode;

/**
 * 最大正方形
 * <p/>
 * 在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。
 * <p/>
 * 示例 1：
 *  <p/>
 * 输入：matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]] <br/>
 * 输出：4
 * <p/>
 * 示例 2：
 *  <p/>
 * 输入：matrix = [["0","1"],["1","0"]] <br/>
 * 输出：1
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：matrix = [["0"]] <br/>
 * 输出：0
 * <p/>
 * 请用Java实现并给出详细的解题思路, Java代码中要加入详细的注释以解释清楚代码逻辑, 要给类取一个合理的类名
 * <p/>
 * 解题思路: <br/>
 * 这个问题可以通过动态规划来解决。思路是建立一个二维的动态规划数组 dp，其中 dp[i][j] 表示以 (i, j) 为右下角的只包含 '1' 的最大正方形的边长。
 * 通过填充这个 dp 数组，可以找出所有可能的最大正方形，并计算出最大的面积。
 *  <p/>
 * 动态规划状态转移方程: <br/>
 * 如果 matrix[i][j] 是 '1'，那么 dp[i][j] 的值将基于它的左边、上边和左上角的 dp 值。具体来说： dp[i][j]=min(min(dp[i−1][j],dp[i][j−1]),dp[i−1][j−1])+1
 * <p/>
 * 这个方程说明了，只有当一个单元格的左边、上边和左上角都是由至少一定长度的正方形构成时，这个单元格才能作为一个更大正方形的一部分。
 * <p/>
 * Copyright: Copyright (c) 2024-10-27 15:10
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MaximalSquare {

	public static int maximalSquare(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}

		int rows = matrix.length, cols = matrix[0].length;
		int[][] dp = new int[rows][cols];
		int maxSide = 0;

		// 遍历每一个单元格，计算dp值
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (matrix[i][j] == '1') {
					/*
					 * if (i == 0 || j == 0)：这个条件判断当前元素是否在矩阵的边界（第一行或第一列）
					 * dp[i][j] = 1：如果当前元素在边界上，且该元素的值为1，那么以它为右下角的最大正方形边长就是 1。
					 */
					if (i == 0 || j == 0) {
						dp[i][j] = 1; // 在边界上，最大正方形就是它自己
					} else {
						dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1])+1;
					}

					// 更新最大边长
					maxSide = Math.max(maxSide, dp[i][j]);
				}
			}
		}

		return maxSide * maxSide;
	}
}
