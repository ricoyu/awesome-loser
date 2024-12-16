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
public class KthPermutation2 {

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
		List<Integer> res = new ArrayList<>();
		for (int i = 1; i <= n; i++) {
			res.add(i);
		}
		int[] fact = new int[n]; // 用于存储1..n-1的阶乘
		fact[0] = 1;
		for (int i = 1; i < n; i++) {
			fact[i] = fact[i - 1] * i;
		}

		/*
		 * 假设n=4, k=18, 有这么多种排列组合
		 * 1 2 3 4
		 * 1 2 4 3
		 * 1 3 2 4
		 * 1 3 4 2
		 * 1 4 2 3
		 * 1 4 3 2
		 * 2 1 3 4
		 * 2 1 4 3
		 * 2 3 1 4
		 * 2 3 4 1
		 * 2 4 1 3
		 * 2 4 3 1
		 * 3 1 2 4
		 * 3 1 4 2
		 * 3 2 1 4
		 * 3 2 4 1
		 * 3 4 1 2
		 * 3 4 2 1
		 * 4 1 2 3
		 * 4 1 3 2
		 * 4 2 1 3
		 * 4 2 3 1
		 * 4 3 1 2
		 * 4 3 2 1
		 *
		 * fact= [1, 1, 2, 6]
		 * 18/4 = 4
		 */
		k = k - 1;
		StringBuilder sb = new StringBuilder();
		for (int i = n; i > 0; i--) {
			/*
			 * 1. 确定第1位后, 剩下的数字有fact[i - 1]种排列组合
			 * 2. 所以第k个排列应该是需要到第 k / fact[i - 1] 组排列组合中去找
			 * 3. 比如第一个数字是1的话, 1开头的排列组合有6种, 所以第7个排列应该是在2开头的组合里面去找
			 * 4. 第7个排列应该出现在2开头的组合的 7%6=1 个位置
			 * 5. 所以k的意义是在变化的, 初始的意义是所有排列中的第k个排列
			 * 6. 每次确定一个数字后, k就变成了在这个数字开头的排列中的第k个排列, 而不是所有排列中的第k个排列
			 */
			int index = k / fact[i - 1]; // 第一次是 17/6 = 2, 确定了第一个数字是3, 因为res列表存的是1, 2, 3, 4
			k = k % fact[i - 1]; //17 % 6 = 5 //第一个数字是2这个排列组合中的第6个
			sb.append(res.get(index));
			res.remove(index);
		}

		return sb.toString();
	}
}
