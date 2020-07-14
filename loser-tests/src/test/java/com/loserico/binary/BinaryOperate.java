package com.loserico.binary;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BinaryOperate {

	/**
	 * 获取byte某一位上的数字
	 * 
	 * Right shifting b by position will make bit #position be in the furthest spot to the right in
	 * the number. Combining that with the bitwise AND & with 1 will tell you if the bit is set.
	 * 
	 * @param b        byte数字
	 * @param position 要获取的位置, 右边第一个数字的position为0, 往左一位是1, 依次类推
	 * @return int 该位上的数字, 1 或 0
	 */
	public static int getByte(byte b, int position) {
		return (b >> (position)) & 1;
	}

	public static void main(String[] args) {
		int oneMillion = 100_0000;
		int oneMillion2 = 1_000_000;
		System.out.println(oneMillion == oneMillion2);

		byte b = -0b1000_0000; // -128
		byte b2 = 0b111_1111; // 127 = 64 + 32 + 16 + 8 + 4+ 2 + 1
	}

	@Test
	public void testGetByte() {
		byte b = 0b1010101;
		assertTrue(getByte(b, 7) == 1);
		assertTrue(getByte(b, 6) == 0);
		assertTrue(getByte(b, 1) == 1);
	}

	/**
	 * if n is an integer variable, then gives you a 1 if the fourth bit from the right in the
	 * binary representation of n is 1, and 0 otherwise.
	 */
	@Test
	public void test1() {
		byte n = 0b1000;
		int fourthBitFromRight = (n & 0b1000) / 0b1000;
		// System.out.println(fourthBitFromRight);

		byte n2 = 0b1010;
		int fourthBitFromRight2 = (n & (1 << 3)) >> 3; // 检查第四位是否为1
		// System.out.println(fourthBitFromRight2);
		System.out.println((n2 >> 2) & 1); // 检查第三位是否为1
	}

	@Test
	public void testLeftShift() {
		byte n = 0b11_0101;
//		byte n = 0b110101;
		System.out.println(((Byte) n).intValue());

		System.out.println(0b110101 << 0);

		int value = n << 1;
		System.out.println(value);

		int intValue = (int) n;
		System.out.println(intValue);
	}

	@Test
	public void testName() {
		byte b = (byte) -128;
		byte b2 = (byte) 0b10000000;
		byte b3 = (byte) 10000000;
		String s = "0b" + ("0000000" + Integer.toBinaryString(0xFF & b)).replaceAll(".*(.{8})$", "$1");
		System.out.println(s);
		System.out.println(b);
		System.out.println(b2);
		System.out.println(b3);
	}

	@Test
	public void testRightShift() {
		byte b = 0b110101;
		System.out.println(b);
		System.out.println(b >> 1);

		int i = 1024;
		System.out.println(i >> 1);
		System.out.println(i >> 2);
		System.out.println(i >> 3);
		System.out.println(i >> 4);
		System.out.println(i >> 5);
	}

	@Test
	public void testIntRange() {
		int intMin = -2147483648;
		System.out.println(Integer.toBinaryString(intMin));
		int intMax = 2147483647;
		System.out.println(Integer.toBinaryString(intMax));
		int byteMin = -128;
		System.out.println(Integer.toBinaryString(byteMin));
		int byteMax = 127;
		System.out.println(Integer.toBinaryString(byteMax));
		long longMin = -9223372036854775808L;
		System.out.println(Long.toBinaryString(longMin));
		long longMax = 9223372036854755807L;
		System.out.println(Long.toBinaryString(byteMin));
		long byteMinLong = -128L;
		System.out.println(Long.toBinaryString(byteMinLong));
		long byteMaxLong = 127L;
		System.out.println(Long.toBinaryString(byteMaxLong));
	}

	@Test
	public void testBitwiseComplement() {
		int i = 6;
		System.out.println("6的二进制形式" + Integer.toBinaryString(i));
		System.out.println("6按位取反后的整数值" + ~i + " 二进制形式" + Integer.toBinaryString(~i));
		
		System.out.println(~9);
		System.out.println(~2);
		System.out.println(~10);
		System.out.println(~11);
		System.out.println(~12);
		
		System.out.println(~-1);
		System.out.println(~-10);
		System.out.println(~-11);
		System.out.println(~-12);
		
		System.out.println(Integer.toBinaryString(-1));
		System.out.println(Integer.toBinaryString(-2));
	}

}
