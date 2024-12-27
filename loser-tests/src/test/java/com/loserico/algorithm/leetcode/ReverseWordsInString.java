package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 反转字符串中的单词
 * <p/>
 * 给你一个字符串 s ，请你反转字符串中 单词 的顺序。
 * <p/>
 * 单词 是由非空格字符组成的字符串。s 中使用至少一个空格将字符串中的 单词 分隔开。
 * <p/>
 * 返回 单词 顺序颠倒且 单词 之间用单个空格连接的结果字符串。
 * <p/>
 * 注意：输入字符串 s中可能会存在前导空格、尾随空格或者单词间的多个空格。返回的结果字符串中，单词间应当仅用单个空格分隔，且不包含任何额外的空格。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：s = "the sky is blue" <br/>
 * 输出："blue is sky the" <br/>
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：s = "  hello world  " <br/>
 * 输出："world hello" <br/>
 * 解释：反转后的字符串中不能存在前导空格和尾随空格。
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：s = "a good   example" <br/>
 * 输出："example good a" <br/>
 * 解释：如果两个单词间有多余的空格，反转后的字符串需要将单词间的空格减少到仅有一个。
 *
 * <ul>解题思路
 *     <li/>首先，我们需要将字符串前后的空格以及单词间多余的空格去掉，确保单词间仅有一个空格。
 *     <li/>可以通过Java提供的String.trim()方法去除首尾空格，并用正则表达式将多余空格替换为单个空格。
 *     <li/>使用String.split(" ")方法按空格分割字符串，将单词存储到一个数组中。
 *     <li/>遍历单词数组，使用双指针或者直接使用Collections.reverse()方法来反转单词顺序。
 *     <li/>将反转后的单词数组用单个空格连接成最终结果字符串。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-12-27 9:29
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ReverseWordsInString {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入字符串: ");
			String input = scanner.nextLine();
			System.out.println(reverseWords(input));
		}
		scanner.close();
	}

	public static String reverseWords(String s) {
		//去除首尾空格，并将多个连续空格替换为一个空格
		s = s.trim().replaceAll("\\s+", " ");

		//按空格分割字符串，得到单词数组
		String[] arr = s.split(" ");

		//反转单词数组
		//创建 StringBuilder 来高效拼接字符串
		StringBuilder sb = new StringBuilder();
		for (int i = arr.length-1; i >= 0; i--) {
			sb.append(arr[i]);
			if (i != 0) {
				sb.append(" ");
			}
		}

		return sb.toString();
	}
}
