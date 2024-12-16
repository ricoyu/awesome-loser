package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 问题描述
 * <p/>
 * C 语言中有一个库函数 char *strstr(const char *haystack, const char *needle)，用于在字符串 haystack 中查找第一次出现字符串 needle 的位置。现在需要实现一个 strstr 的增强函数，支持使用带可选段的字符串进行模糊查询。
 * <p/>
 * 可选段使用 "[]" 标识，表示该位置可以是可选段中的任意一个字符。例如，"a[bc]" 可以匹配 "ab" 或 "ac"。
 * <p/>
 * 输入格式: <br/>
 * 输入包含两个字符串，分别是源字符串和目标字符串，以空格分隔。
 * <p/>
 * 输出格式: <br/>
 * 输出一个整数，表示匹配子字符串在源字符串中的起始位置（从 0 开始计数）。如果没有匹配，则输出 -1。
 * <p/>
 * 样例输入: abcd b[cd]
 * <p/>
 * 样例输出: 1
 * <p/>
 * 样例解释: <br/>
 * 目标字符串 "b[cd]" 可以匹配 "bc" 或 "bd"。在源字符串 "abcd" 中，"bc" 子字符串的起始位置是 1。
 * <p/>
 * <ul>数据范围:
 *     <li/>源字符串中不包含 '[]'。
 *     <li/>目标字符串中的 '[]' 成对出现，且不会嵌套。
 *     <li/>输入的字符串长度在[1,100] 之间。
 * </ul>
 * <p>
 * Copyright: Copyright (c) 2024-09-11 20:45
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class EnhancedStrStr {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入源字符串和目标字符串: ");
		String input = scanner.nextLine();
		String source = input.split(" ")[0];
		String target = input.split(" ")[1];
		System.out.println(enahancedStrStr(source, target));
	}

	/**
	 * 对目标字符串中的特殊正则字符进行转义处理，确保正则表达式正确匹配字面值。
	 * @param pattern
	 * @return
	 */
	public static String escapeRegex(String pattern) {
		StringBuilder regex = new StringBuilder();
		for (int i = 0; i < pattern.length(); i++) {
			char ch = pattern.charAt(i);
			if ("\\.^$|?*+(){}".indexOf(ch) != -1) {
				regex.append('\\');
			}
			regex.append(ch);
		}

		return regex.toString();
	}

	/**
	 * 查找目标字符串在源字符串中的第一次出现位置，支持带可选段的模糊查询。
	 * @param haystack
	 * @param needle
	 * @return
	 */
	public static int enahancedStrStr(String haystack, String needle) {
		String regex = escapeRegex(needle);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(haystack);

		if (matcher.find()) {
			return matcher.start();
		}

		return -1;
	}
}
