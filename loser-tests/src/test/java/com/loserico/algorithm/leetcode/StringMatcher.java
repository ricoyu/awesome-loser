package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 找出字符串中第一个匹配项的下标
 * <p>
 * 给你两个字符串 haystack 和 needle ，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。如果 needle 不是 haystack 的一部分，则返回  -1 。
 * <p>
 * 示例 1：
 * <p>
 * 输入：haystack = "sadbutsad", needle = "sad" <br/>
 * 输出：0 <br/>
 * 解释："sad" 在下标 0 和 6 处匹配。 <br/>
 * 第一个匹配项的下标是 0 ，所以返回 0 。 <br/>
 * <p>
 * 示例 2：
 * <p>
 * 输入：haystack = "leetcode", needle = "leeto" <br/>
 * 输出：-1 <br/>
 * 解释："leeto" 没有在 "leetcode" 中出现，所以返回 -1 。 <br/>
 * <p>
 * 暴力匹配： 使用两层循环，逐一检查 haystack 的每个子串是否与 needle 相等。这种方法简单直观，但效率较低，为 O(n * m)（n 是 haystack 长度，m 是 needle 长度）。
 * <p/>
 * Copyright: Copyright (c) 2024-12-31 9:10
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StringMatcher {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入主字符串: ");
		String haystack = scanner.nextLine();
		System.out.print("请输入子字符串: ");
		String needle = scanner.nextLine();
		System.out.println(findFirstMatch(haystack, needle));

	}

	public static int findFirstMatch(String haystack, String needle) {
		// 特殊情况处理：needle 为空字符串时返回 0
		if (needle == null || needle.length() == 0) {
			return 0;
		}

		// 主字符串和子字符串的长度
		int m = haystack.length();
		int n = needle.length();

		// 遍历主字符串的每个起始位置
		for (int i = 0; i <= m - n; i++) {
			// 截取主字符串中与子字符串长度相等的子串
			String sub = haystack.substring(i, i + n);

			// 判断截取的子串是否与子字符串相等
			if (sub.equals(needle)) {
				return i;// 返回第一个匹配位置
			}
		}
		// 如果遍历结束未找到匹配项，返回 -1
		return -1;
	}
}
