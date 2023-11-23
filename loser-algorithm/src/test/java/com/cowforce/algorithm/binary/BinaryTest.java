package com.cowforce.algorithm.binary;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2022-12-16 14:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BinaryTest {
	
	@Test
	public void testSwap() {
		int a = 3;
		int b = 4;
		System.out.println("a=" + a);
		System.out.println("b=" + b);
		System.out.println("===============");
		a = a ^ b;
		b = a ^ b;
		a = a ^ b;
		System.out.println("a=" + a);
		System.out.println("b=" + b);
	}
}
