package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 最后一个单词的长度
 * <p/>
 * 给你一个字符串 s，由若干单词组成，单词前后用一些空格字符隔开。返回字符串中 最后一个 单词的长度。
 * <p/>
 * 单词 是指仅由字母组成、不包含任何空格字符的最大子字符串
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：s = "Hello World" <br/>
 * 输出：5 <br/>
 * 解释：最后一个单词是“World”，长度为 5。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：s = "   fly me   to   the moon  " <br/>
 * 输出：4 <br/>
 * 解释：最后一个单词是“moon”，长度为 4。
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：s = "luffy is still joyboy" <br/>
 * 输出：6 <br/>
 * 解释：最后一个单词是长度为 6 的“joyboy”。
 *
 * <p/>
 * 这个问题可以通过从字符串的尾部向前遍历来解决。核心思路是忽略字符串中的尾部空格，然后找到最后一个单词的长度。
 * <ul>解题思路：
 *     <li/>首先，去除字符串尾部的空格，因为空格不会影响单词的判断。
 *     <li/>然后，从尾部开始查找字符，直到找到第一个空格为止，表示已经找到了最后一个单词。
 *     <li/>最后，返回该单词的长度。
 * </ul>
 * Copyright: Copyright (c) 2024-12-26 9:39
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LastWordLength {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i <= 3; i++) {
			System.out.print("请输入字符串: ");
			String input = scanner.nextLine();
			System.out.println(lengthOfLastWord(input));
		}
		scanner.close();
	}

	public static int lengthOfLastWord(String s) {
		// 1. 去除尾部空格
		s = s.trim();
		// 2. 从尾部开始找到第一个空格的位置
		int length = 0;
		for (int i = s.length()-1; i >= 0; i--) {
			// 如果字符是空格，停止循环
			if (s.charAt(i) == ' ') {
				break;
			}
			// 如果不是空格，则计数
			length++;
		}

		// 3. 返回最后一个单词的长度
		return length;
	}
}
