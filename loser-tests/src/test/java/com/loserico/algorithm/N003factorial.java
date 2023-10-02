package com.loserico.algorithm;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2023-09-08 9:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class N003factorial {
	
	/**
	 * 给定一个参数N,返回1!+2!+3!+...+N！(感叹号是阶乘的意思)的结果
	 */
	@Test
	public void testFactorial() {
		System.out.println(factorialSum(2));
	}
	
	public int factorialSum(int n) {
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		
		int sum= 0;
		for (int i = 0; i <=n; i++) {
			sum+=factorial(i);
		}
		return sum;
	}
	

	/**
	 * 返回n的阶乘
	 * 
	 * 阶乘就是:
	 * <ul>
	 *     <li/>1的阶乘是 1
	 *     <li/>2的阶乘是 1 * 2
	 *     <li/>3的阶乘是 1*2*3
	 *     <li/>4的阶乘是 1*2*3*4
	 * </ul>
	 * @param n
	 * @return
	 */
	public static int factorial(int n) {
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		return factorial(n-1) * n;
	}
}
