package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 验证回文串
 * <p/>
 * 如果在将所有大写字符转换为小写字符、并移除所有非字母数字字符之后，短语正着读和反着读都一样。则可以认为该短语是一个 回文串 。
 * <p/>
 * 字母和数字都属于字母数字字符。
 * <p/>
 * 给你一个字符串 s，如果它是 回文串 ，返回 true ；否则，返回 false 。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入: s = "A man, a plan, a canal: Panama" <br/>
 * 输出：true <br/>
 * 解释："amanaplanacanalpanama" 是回文串。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：s = "race a car" <br/>
 * 输出：false <br/>
 * 解释："raceacar" 不是回文串。
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：s = " " <br/>
 * 输出：true <br/>
 * 解释：在移除非字母数字字符之后，s 是一个空字符串 "" 。 <br/>
 * 由于空字符串正着反着读都一样，所以是回文串。
 *
 * <ul>步骤解析：
 *     <li/>转换和清理：将字符串中的所有大写字符转换为小写，并移除所有非字母数字字符。
 *     <li/>回文判断：判断清理后的字符串是否从前往后读与从后往前读是相同的。
 * </ul>
 * <ul>核心算法：
 *     <li/>清理字符串：遍历原字符串的每个字符，检查其是否为字母或数字（利用Character.isLetterOrDigit()方法），并将大写转换为小写（利用Character.toLowerCase()）。
 *     <li/>判断回文：使用双指针技术，一个指针从字符串的开始向后移动，另一个从字符串的末尾向前移动，逐个比较对应位置的字符是否相等。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2025-01-06 9:52
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PalindromeValidator {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入s: ");
			String s = scanner.nextLine();
			System.out.println(isPalindrome(s));
		}
	}

	public static boolean isPalindrome(String s) {
		// 构建一个StringBuilder存储清理后的字符串
		StringBuilder cleanded = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				cleanded.append(Character.toLowerCase(c));
			}
		}

		s = cleanded.toString();
		int begin = 0;
		int end = s.length() - 1;
		while (begin < end) {
			if (s.charAt(begin) == s.charAt(end)) {
				begin++;
				end--;
			} else {
				return false;
			}
		}
		return true;
	}
}
