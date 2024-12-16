package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 不同的子序列
 * <p/>
 * 给你两个字符串 s 和 t ，统计并返回在 s 的 子序列 中 t 出现的个数，结果需要对 1^19 + 7 取模。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：s = "rabbbit", t = "rabbit" <br/>
 * 输出：3 <br/>
 * 解释：
 * 如下所示, 有 3 种可以从 s 中得到 "rabbit" 的方案。 <br/>
 * rabbbit <br/>
 * rabbbit <br/>
 * rabbbit <br/>
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：s = "babgbag", t = "bag" <br/>
 * 输出：5 <br/>
 * 解释： <br/>
 * 如下所示, 有 5 种可以从 s 中得到 "bag" 的方案。  <br/>
 * babgbag <br/>
 * babgbag <br/>
 * babgbag <br/>
 * babgbag <br/>
 * babgbag <br/>
 * <p/>
 * 请用Java实现并给出详细的解题思路, Java代码中要加入详细的注释以解释清楚代码逻辑, 要给类取一个合理的类名
 * <p/>
 * 定义动态规划数组：
 * <ul>
 *     <li/>创建一个二维数组 dp，其中 dp[i][j] 表示 s 的前 i 个字符中子序列 t 的前 j 个字符出现的次数。
 *     <li/>边界条件：如果 t 是空串，那么 s 的任意前缀都包含空串，故对所有 i，dp[i][0] = 1。
 * </ul>
 * 状态转移方程：
 * <ul>
 *     <li/>遍历字符串 s 和 t，如果 s[i-1] == t[j-1]，说明当前字符相等，那么有两种情况
 *          不包含 s[i-1]，即 dp[i-1][j]
 *          包含 s[i-1]，即 dp[i-1][j-1]
 *          所以 dp[i][j] = dp[i-1][j] + dp[i-1][j-1]
 * </ul>
 * 如果 s[i-1] != t[j-1]，那么 dp[i][j] = dp[i-1][j]，即当前字符不匹配时只能忽略 s[i-1]。
 * Copyright: Copyright (c) 2024-11-01 9:12
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SubsequenceCounter {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.print("请输入字符串s: ");
			String s = scanner.nextLine();
			System.out.print("请输入字符串t: ");
			String t = scanner.nextLine();
			System.out.println(numDistinct(s, t));
		}
	}

	// 定义取模常量
	private static final int MOD = 1000000007;

	public static int numDistinct(String s, String t) {
		int m = s.length();
		int n = t.length();

		// 创建二维DP数组，dp[i][j]表示s的前i个字符中t的前j个字符出现的次数
		int[][] dp = new int[m + 1][n + 1];

		// 初始化：如果t是空字符串，那么s的任意前缀都包含空串，初始化为1
		for (int i = 0; i <= m; i++) {
			dp[i][0] = 1;
		}

		// 填充dp数组
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (s.charAt(i - 1) == t.charAt(j - 1)) {
					// 当前字符匹配时，两种选择：不包含s[i-1]或包含s[i-1]
					dp[i][j] = (dp[i - 1][j] + dp[i - 1][j - 1]) % MOD;
				} else {
					// 当前字符不匹配，只能忽略s[i-1]
					dp[i][j] = dp[i - 1][j];
				}
			}
		}

		return dp[m][n];
	}
}
