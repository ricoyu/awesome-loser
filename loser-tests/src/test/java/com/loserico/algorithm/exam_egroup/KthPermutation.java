package com.loserico.algorithm.exam_egroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 题目描述: <br/>
 * 给定参数n，从1到n会有n个整数:1,2,3,.,n，这n个数字共有 n!种排列。按大小顺序升序列出所有排列情况，并一一标记，当n=3时,所有排列如下:
 * <ol>
 *     <li/>"123"
 *     <li/>"132"
 *     <li/>"213"
 *     <li/>"231"
 *     <li/>"312"
 *     <li/>"321"
 * </ol>
 * <p>
 * 给定n 和 k，返回第k个排列。
 * <p/>
 * 输入描述: <br/>
 * 输入两行，第一行为n，第二行为k，给定n的范国是[1,9]，给定k的范围是[1,n!]。
 * <p/>
 * 输出描述: <br/>
 * 输出排在第k位置的数字。
 * <p/>
 * 示例1
 * <p/>
 * 输入：<br/>
 * 3 <br/>
 * 3
 * <p/>
 * 输出：<br/>
 * 213
 * <p/>
 * 说明： 3的排列有123 132 213...，那么第3位置的为213
 * <p/>
 * Copyright: Copyright (c) 2024-09-23 9:53
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class KthPermutation {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入n: ");
		int n = scanner.nextInt();
		System.out.print("请输入k: ");
		int k = scanner.nextInt();
		String result = getKthPermutation(n, k);
		System.out.println(result);
	}
	public static String getKthPermutation(int n, int k) {
		// 创建一个包含从 1 到 n 的数字列表
		List<Integer> numbers = new ArrayList<>();
		for (int i = 1; i <= n; i++) {
			numbers.add(i);
		}

		// k 减去 1 因为我们要使用 0 基索引
		k -= 1;

		// 初始化结果字符串
		StringBuilder permutation = new StringBuilder();

		//计算 (n-1)! 的阶乘以确定每个数字的范围
		int factorial = 1;
		for (int i = 1; i < n; i++) {
			factorial *= i;
		}

		// 遍历每一位
		for (int i = n; i > 0; i--) {
			int index = k / factorial;
			// 将选中的数字添加到结果中
			permutation.append(numbers.get(index));
			// 从列表中移除选中的数字
			numbers.remove(index);

			// 更新 k，获取剩余部分的索引
			k %= factorial;

			// 计算下一个数字的范围，(i-1)!
			if (i > 1) {
				factorial /= (i - 1);
			}
		}

		return permutation.toString();
	}
}
