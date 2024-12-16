package com.loserico.algorithm.exam_egroup;

import java.util.HashSet;
import java.util.Scanner;

/**
 * 【字符串分割】给定非空字符在s，将该字符串分割成一些子串，使每个子串的ASCII A码值的和均为水仙花数。 <p/>
 * 1、若分割不成功则返回 0 <br/>
 * 2、若分割成功且分割结果不唯一则返回-1 <br/>
 * 3、若分割成功且分割结果唯一，则返回分割后的子串数目 <p/>
 * 输入描述: 1、输入字符串的最大长度为 200 <br/>
 * 输出描述:根据题目描述中情况返回相应的结果 <p/>
 * 备注:“水仙花数”是指一个三位数，每位上数字的立方和等于该数字本身，如 371是“水仙花数”，因为:371=3^3+7^3+1^3。 <p/>
 * 示例： <br/>
 * 输入 <br/>
 * abc <p/>
 * 输出 <br/>
 * 0
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-10-01 11:06
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class NarcissisticNumberSplitter {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入第"+(i+1)+"个字符串: ");
			String input = scanner.nextLine();
			int result = splitIntoNarcissistic(input);
			System.out.println(result);
		}
	}

	/**
	 * 计算给定字符串中所有字符的ASCII值的和。
	 *
	 * @param str
	 * @return
	 */
	public static int asciiSum(String str) {
		int sum = 0;
		for (char c : str.toCharArray()) {
			sum += (int) c;
		}
		return sum;
	}

	/**
	 * 判断给定的数字是否为水仙花数。
	 * 水仙花数是指一个三位数，每位上数字的立方和等于该数字本身。
	 *
	 * @param num
	 * @return
	 */
	public static boolean isNarcissistic(int num) {
		if (num < 100 || num > 999) {
			return false; // 仅检查三位数
		}
		int sum = 0;
		int originalSum = num;
		while (num > 0) {
			int digit = num % 10;
			sum += digit * digit * digit;
			num /= 10;
		}
		return sum == originalSum;
	}

	/**
	 * 生成所有可能的水仙花数，并存入集合中。
	 *
	 * @return
	 */
	public static HashSet<Integer> generateNarcissisticNumbers() {
		HashSet<Integer> narcissisticNumbers = new HashSet<>();
		for (int i = 100; i <= 999; i++) {
			if (isNarcissistic(i)) {
				narcissisticNumbers.add(i);
			}
		}

		return narcissisticNumbers;
	}

	/**
	 * 检查字符串是否可以分割成水仙花数的子串，并返回相应的结果。
	 * 结果有三种可能：
	 * 0 - 无法分割
	 * -1 - 存在多种分割方式
	 * 正整数 - 唯一的分割方式数量
	 *
	 * @param s
	 * @return
	 */
	public static int splitIntoNarcissistic(String s) {
		HashSet<Integer> narcissisticNumbers = generateNarcissisticNumbers();
		int n = s.length();
		int[] dp = new int[n + 1];
		dp[0] = 1; // dp[0] 表示空字符串可以分割（基础情况）

		// 动态规划填充 dp 数组
		for (int i = 0; i < n; i++) {
			for (int j = i - 1; j >= 0; j--) {
				String sub = s.substring(j, i);
				int sum = asciiSum(sub);
				if (narcissisticNumbers.contains(sum)) {
					dp[i] += dp[j]; // 更新分割方式数量
				}
			}
		}

		// 检查 dp[n] 的值
		if (dp[n] == 0) {
			return 0;// 无法分割
		} else if (dp[n] > 0) {
			return -1; // 存在多种分割方式
		}else {
			return 1; // 唯一分割方式
		}
	}
}
