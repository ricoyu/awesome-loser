package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 斐波那契数(通常用 `F(n)` 表示) 形成的序列称为 斐波那契数列。该数列由 0 和 1 开始, 后面的每一项数字都是前面两项数字的和。也就是:
 * <p/>
 * F(0) = 0，F(1) = 1 <br/>
 * F(n) = F(n - 1) + F(n - 2)，其中 n > 1
 * <p/>
 * 给定 `n` , 请计算 `F(n)` 。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：n = 2 <br/>
 * 输出：1 <br/>
 * 解释：F(2) = F(1) + F(0) = 1 + 0 = 1 <br/>
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：n = 3 <br/>
 * 输出：2 <br/>
 * 解释：F(3) = F(2) + F(1) = 1 + 1 = 2
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：n = 4 <br/>
 * 输出：3 <br/>
 * 解释：F(4) = F(3) + F(2) = 2 + 1 = 3
 * <p/>
 * Copyright: Copyright (c) 2024-09-18 11:50
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class Fibonacci {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.print("请输入数字n: ");
			Scanner scanner = new Scanner(System.in);
			int n = scanner.nextInt();
			//System.out.println(fib(n));
			System.out.println(fibonacci2(n));
		}
	}

	private static int fibonacci(int n) {
		if (n < 2) {
			return n;
		}
		return fibonacci(n - 1) + fibonacci(n - 2);
	}

	public static int fib(int n) {
		int[] memo = new int[n + 1]; //备忘录全部初始化为0
		//进行备忘录的递归
		return helper(memo, n);
	}

	public static int helper(int[] memo, int n) {
		//base case
		if (n == 0 || n == 1) {
			return n;
		}
		//已经计算过, 不用再计算
		if (memo[n] != 0) {
			return memo[n];
		}
		memo[n] = helper(memo, n - 1) + helper(memo, n - 2);
		return memo[n];
	}

	/**
	 * 优化后的斐波那契算法
	 * @param n
	 * @return
	 */
	public static int fibonacci2(int n) {
		// 如果n为0，直接返回0，因为F(0) = 0
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}

		// 初始化前两个值
		long prev = 0; //f(0)
		long current = 1; //f(1)

		// 循环从2计算到n，得到F(n)
		for(int i = 2; i<=n; i++) {
			// 当前值为前两个值的和
			long next = prev + current;
			// 更新前一个值为当前值
			prev = current;
			// 更新当前值为新计算的值
			current = next;
		}

		// 返回计算结果
		return (int) current;
	}
}
