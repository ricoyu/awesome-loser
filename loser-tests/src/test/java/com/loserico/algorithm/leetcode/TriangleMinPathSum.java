package com.loserico.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 三角形最小路径和
 * <p/>
 * 给定一个三角形 triangle ，找出自顶向下的最小路径和。
 * <p/>
 * 每一步只能移动到下一行中相邻的结点上。相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。也就是说，如果正位于当前行的下标 i ，那么下一步可以移动到下一行的下标 i 或 i +
 * 1 。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：triangle = [[2],[3,4],[6,5,7],[4,1,8,3]] <br/>
 * 输出：11 <br/>
 * 解释：如下面简图所示： <br/>
 * <pre> {@code
 *    2
 *   3 4
 *  6 5 7
 * 4 1 8 3
 * }</pre>
 * 自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。
 * <p/>
 * 示例 2：
 * <br/>
 * 输入：triangle = [[-10]] <br/>
 * 输出：-10
 * <p/>
 *
 * <ol>解题思路：
 *     <li/>初始化状态：我们用一个二维数组 dp 来记录到达每个位置的最小路径和。dp[i][j] 表示到达第 i 行第 j 列的最小路径和。
 *     <li/>状态转移：从第二行开始，每个位置的最小路径和可以从上一行的相邻位置转移得到： <br/>
 *          dp[i][j] = min(dp[i-1][j-1], dp[i-1][j]) + triangle[i][j]，其中 j-1 位置是左上方的位置，j 位置是正上方的位置（注意边界条件）。
 *     <li/>边界条件处理：每一行的开始和结束位置需要特殊处理，因为开始位置没有左上方的元素，结束位置没有正上方的元素。
 *     <li/>计算结果：最后一行中的最小值即为自顶向下的最小路径和。
 * </ol>
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-10-25 18:45
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
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
		// dp 数组，大小与三角形的行数相同
		int[][] dp = new int[n][n];
		// 初始化顶点
		dp[0][0] = triangle.get(0).get(0);

		// 填充 dp 数组
		for (int i = 1; i < n; i++) {
			// 第一列
			dp[i][0] = dp[i - 1][0] + triangle.get(i).get(0);
			// 中间列
			for (int j = 1; j < i; j++) {
				dp[i][j] = Math.min(dp[i - 1][j - 1], dp[i - 1][j]) + triangle.get(i).get(j);
			}
			dp[i][i] = dp[i - 1][i - 1] + triangle.get(i).get(i); // 每行的最后一个元素
		}

		// 在最后一行中找到最小值
		int minPath = dp[n - 1][0];
		for (int j = 1; j < n; j++) {
			minPath = Math.min(minPath, dp[n - 1][j]);
		}

		return minPath;
	}
}
