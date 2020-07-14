package com.loserico.bitop;

import org.junit.Test;

public class BitTest {

	@Test
	public void testToBit() {
		System.out.println(Integer.toBinaryString(3));
		
	}
	
	@Test
	public void testbit2Int() {
		System.out.println(Integer.valueOf("11", 2));
		System.out.println(3<<2);
		System.out.println(1>>>2);
	}
	
	@Test
	public void testCapacity() {
		//00011111 11111111 11111111 11111111 11111111 
//		System.out.println(Integer.parseInt("00011111111111111111111111111111", 2));
//		System.out.println(Integer.parseInt("11100000000000000000000000000000", 2));
//		System.out.println(-1 << 29);
//		System.out.println(Integer.parseInt(Integer.toBinaryString(-1 << 29) + "", 2));
//		System.out.println(Integer.parseInt("00010000000000000000000000000000", 2));
//		System.out.println(1 << 29);
//		System.out.println(Integer.toBinaryString(1 << 29));
//		System.out.println(2<<29);
		System.out.println(3<<29);
	}
}
