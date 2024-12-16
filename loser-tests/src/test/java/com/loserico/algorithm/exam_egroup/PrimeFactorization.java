package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 题目描述: <p/>
 * RSA加密算法在网络安全世界中无处不在，它利用了极大整数因数分解的困难度，数据越大，安全系数越高，给定一个 32 位正整数，请对其进行因数分解，找出是哪两个素数的乘积。
 * <p/>
 * 输入描述: <br/>
 * 一个正整数 num 0 < num < 2147483647
 * <p/>
 * 输出描述: <br/>
 * 如果成功找到，以单个空格分割，从小到大输出两个素数，分解失败，请输出-1, -1
 * <p/>
 * <p>
 * 用例: <p/>
 * 输入: 15
 * <br/>
 * 输出: 3 5
 * <p/>
 * 输入: 27
 * <br/>
 * 输出: -1 -1
 * <p/>
 * Copyright: Copyright (c) 2024-09-26 11:04
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PrimeFactorization {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字n: ");
		int n = scanner.nextInt();
		int[] primeFactors = findPrimeFactors(n);
		for (int i = 0; i < primeFactors.length; i++) {
			System.out.print(primeFactors[i] + " ");
		}
		System.out.println();
	}

	public static int[] findPrimeFactors(int num) {
		// 从小到大寻找因数
		for (int i = 2; i <= Math.sqrt(num); i++) {
			if (num % i == 0) { // 如果i是num的因数
				int antherFactor = num / i; // 另一个因数
				// 检查这两个因数是否都是素数
				if (isPrime(i) && isPrime(antherFactor)) {
					return new int[]{Math.min(i, antherFactor), Math.max(i, antherFactor)};
				}
			}
		}
		// 如果没有找到符合条件的因数，返回-1
		return new int[]{-1, -1};
	}

	/**
	 * 判断一个数是否为素数
	 *
	 * @param n
	 * @return
	 */
	public static boolean isPrime(int n) {
		if (n < 1) {
			return false;
		}

		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0) { // 如果能被其他数整除，不是素数
				return false;
			}
		}
		return true; // 经过检查，确定是素数
	}
}
