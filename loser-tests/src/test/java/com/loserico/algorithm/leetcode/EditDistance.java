package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 编辑距离
 * <p/>
 * 给你两个单词 word1 和 word2， 请返回将 word1 转换成 word2 所使用的最少操作数  。
 * <p/>
 * 你可以对一个单词进行如下三种操作：
 * <p/>
 * 插入一个字符 <br/>
 * 删除一个字符 <br/>
 * 替换一个字符 <br/>
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：word1 = "horse", word2 = "ros" <br/>
 * 输出：3 <br/>
 * 解释： <br/>
 * horse -> rorse (将 'h' 替换为 'r') <br/>
 * rorse -> rose (删除 'r') <br/>
 * rose -> ros (删除 'e') <br/>
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：word1 = "intention", word2 = "execution" <br/>
 * 输出：5 <br/>
 * 解释： <br/>
 * intention -> inention (删除 't') <br/>
 * inention -> enention (将 'i' 替换为 'e') <br/>
 * enention -> exention (将 'n' 替换为 'x') <br/>
 * exention -> exection (将 'n' 替换为 'c') <br/>
 * exection -> execution (插入 'u') <br/>
 * <p/>
 * 这个问题通常被称为“编辑距离”问题，它是一个经典的动态规划问题。我们定义一个二维数组 dp，
 * 其中 dp[i][j] 表示将 word1 的前 i 个字符转换成 word2 的前 j 个字符所需要的最小操作数。
 * <p/>
 * 解题思路:
 * <ul>
 *     <li/>当 word1 为空时，将 word2 的前 j 个字符插入到 word1，所以 dp[0][j] = j。
 *     <li/>当 word2 为空时，需要从 word1 的前 i 个字符删除所有字符，所以 dp[i][0] = i。
 * </ul>
 * <p>
 * 对于每个 i 和 j，我们有三种可能的操作：
 * <ul>
 *     <li/>插入：在 word1 中插入 word2[j-1]，这样 word1 的前 i 个字符加上这个新字符应该和 word2 的前 j 个字符相同。因此，dp[i][j] = dp[i][j-1] + 1。
 *     <li/>删除：从 word1 中删除字符 word1[i-1]，使得 word1 的前 i-1 个字符和 word2 的前 j 个字符相同。因此，dp[i][j] = dp[i-1][j] + 1。
 *     <li/>替换：如果最后一个字符不同，替换 word1[i-1] 为 word2[j-1]，这样 word1 的前 i 个字符就和 word2 的前 j 个字符相同。因此，dp[i][j] = dp[i-1][j-1]
 *     + 1。
 * </ul>
 * 如果最后一个字符已经相同，则无需替换，dp[i][j] = dp[i-1][j-1]。
 * Copyright: Copyright (c) 2024-10-30 8:30
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class EditDistance {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入字符串word1: ");
		String word1 = scanner.nextLine();
		System.out.print("请输入字符串word2: ");
		String word2 = scanner.nextLine();
		System.out.println(minDistance(word1, word2));
	}

	public static int minDistance(String word1, String word2) {
		int m = word1.length();
		int n = word2.length();

		// dp[i][j] 表示将 word1 的前 i 个字符转换成 word2 的前 j 个字符所使用的最少操作数
		int[][] dp = new int[m + 1][n + 1];

		// 初始化 dp 数组
		for (int i = 0; i <= m; i++) {
			//dp[i][0]：表示将 word1 的前 i 个字符转换成空字符串所需的操作数，显然是 i 次删除操作。
			dp[i][0] = i;// 删除所有字符, 认为word2是空串的情况
		}

		for (int j = 0; j <= n; j++) {
			//dp[0][j]：表示将空字符串转换成 word2 的前 j 个字符所需的操作数，显然是 j 次插入操作。
			dp[0][j] = j;// 插入所有字符, 认为word1是空串的情况
		}

		// 计算所有 dp 值
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					// 如果当前字符相同，则不需要操作，直接继承之前的结果
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					// 计算插入、删除、替换中的最小值
					dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) +1;
				}
			}
		}

		// 返回将整个 word1 转换为 word2 的最小操作数
		return dp[m][n];
	}
}
