package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 第 N 个泰波那契数
 * <p>
 * 泰波那契序列 Tn 定义如下：
 * <p>
 * T0 = 0, T1 = 1, T2 = 1, 且在 n >= 0 的条件下 Tn+3 = Tn + Tn+1 + Tn+2
 * <p/>
 * 给你整数 n，请返回第 n 个泰波那契数 Tn 的值。
 * <p/>
 * 示例 1：<br/>
 * 输入：n = 4 <br/>
 * 输出：4 <p/>
 * 解释： <br/>
 * T_3 = 0 + 1 + 1 = 2 <br/>
 * T_4 = 1 + 1 + 2 = 4
 * <p/>
 * 示例 2： <br/>
 * 输入：n = 25 <br/>
 * 输出：1389537
 * <p>
 * 下面是详细的实现步骤和代码： <p/>
 * <ol>
 *     <li/>初始化: 首先，我们需要为泰波那契序列的前三个数设定初值：T0 = 0, T1 = 1, T2 = 1。
 *     <li/>边界条件处理: 如果 n 是 0、1 或 2，我们可以直接返回这些值，因为这是泰波那契数列的定义。
 *     <li/>迭代计算: 对于 n >= 3 的情况，我们用一个循环来计算从 T3 到 Tn 的每一个值。每一步，我们都利用前三个数的和来更新下一个泰波那契数，并更新这三个数，滑动窗口前进。
 *     <li/>结果返回: 最终，循环结束后，我们得到的最后一个更新的数就是 Tn。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-10-23 9:24
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class Tribonacci {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入第"+(i+1)+"个泰波那契数: ");
			int n = scanner.nextInt();
			System.out.println(tribonacci(n));
		}
	}

	public static int tribonacci(int n) {
		if (n == 0) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}

		// 初始化泰波那契序列的前三个值
		int t0 = 0, t1 = 1, t2 = 1;

		// 用于存储当前计算的泰波那契数
		int current = 0;

		// 开始计算从 T3 到 Tn
		for (int i = 3; i <= n; i++) {
			current = t0 + t1 + t2;
			t0 = t1;
			t1 = t2;
			t2 = current;
		}

		return current;
	}
}
