package com.loserico.binary;

import com.loserico.codec.RedixUtils;
import org.junit.Test;

import java.util.Random;

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
		Random random = new Random();
		int num1 = random.nextInt(10000);
		System.out.println("num1: " +num1);
		byte[] bytes = RedixUtils.int2Bytes(num1);
		//byte[] bytes1 = RedixUtils.intToByteArray(num2);
		String binaryStr = RedixUtils.bytes2BinaryStr(bytes);
		//RedixUtils.print(bytes1);
		System.out.println(binaryStr);
		int value = RedixUtils.binaryStr2Int(binaryStr);
		System.out.println("还原成int: "+value);
	}
	
}
