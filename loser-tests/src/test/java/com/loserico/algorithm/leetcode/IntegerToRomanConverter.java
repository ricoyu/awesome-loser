package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 整数转罗马数字
 * <p/>
 * 七个不同的符号代表罗马数字，其值如下： <p/>
 * 符号值 <br/>
 * I     1 <br/>
 * V     5 <br/>
 * X     10 <br/>
 * L     50 <br/>
 * C     100 <br/>
 * D     500 <br/>
 * M     1000 <br/>
 * <p/>
 * 罗马数字是通过添加从最高到最低的小数位值的转换而形成的。将小数位值转换为罗马数字有以下规则：
 * <p/>
 * 如果该值不是以 4 或 9 开头，请选择可以从输入中减去的最大值的符号，将该符号附加到结果，减去其值，然后将其余部分转换为罗马数字。
 * <p/>
 * 如果该值以 4 或 9 开头，使用 减法形式，表示从以下符号中减去一个符号，例如 4 是 5 (V) 减 1 (I): IV ，9 是 10 (X) 减 1 (I)：IX。仅使用以下减法形式：4 (IV)，9 (IX)，40
 * (XL)，90 (XC)，400 (CD) 和 900 (CM)。
 * <p/>
 * 只有 10 的次方（I, X, C, M）最多可以连续附加 3 次以代表 10 的倍数。你不能多次附加 5 (V)，50 (L) 或 500 (D)。如果需要将符号附加4次，请使用 减法形式。
 * <p/>
 * 给定一个整数，将其转换为罗马数字。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：num = 3749 <br/>
 * 输出： "MMMDCCXLIX" <br/>
 * 解释： <br/>
 * 3000 = MMM 由于 1000 (M) + 1000 (M) + 1000 (M) <br/>
 * 700 = DCC 由于 500 (D) + 100 (C) + 100 (C) <br/>
 * 40 = XL 由于 50 (L) 减 10 (X) <br/>
 * 9 = IX 由于 10 (X) 减 1 (I) <br/>
 * 注意：49 不是 50 (L) 减 1 (I) 因为转换是基于小数位
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：num = 58 <br/>
 * 输出："LVIII" <br/>
 * 解释： <br/>
 * 50 = L <br/>
 * 8 = VIII <br/>
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：num = 1994 <br/>
 * 输出："MCMXCIV" <br/>
 * 解释：
 * <p/>
 * 1000 = M <br/>
 * 900 = CM <br/>
 * 90 = XC <br/>
 * 4 = IV <br/>
 *
 * <ul>解题思路：
 *     <li/>罗马数字规则：根据题目提供的罗马数字规则及其特殊的减法形式，我们需要从大到小依次将整数减去能表示的最大值，同时记录对应的罗马数字。
 *     <li/>预定义映射表：将所有可能的罗马数字与其对应的整数值存储在一个数组中，从大到小排序（包含特殊的减法形式）。
 *     <li/>迭代减法：从最大的数值开始尝试减去输入值，记录对应的罗马数字，直到整数变为零。
 *     <li/>特殊处理：包含减法形式的值（如900, 400, 90, 等）放在映射表中，算法会优先匹配这些值。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-12-24 8:35
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class IntegerToRomanConverter {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for(int i = 0; i < 3; i++) {
			System.out.print("请输入数字: ");
			int num = scanner.nextInt();
			System.out.println(intToRoman(num));
		}
		scanner.close();
	}

	public static String intToRoman(int num) {
		// 定义所有可能的罗马数字及其对应的整数值，从大到小排序
		int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
		String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

		StringBuilder sb = new StringBuilder();

		// 遍历预定义的数值-符号对
		for (int i = 0; i < values.length; i++) {
			// 如果当前数值可以从num中减去
			while (num >= values[i]) {
				num -= values[i];// 减去当前数值
				sb.append(symbols[i]); // 添加对应的罗马数字符号
			}
		}

		return sb.toString();
	}
}
