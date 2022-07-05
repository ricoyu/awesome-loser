package com.loserico.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-07-01 18:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Fabonacci {
	
	/**
	 * 1 1 2 3 5 8 13 21 34
	 * f(n) = f(n-1) + f(n-2)
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(i + ":" + fab(i));
		}
	}
	
	public static int fab(int n) {
		if (n <= 2) {
			return 1; //递归的终止条件
		}
		return fab(n - 1) + fab(n - 2);
	}
}
