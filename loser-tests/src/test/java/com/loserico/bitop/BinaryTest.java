package com.loserico.bitop;

import org.junit.Test;

/**
 * 1 1 2 10 3 11 4 100 5 101 6 110 7 111 8 1000 9 1001 10 1010
 * 
 * <p>
 * Copyright: Copyright (c) 2019-03-19 13:27
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * 
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class BinaryTest {

	@Test
	public void testBitwise() {
		int a = 5;
		int b = 7;

		/*
		 * bitwise and 
		 * 0101 & 0111=0101 = 5
		 * @on
		 */
		System.out.println("a&b = " + (a & b));

		// 101 | 111 = 7
		System.out.println("a|b = " + (a | b));

		// 101 ^ 111 = 2
		System.out.println("a^b = " + (a ^ b));

		// bitwise and
		// ~0101=1010
		// will give 2's complement of 1010 = -6
		System.out.println("~a = " + (~a));

		// can also be combined with
		// assignment operator to provide shorthand
		// assignment
		// a=a&b
		a &= b;
		System.out.println("a = " + a);
	}
	
	@Test
	public void test() {
		int countBits = Integer.SIZE - 3;
		int running = -1 << countBits;
		System.out.println(Integer.toBinaryString(-1));
		System.out.println(Integer.toBinaryString(running));
		System.out.println(Integer.toBinaryString(-3));
	}
}
