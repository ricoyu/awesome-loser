package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 字符串变换最小字符串
 * <p/>
 * 问题描述: <p/>
 * 给定一个由小写字母组成的字符串s，你可以最多进行一次变换操作。变换操作的规则是：交换字符串中任意两个不同位置的字符。请返回经过变换后能得到的字典序最小的字符串。
 * <p/>
 * 输入格式:
 * <p/>
 * 输入一行，包含一个由小写字母组成的字符串s。
 * <p/>
 * 输出格式:
 * <p/>
 * 输出一行，表示经过最多一次变换后得到的字典序最小的字符串。
 * <p/>
 * 样例输入:
 * <br/>
 * abcdef
 * <p/>
 * 样例输出:
 * <br/>
 * abcdef
 * <p/>
 * 样例解释:
 * <p/>
 * 字符串 "abcdef" 已经是字典序最小的，不需要进行任何交换操作。
 * <p/>
 * 样例输入:
 * <br/>
 * bcdefa
 * <p/>
 * 样例输出: <br/>
 * acdefb
 * <p/>
 * 样例解释:
 * <p/>
 * 将 'a' 和 'b' 交换位置，可以得到字典序最小的字符串 "acdefb"。
 * <p/>
 * 思路步骤 <p/>
 * 1. 遍历字符：遍历字符串中的每一个字符，记录下每个字符右侧的最小字符和其索引。 <p/>
 * 2. 选择交换：如果找到一个字符可以和它的右侧字符交换，并且交换后会得到更小的字符串，则进行交换。 <p/>
 * 3. 构建结果：在交换后构建新的字符串，并返回。<p/>
 * 返回结果：
 * <p>
 * 比较所有可能的结果，返回字典序最小的字符串。
 * <p/>
 * Copyright: Copyright (c) 2024-09-28 8:58
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MinLexicoString {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入字符串: ");
			String s = scanner.nextLine();

			// 调用方法获取字典序最小的字符串
			String result = getMinLexicoString(s);
			System.out.println(result);
		}
		scanner.close();
	}

	/**
	 * 获取经过最多一次交换后得到的字典序最小的字符串
	 *
	 * @param s 输入的字符串
	 * @return 字典序最小的字符串
	 */
	public static String getMinLexicoString(String s) {
		// 将字符串转化为字符数组以便进行操作
		char[] chars = s.toCharArray();
		int n = chars.length;

		// 记录最小字符及其位置
		char minChar = chars[n - 1];
		int minIndex = n - 1;
		int swapIndex1 = -1;// 第一个交换字符的位置
		int swapIndex2 = -1;// 第二个交换字符的位置

		// 遍历字符数组，寻找可以交换的字符
		for (int i = n - 2; i >= 0; i--) {
			// 如果当前字符大于记录的最小字符，则考虑交换
			if (chars[i] > minChar) {
				swapIndex1 = i;// 需要交换的字符位置
				swapIndex2 = minIndex;// 当前最小字符的位置
			}else if (chars[i] < minChar) {
				// 更新最小字符及其索引
				minChar = chars[i];
				minIndex = i;
			}
		}

		// 如果没有找到可以交换的字符，则返回原字符串
		if (swapIndex1 == -1 || swapIndex2 == -1) {
			return s;
		}

		// 交换字符并构造新的字符串
		char temp = chars[swapIndex1];
		chars[swapIndex1] = chars[swapIndex2];
		chars[swapIndex2] = temp;

		return new String(chars);
	}

}
