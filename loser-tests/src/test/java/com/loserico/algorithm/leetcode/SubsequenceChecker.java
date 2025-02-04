package com.loserico.algorithm.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 判断子序列
 * <p/>
 * 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
 * <p/>
 * 字符串的一个子序列是原始字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。（例如，"ace"是"abcde"的一个子序列，而"aec"不是）。
 * <p/>
 * 进阶：
 * <p/>
 * 如果有大量输入的 S，称作 S1, S2, ... , Sk 其中 k >= 10亿，你需要依次检查它们是否为 T 的子序列。在这种情况下，你会怎样改变代码？
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：s = "abc", t = "ahbgdc" <br/>
 * 输出：true
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：s = "axc", t = "ahbgdc" <br/>
 * 输出：false
 *
 * <ul>解题思路：
 *     <li/>遍历字符串 t，同时用一个指针遍历字符串 s。
 *     <li/>如果发现 t 中的字符与 s 当前指针指向的字符匹配，则将 s 的指针后移一位。
 *     <li/>如果 s 的指针成功走到 s.length()，则说明 s 是 t 的子序列。
 * </ul>
 * <ul>进阶问题：
 *     <li/>由于 S1, S2, ..., Sk 的数量可能达到 10 亿以上，直接使用暴力匹配会非常慢。
 *     <li/>为了解决这个问题，可以先预处理 t，为 t 中每个字符生成一个索引表，用于快速找到下一个出现的字符。
 *     <li/>然后对每个字符串 s，通过索引表快速验证其是否是 t 的子序列。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2025-01-07 9:01
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SubsequenceChecker {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
		    System.out.print("请输入字符串s: ");
			String s = scanner.nextLine();
			System.out.print("请输入字符串t: ");
			String t = scanner.nextLine();
			System.out.println(isSubsequence(s, t));
		}
	}

	public static boolean isSubsequence(String s, String t) {
		// 双指针法
		int sIndex = 0, tIndex = 0;

		// 遍历 t，尝试匹配 s 的每个字符
		while (sIndex < s.length() && tIndex < t.length()) {
			// 如果当前字符匹配，则 s 的指针向前移动
			if (s.charAt(sIndex) == t.charAt(tIndex)) {
				sIndex++;
			}
			// t 的指针总是向前移动
			tIndex++;
		}

		// 如果 s 的指针已经走到了末尾，说明 s 是 t 的子序列
		return sIndex == s.length();
	}

	/**
	 * 使用预处理后的索引映射快速判断 s 是否为 t 的子序列
	 *
	 * @param s
	 * @param charToIndexMap
	 * @return
	 */
	public static boolean isSubsequenceUsingIndexMap(String s, Map<Character, List<Integer>> charToIndexMap) {
		// 当前在 t 中的位置
		int currentIndex = -1;
		for (char c : s.toCharArray()) {
			// 如果字符 c 不在 t 中，直接返回 false
			if (!charToIndexMap.containsKey(c)) {
				return false;
			}

			// 二分查找字符 c 在 t 中的下一个位置
			List<Integer> indexList = charToIndexMap.get(c);
			int nextIndex = binarySearchNextIndex(indexList, currentIndex);

			// 如果找不到下一个位置，说明 s 不是 t 的子序列
			if (nextIndex == -1) {
				return false;
			}

			// 更新当前在 t 中的位置
			currentIndex = nextIndex;
		}

		return true;
	}

	public static List<Boolean> isSubsequenceBatch(List<String> sArray, String t) {
		// 预处理 t，构建字符索引映射表
		Map<Character, List<Integer>> charToIndexMap = preprocess(t);
		List<Boolean> results = new ArrayList<>();

		for (String s : sArray) {
			results.add(isSubsequenceUsingIndexMap(s, charToIndexMap));
		}

		return results;
	}

	private static Map<Character, List<Integer>> preprocess(String t) {
		Map<Character, List<Integer>> charToIndexMap = new HashMap<>();
		for (int i = 0; i < t.length(); i++) {
			char c = t.charAt(i);
			charToIndexMap.putIfAbsent(c, new ArrayList<>());
			charToIndexMap.get(c).add(i);
		}
		return charToIndexMap;
	}

	/**
	 * 在索引列表中使用二分查找找到第一个大于 currentIndex 的位置
	 *
	 * @param indexList
	 * @param currentIndex
	 * @return
	 */
	private static int binarySearchNextIndex(List<Integer> indexList, int currentIndex) {
		int left = 0, right = indexList.size() - 1;

		// 标准的二分查找模板
		while (left <= right) {
			int mid = left + (right - left) / 2;
			if (indexList.get(mid) > currentIndex) {
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}

		// 如果 left 越界，说明没有找到有效的下一个索引
		return left < indexList.size() ? indexList.get(left) : -1;
	}
}
