package com.loserico.algorithm.exam_egroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 最长子字符串长度一
 * <p/>
 * 题目描述
 * <p/>
 * 给你一个字符串 s，首尾相连成一个环形，请你在环中找出 'o' 字符出现了偶数次最长子字符串的长度。
 * <p/>
 * 输入描述
 * <p/>
 * 输入是一个小写字母组成的字符串
 * <p/>
 * 输出描述
 * <p/>
 * 输出是一个整数
 * <p/>
 * 备注
 * <p/>
 * 1 ≤ s.length ≤ 500000
 * <p/>
 * s 只包含小写英文字母
 * <p/>
 * 用例1
 * <p/>
 * 输入
 * <p/>
 * alolobo
 * <p/>
 * 输出
 * <p/>
 * 6
 * <p/>
 * Copyright: Copyright (c) 2024-10-10 15:56
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestSubstrWithEvenO {

	public static void main(String[] args) {
		System.out.print("请输入字符串: ");
		Scanner scanner = new Scanner(System.in);
		String s = scanner.nextLine();
		System.out.println(findLongestEvenO(s));
		scanner.close();
	}

	public static int findLongestEvenO(String s) {
		int n = s.length();
		// 将字符串复制一遍，模拟环形字符串，方便处理子串情况
		String doubleS = s + s;

		int maxLength = 0;
		int[] prefix = new int[2 * n + 1]; // 前缀和数组，用于存储 'o' 出现次数的奇偶状态
		int currentState = 0;
		Map<Integer, Integer> stateIndexMap = new HashMap<>();
		stateIndexMap.put(0, -1); // 初始状态为0，索引为-1，表示初始位置

		for (int i = 0; i < 2 * n; i++) {
			// 更新当前状态，如果字符是 'o'，翻转奇偶状态
			if (doubleS.charAt(i) == 'o') {
				currentState ^= 1;
			}
			// 如果当前状态之前出现过，计算最大长度
			if (stateIndexMap.containsKey(currentState)) {
				int prevIndex = stateIndexMap.get(currentState);
				if (i - prevIndex <= n) { // 确保长度不超过原始字符串长度
					maxLength = Math.max(maxLength, i - prevIndex);
				}
			} else {
				stateIndexMap.put(currentState, i);
			}
		}

		return maxLength;
	}
}
