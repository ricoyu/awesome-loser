package com.loserico.bitop;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2019/11/27 17:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BitAndModulusTest {
	
	@Test
	public void testBit() {
		//int number = 10000 * 10;
		int number = 10000 * 100;
		//int number = 10000 * 1000;
		//int number = 10000 * 10000;
		int a = 1;
		long start = System.currentTimeMillis();
		for (int i = number; i > 0; i++) { //直到i达到int型最大值, 大概80多亿
			a = a & i;
		}
		long end = System.currentTimeMillis();
		System.out.println("位运算耗时: " + (end - start));
	}
	
	@Test
	public void testModulus() {
		//int number = 10000 * 10;
		int number = 10000 * 100;
		//int number = 10000 * 1000;
		//int number = 10000 * 10000;
		int a = 1;
		long start = System.currentTimeMillis();
		for (int i = number; i > 0; i++) { //直到i达到int型最大值, 大概80多亿
			a %= i;
		}
		long end = System.currentTimeMillis();
		System.out.println("位运算耗时: " + (end - start));
	}
}
