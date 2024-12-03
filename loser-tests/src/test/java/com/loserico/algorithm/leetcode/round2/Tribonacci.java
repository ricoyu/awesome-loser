package com.loserico.algorithm.leetcode.round2;

import java.util.Scanner;

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
			return 2;
		}

		int t0 = 0, t1 = 1, t2 = 1;
		int current = 0;
		for (int i = 3; i <= n; i++) {
			current = t0 + t1 + t2;
			t0 = t1;
			t1 = t2;
			t2 = current;
		}

		return current;
	}
}
