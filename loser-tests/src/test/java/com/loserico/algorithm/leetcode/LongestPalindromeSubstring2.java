package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 最长回文子串
 * <p/>
 * 给你一个字符串 s，找到 s 中最长的 回文子串
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：s = "babad" <br/>
 * 输出："bab" <br/>
 * 解释："aba" 同样是符合题意的答案。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：s = "cbbd" <br/>
 * 输出："bb" <br/>
 * <p/>
 * 解题思路
 * <ol>
 *     <li/>回文字符串的定义：回文字符串是正着读和反着读都相同的字符串，例如 "aba" 或 "bb"。
 *     <li/>使用一个二维数组 dp，其中 dp[i][j] 表示子串 s[i..j] 是否是回文串。
 *     <li/>如果 s[i] 等于 s[j]，并且 s[i+1..j-1] 也是回文，则 s[i..j] 是回文。
 *     <li/>动态规划的状态转移方程为：dp[i][j] = (s[i] == s[j]) && (j - i < 3 || dp[i + 1][j - 1])
 *     <li/>最后遍历 dp 数组，找到最长的回文子串。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-10-28 9:05
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestPalindromeSubstring2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.print("请输入原始字符串: ");
			String s = scanner.nextLine();
			String result = longestPalindrome(s);
			System.out.println(result);
		}
	}

	public static String longestPalindrome(String s) {
		// 输入字符串为空或长度小于1
		if (s == null || s.length() < 1) {
			return "";
		}

		int n = s.length();
		boolean[][] dp = new boolean[n][n]; // dp[i][j] 表示 s[i..j] 是否为回文
		String longest = "";

		/*
		 * j 表示串的右边界
		 * i 表示左边界
		 */
		for (int j = 0; j < n; j++) {
			for (int i = 0; i <= j; i++) {
				/*
				 * s.charAt(i) == s.charAt(j) 首先，首尾字符必须相等，这是回文的必要条件
				 * j - i < 3
				 *  如果子串长度小于等于3，则只需要判断首尾字符是否相等即可，无需再考虑中间的字符。
				 *  长度为1（j - i = 0）时，显然是回文。
				 *  长度为2（j - i = 1）时，两个字符相等则是回文。
				 *  长度为3（j - i = 2）时，首尾字符相等则是回文，中间字符不影响。
				 * dp[i + 1][j - 1]: 如果子串长度大于3（j - i >= 3），我们需要检查去掉首尾字符后的子串 s[i + 1..j - 1] 是否为回文。这是递归的动态规划部分。
				 *
				 */
				if (s.charAt(i) == s.charAt(j) && ((j - i) < 3 || dp[i + 1][j - 1])) {
					dp[i][j] = true;// 更新状态
					if (j - i + 1 > longest.length()) {
						longest = s.substring(i, j + 1); // 更新最长回文子串
					}
				}
			}
		}
		return longest;
	}
}
