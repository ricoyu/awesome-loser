package com.loserico.algorithm;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2022-06-24 9:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CheckTwoRound {
	
	@Test
	public void test1() {
		System.out.println(isTwoRound(10));
	}
	
	public static boolean isTwoRound(int n) {
		return (n & (n - 1)) == 0;
	}
	
	@Test
	public void test2() {
		int n = Integer.MAX_VALUE;
		int i = 1;
		while (i <= n) {
			i = i * 2;
		}
	}
	
	@Test
	public void test3() {
		int a = 1;
		int n = Integer.MAX_VALUE;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				a = a + 1;
			}
		}
	}
}
