package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 让字符串成为回文串的最少插入次数
 * <p/>
 * 给你一个字符串 s ，每一次操作你都可以在字符串的任意位置插入任意字符。
 * <p/>
 * 请你返回让 s 成为回文串的 最少操作次数 。
 * <p/>
 * 「回文串」是正读和反读都相同的字符串。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：s = "zzazz" <br/>
 * 输出：0 <br/>
 * 解释：字符串 "zzazz" 已经是回文串了，所以不需要做任何插入操作。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：s = "mbadm" <br/>
 * 输出：2 <br/>
 * 解释：字符串可变为 "mbdadbm" 或者 "mdbabdm" 。
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：s = "leetcode" <br/>
 * 输出：5 <br/>
 * 解释：插入 5 个字符后字符串变为 "leetcodocteel" 。
 * <p/>
 * 我们可以将问题转化为「找到字符串 s 的最长回文子序列的长度」，因为：
 * <p/>
 * 如果我们知道了 s 的最长回文子序列的长度L，那么 s 中剩余的字符（即s.length()−L）就是需要通过插入来完成的最少操作次数。
 *
 * <ul>要计算最长回文子序列，可以使用动态规划：
 *     <li/>定义状态：设dp[i][j] 表示字符串s[i:j]（从索引 i 到 j 的子串）的最长回文子序列长度。
 *     <li/>转移方程：如果s[i]==s[j]，那么dp[i][j]=dp[i+1][j−1]+2；
 *     <li/>边界条件：当i==j，即单个字符时，dp[i][i]=1
 * </ul>
 *
 * 通过动态规划表dp，可以得到最长回文子序列长度dp[0][n−1]（n 是字符串长度）。最终，最少插入次数为s.length()−dp[0][n−1]。
 * <p/>
 * Copyright: Copyright (c) 2024-12-10 9:57
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MinInsertionsForPalindromes {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入一个字符串: ");
		String input = scanner.nextLine();
		System.out.println(minInsertions(input));
	}

	public static int minInsertions(String s) {
		int n = s.length();
		// dp[i][j] 表示 s[i:j] 的最长回文子序列长度
		int[][] dp = new int[n][n];

		// 初始化边界情况：单个字符的子串本身是回文，长度为 1
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}

		// 填充 DP 表，注意 i 是从后向前遍历，j 是从 i+1 开始
		//i是左边界, j是右边界
		for(int i = n-1; i >= 0; i--) {
		  for(int j = i+1; j <n; j++) {
			  // 两端字符相等，最长回文子序列长度 +2
			  if (s.charAt(i) == s.charAt(j)) {
				  dp[i][j] = dp[i+1][j-1]+2;
			  }else {
				// 两端字符不相等，取去掉左端或右端后的最大长度
				dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
			  }
		  }
		}

		// 最少插入次数 = 原字符串长度 - 最长回文子序列长度
		return n - dp[0][n-1];
	}
}
