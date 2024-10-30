package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 最长回文子序列
 * <p/>
 * 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
 * <p/>
 * 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：s = "bbbab" <br/>
 * 输出：4 <br/>
 * 解释：一个可能的最长回文子序列为 "bbbb" 。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：s = "cbbd" <br/>
 * 输出：2 <br/>
 * 解释：一个可能的最长回文子序列为 "bb" 。
 * <p/>
 *
 * <ul>解题思路：
 *     <li/>定义状态：使用 dp[i][j] 表示从字符串 s 的第 i 个字符到第 j 个字符这一子串的最长回文子序列的长度。
 *     <li/>状态转移方程：如果 s[i] == s[j]，那么 dp[i][j] = dp[i+1][j-1] + 2，意思是当 s[i] 和 s[j] 相等时，
 *          它们可以组成一个更长的回文子序列，因此在去掉它们后的子串 dp[i+1][j-1] 基础上再加上这两个相等字符。
 *     <li/>如果 s[i] != s[j]，那么 dp[i][j] = max(dp[i+1][j], dp[i][j-1])，意思是当 s[i] 和 s[j] 不相等时，
 *          我们只能选择去掉其中一个字符，然后取较大的那个子串的最长回文子序列长度。
 *     <li/>初始化：对于每一个单个字符，回文子序列的长度都是 1，因此 dp[i][i] = 1。
 *     <li/>
 * </ul>
 * 最终答案：我们要求的最长回文子序列就是 dp[0][n-1]，其中 n 是字符串的长度，表示从字符串的第一个字符到最后一个字符的最长回文子序列的长度。
 * Copyright: Copyright (c) 2024-10-29 9:13
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestPalindromicSubsequence {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入字符串s: ");
		String s = scanner.nextLine();
		System.out.println(longestPalindromeSubseq(s));
	}

	public static int longestPalindromeSubseq(String s) {
		int n = s.length();

		// dp[i][j] 表示字符串 s 从 i 到 j 的子串的最长回文子序列长度
		int[][] dp = new int[n][n];

		// 初始化：单个字符的最长回文子序列长度为1
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}

		// 开始填表，l表示子串的长度，从2开始逐渐增长
		for (int l = 2; l <= n; l++) {
			for (int i = 0; i <= n - l; i++) {
				// j 是子串的右边界
				int j = l + i - 1;

				// 如果子串的首尾字符相同
				if (s.charAt(i) == s.charAt(j)) {
					// 首尾字符相等，可以在中间部分的最长回文基础上 +2
					dp[i][j] = dp[i+1][j-1]+2;
				} else {
					// 首尾字符不相等，选择去掉其中一个字符，取较大的结果
					dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
				}
			}
		}

		// 返回整个字符串的最长回文子序列长度
		return dp[0][n-1];
	}
}
