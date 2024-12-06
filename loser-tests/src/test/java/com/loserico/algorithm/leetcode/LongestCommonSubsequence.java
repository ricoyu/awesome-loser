package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 最长公共子序列
 * <p/>
 * 给定两个字符串 text1 和 text2，返回这两个字符串的最长 公共子序列 的长度。如果不存在 公共子序列 ，返回 0 。
 * <p/>
 * 一个字符串的 子序列 是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。
 * <p/>
 * 例如，"ace" 是 "abcde" 的子序列，但 "aec" 不是 "abcde" 的子序列。
 * <p/>
 * 两个字符串的 公共子序列 是这两个字符串所共同拥有的子序列。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：text1 = "abcde", text2 = "ace" <br/>
 * 输出：3 <br/>
 * 解释：最长公共子序列是 "ace" ，它的长度为 3 。 <br/>
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：text1 = "abc", text2 = "abc" <br/>
 * 输出：3 <br/>
 * 解释：最长公共子序列是 "abc" ，它的长度为 3 。 <br/>
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：text1 = "abc", text2 = "def" <br/>
 * 输出：0 <br/>
 * 解释：两个字符串没有公共子序列，返回 0 。 <br/>
 * <p/>
 * 对于最长公共子序列问题，我们可以创建一个二维数组 dp，其中 dp[i][j] 表示 text1 中前 i 个字符和 text2 中前 j 个字符的最长公共子序列的长度。
 *
 * <ul>状态转移方程
 *     <li/>当 text1[i - 1] == text2[j - 1] 时，说明两个字符串的第 i 和第 j 个字符匹配，因此 dp[i][j] = dp[i-1][j-1] + 1。
 *     <li/>当 text1[i - 1] != text2[j - 1] 时，dp[i][j] 应该取 dp[i-1][j] 和 dp[i][j-1] 中的较大值，因为最长子序列可能是由 text1 去掉最后一个字符得到，
 *          或者是 text2 去掉最后一个字符得到。
 * </ul>
 * 初始条件: 当 i == 0 或 j == 0 时，dp[i][j] = 0，因为任意字符串与空字符串的最长公共子序列长度为 0。
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-12-06 8:25
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestCommonSubsequence {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入第一个字符串: ");
		String text1 = scanner.nextLine();
		System.out.print("请输入第二个字符串: ");
		String text2 = scanner.nextLine();
		System.out.println(longestCommonSubsequence(text1, text2));
	}
	public static int longestCommonSubsequence(String text1, String text2) {
		int m = text1.length();
		int n = text2.length();

		// dp数组，其中dp[i][j]表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
		int[][] dp = new int[m + 1][n + 1];

		// 填充dp数组
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
					// 字符匹配，当前位置的值为左上角值加一
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					// 字符不匹配，取左边或上边的较大值
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}

		return dp[m][n];
	}
}
