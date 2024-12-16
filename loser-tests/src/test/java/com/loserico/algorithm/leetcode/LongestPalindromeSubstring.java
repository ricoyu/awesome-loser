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
 *     <li/>中心扩展法：我们可以利用回文字符串的特性，任何回文字符串都有一个中心，我们可以从中心向两边扩展来寻找回文。
 *          对于一个字符串的每个字符（以及字符之间的位置）都可以作为回文的中心，分为两种情况：
 *          <ul>
 *              <li/>单字符中心（奇数长度的回文，例如 "aba"）
 *              <li/>双字符中心（偶数长度的回文，例如 "bb"）
 *          </ul>
 *     <li/>实现步骤：
 *          <ul>
 *              <li/>遍历字符串中的每个字符，尝试将其作为中心，扩展查找最长回文。
 *              <li/>记录下当前找到的最长回文字符串。
 *          </ul>
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-10-28 9:05
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestPalindromeSubstring {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入原始字符串: ");
		String s = scanner.nextLine();
		String result = longestPalindrome(s);
		System.out.println(result);
	}

	public static String longestPalindrome(String s) {
		// 如果输入字符串为空或长度小于1，返回空字符串
		if (s== null || s.length()==0) {
			return "";
		}

		int start = 0; // 记录最长回文子串的起始位置
		int end = 0; // 记录最长回文子串的结束位置

		// 遍历字符串，尝试以每个字符及字符之间的位置为中心扩展
		for (int i = 0; i < s.length(); i++) {
			/*
			 * 以单字符为中心扩展（处理奇数长度回文）
			 * 在奇数长度的回文中，回文的中心是一个字符。例如，在字符串 "aba" 中，'b' 是中心字符。
			 * 当调用 expandAroundCenter(s, i, i) 时，left 和 right 都从索引 i 开始，这样可以确保我们从该字符开始扩展，检查左右字符是否相等。
			 * 例如，对于字符串 "aba"，当 i = 1（指向 'b'）时，扩展会检查 'a' 和 'a' 是否相等，从而找到回文。
			 */
			int len1 = expandAroundCenter(s, i, i);
			/*
			 * 以双字符为中心扩展（处理偶数长度回文）
			 * 在偶数长度的回文中，回文的中心是两个相邻的字符。例如，在字符串 "cc" 中，'c' 和 'c' 之间的间隙是中心。
			 * 当调用 expandAroundCenter(s, i, i + 1) 时，left 从 i 开始，right 从 i + 1 开始，这样可以确保我们从这两个字符开始扩展，检查它们是否相等。
			 * 例如，对于字符串 "cbbd"，当 i = 1（指向第一个 'b'）时，扩展会检查第二个 'b' 和 'b' 是否相等，从而找到回文。
			 */
			int len2 = expandAroundCenter(s, i, i+1);
			// 找到当前最大的回文长度
			int len = Math.max(len1, len2);

			// 如果当前回文长度大于之前记录的长度，更新起始和结束位置
			if (len > end-start) {
				/*
				 * len 是当前回文的长度。
				 * 如果回文是奇数长度（例如长度为3），则 len 为3，(len - 1) / 2 计算结果为1。因此，start 的新值是 i - 1，即中心字符左边的字符位置。
				 * 如果回文是偶数长度（例如长度为4），则 len 为4，(len - 1) / 2 计算结果为1。因此，start 的新值是 i - 1，这个值指向了回文的左边界。
				 */
				start = i-(len-1)/2;// 更新起始位置
				end = i+len/2; // 更新结束位置
			}
		}

		return s.substring(start, end+1);
	}

	/**
	 * 辅助函数：从中心向外扩展，寻找最长回文长度
	 * @param s
	 * @param left
	 * @param right
	 * @return
	 */
	public static int expandAroundCenter(String s, int left, int right) {
		while (left>=0 && right<s.length() && s.charAt(left) == s.charAt(right)) {
			left--;// 向左扩展
			right++;// 向右扩展
		}
		return right-left -1; // 返回当前回文长度
	}
}
