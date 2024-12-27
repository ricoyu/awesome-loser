package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 最长公共前缀
 * <p/>
 * 编写一个函数来查找字符串数组中的最长公共前缀。 <p/>
 * 如果不存在公共前缀，返回空字符串 ""。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：strs = ["flower","flow","flight"] <br/>
 * 输出："fl"
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：strs = ["dog","racecar","car"] <br/>
 * 输出："" <br/>
 * 解释：输入不存在公共前缀。
 * <p>
 * <p/>
 * <ul>横向扫描法：
 *     <li/>设定一个初始的公共前缀为第一个字符串。然后逐个与后续字符串进行比较，更新公共前缀。
 *     <li/>比较两个字符串的公共前缀时，从第一个字符开始，逐个字符进行比较，直到某个字符不匹配或字符串结束为止。
 *     <li/>如果某个字符串与当前公共前缀没有公共部分，则更新公共前缀为空字符串，表示不存在公共前缀。
 * </ul>
 * <ul>纵向扫描法：
 *     <li/>通过逐列对比每个字符串的字符。
 *     <li/>从第一个字符开始，检查所有字符串的第一个字符是否相同，若相同则继续检查第二个字符，以此类推。
 *     <li/>若某列的字符不同，则返回当前已经扫描过的部分作为公共前缀。
 * </ul>
 * Copyright: Copyright (c) 2024-12-26 18:20
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestCommonPrefix {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入字符串数组：");
			String input = scanner.nextLine().trim();
			String[] parts = Arrays.toStringArray(input);
			System.out.println(longestCommonPrefix(parts));
		}
	}

	public static String longestCommonPrefix(String[] strs) {
		// 如果数组为空，返回空字符串
		if (strs == null || strs.length == 0) {
			return "";
		}

		// 假设第一个字符串为公共前缀
		String prefix = strs[0];

		// 从第二个字符串开始遍历
		for (int i = 1; i < strs.length; i++) {
			// 使用当前字符串与前缀进行比较，直到找到公共部分
			while (strs[i].indexOf(prefix) != 0) {
				// 如果当前前缀在当前字符串中不存在，就去掉前缀的最后一个字符
				prefix = prefix.substring(0, prefix.length() - 1);
				// 如果前缀已经为空，说明没有公共前缀，直接返回
				if (prefix.isEmpty()) {
					return "";
				}
			}
		}
		// 返回找到的最长公共前缀
		return prefix;
	}
}
