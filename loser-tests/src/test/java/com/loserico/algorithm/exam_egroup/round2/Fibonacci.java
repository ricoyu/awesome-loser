package com.loserico.algorithm.exam_egroup.round2;

import java.util.Scanner;

/**
 * 斐波那契数 (通常用 `F(n)` 表示) 形成的序列称为 斐波那契数列 。该数列由 0 和 1 开始, 后面的每一项数字都是前面两项数字的和。也就是:
 * <pre> {@code
 * F(0) = 0，F(1) = 1
 * F(n) = F(n - 1) + F(n - 2)，其中 n > 1
 * }</pre>
 * <p>
 * Copyright: Copyright (c) 2024-09-20 19:53
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class Fibonacci {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 5; i++) {
			System.out.print("请输入数字n: ");
			int n = scanner.nextInt();
			int[] memo = new int[n+1];
			System.out.println("结果: " + fib3(n));
		}
	}

	/**
	 * 这是自顶向下的递归解法
	 * @param memo
	 * @param n
	 * @return
	 */
	public static int fib(int[] memo, int n) {
		if (n == 0 || n == 1) {
			return n;
		}
		if (memo[n] != 0) {
			return memo[n];
		}
		memo[n] = fib(memo, n-1) + fib(memo, n-2);
		return memo[n];
	}

	/**
	 * 这是自底向上的解法
	 * @param memo
	 * @param n
	 * @return
	 */
	public static int fib2(int[] memo, int n) {
		if (n == 0 || n == 1) {
			return n;
		}

		//base case
		memo[0] = 0;
		memo[1] = 1;
		for (int i = 2; i <=n; i++) {
			memo[i] = memo[i-1] + memo[i-2];
		}

		return memo[n];
	}

	/**
	 * 这是优化后的自底向上解法, 优化了空间复杂度,
	 * 因为计算的 fib(n) 只用到了 fib(n-1) 和 fib(n-2)
	 * @param n
	 * @return
	 */
	public static int fib3(int n) {
		if (n == 0 || n == 1) {
			return n;
		}

		int prev = 0;
		int current = 1;
		for (int i = 2; i <=n ; i++) {
			int temp = current + prev;
			prev = current;
			current = temp;
		}
		return current;
	}
}
