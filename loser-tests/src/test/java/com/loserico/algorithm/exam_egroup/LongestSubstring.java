package com.loserico.algorithm.exam_egroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 题目描述:
 * <p/>
 * 给定一个字符串，只包含大写字母，求在包含同一字母的子串中，长度第 k 长的子串的长度，相同字母只取最长的那个子串。
 * <p/>
 * 输入描述: <br/>
 * 第一行有一个子串(1<长度<=100)，只包含大写字母。 <br/>
 * 第二行为 k的值 <br/>
 * <p/>
 * 输出描述: <br/>
 * 输出连续出现次数第k多的字母的次数。
 * <p/>
 * 补充说明: <br/>
 * 若子串中只包含同一字母的子串数小于k，则输出-1
 * <p/>
 * 用例1:
 * <p/>
 * 输入:
 * <pre> {@code
 * AAAAHHHBBCDHHHH
 * 3
 * }</pre>
 * <p>
 * 输出:
 * 2
 * <p/>
 * 说明：同一字母连续出现的最多的是A和H，四次；第二
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-09-22 12:02
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestSubstring {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入子串: ");
		String input = scanner.nextLine().trim();
		System.out.print("请输入k值: ");
		int k = scanner.nextInt();

		// 用来存储字母及其最长连续出现次数
		Map<Character, Integer> maxCounts = new HashMap<>();

		// 遍历字符串以找到最长的连续子串
		char currentChar = input.charAt(0);
		int currentCount = 1;
		for (int i = 1; i < input.length(); i++) {
			char nextChar = input.charAt(i);
			if (nextChar == currentChar) {
				currentCount++;
			} else {
				// 更新最长连续子串
				maxCounts.put(currentChar, Math.max(maxCounts.getOrDefault(currentChar, 0), currentCount));
				// 更新当前字符和连续出现次数
				currentChar = nextChar;
				currentCount = 1;
			}

		}
		// 更新最后一个字符的出现次数
		maxCounts.put(currentChar, Math.max(maxCounts.getOrDefault(currentChar, 0), currentCount));
		// 将出现次数转为列表并排序
		List<Integer> lengths = new ArrayList<>(maxCounts.values());
		Collections.sort(lengths, Collections.reverseOrder());

		// 检查并输出第k长的长度
		if (k <= lengths.size()) {
			System.out.println(lengths.get(k - 1));
		} else {
			System.out.println(-1);
		}

		scanner.close();
	}
}
