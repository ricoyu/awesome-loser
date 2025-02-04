package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 字形变换
 * <p/>
 * 将一个给定字符串 s 根据给定的行数 numRows ，以从上往下、从左到右进行 Z 字形排列。
 * <p/>
 * 比如输入字符串为 "PAYPALISHIRING" 行数为 3 时，排列如下： <p/>
 * P   A   H   N <br/>
 * A P L S I I G <br/>
 * Y   I   R <br/>
 * <p>
 * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："PAHNAPLSIIGYIR"。
 * <p/>
 * 请你实现这个将字符串进行指定行数变换的函数： string convert(string s, int numRows);
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：s = "PAYPALISHIRING", numRows = 3 <br/>
 * 输出："PAHNAPLSIIGYIR" <br/>
 * <p/>
 * 示例 2： <p/>
 * 输入：s = "PAYPALISHIRING", numRows = 4 <br/>
 * 输出："PINALSIGYAHRPI" <br/>
 * 解释：
 * P     I    N <br/>
 * A   L S  I G <br/>
 * Y A   H R <br/>
 * P     I <br/>
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：s = "A", numRows = 1
 * 输出："A"
 * <p/>
 * 解题思路：
 *
 * <ul>基本规则：
 *     <li/>Z 字形排列是按照从上到下、再从下到上的顺序进行填充。
 *     <li/>对于每一行，有两个方向：向下（垂直填充）和向上（对角线填充）。
 * </ul>
 * <ul>模拟填充过程：
 *     <li/>创建 numRows 个字符串列表，分别代表每一行。
 *     <li/>遍历字符串 s，将字符依次放入对应行的列表中。
 *     <li/>使用一个标志变量 direction 来判断是向下还是向上移动。
 * </ul>
 * <ul>最终输出：
 *     <li/>将所有行的字符拼接起来，形成结果字符串。
 * </ul>
 * <ul>特殊情况：
 *     <li/>如果 numRows 等于 1，则无需进行变换，直接返回原字符串。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-12-29 11:57
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ZigzagConversion {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入字符串: ");
		String input = scanner.nextLine();
		System.out.print("请输入行数: ");
		int numRows = scanner.nextInt();
		System.out.println(convert(input, numRows));
	}

	public static String convert(String s, int numRows) {
		// 如果只有一行，则无需变换，直接返回
		if (numRows == 1 || s.length() <= numRows) {
			return s;
		}

		// 创建一个StringBuilder数组，存储每一行的字符
		StringBuilder[] rows = new StringBuilder[numRows];
		for (int i = 0; i < numRows; i++) {
			rows[i] = new StringBuilder();
		}

		// 当前所在行索引
		int currentRow = 0;
		// 方向标志，向下为true，向上为false
		boolean goingDown = false;

		// 遍历字符串中的每个字符
		for (char c : s.toCharArray()) {
			// 将当前字符添加到对应行的StringBuilder中
			rows[currentRow].append(c);

			// 如果当前行是第一行或最后一行，则需要改变方向
			if ((currentRow == 0) || (currentRow == numRows - 1)) {
				goingDown = !goingDown;
			}

			// 根据方向更新当前行索引
			currentRow += goingDown ? 1 : -1;
		}

		// 将所有行的内容拼接成一个最终字符串
		StringBuilder result = new StringBuilder();
		for (StringBuilder row : rows) {
			result.append(row);
		}

		return result.toString();
	}
}
