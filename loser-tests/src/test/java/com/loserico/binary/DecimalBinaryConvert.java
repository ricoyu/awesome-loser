package com.loserico.binary;

import java.math.BigInteger;

import org.junit.Test;

public class DecimalBinaryConvert {

	/**
	 * 十进制转二进制
	 * 
	 * @param decimal
	 * @return String
	 */
	public static String decimal2Binary(int decimal) {
		// 转换成 BigInteger 类型，默认是十进制
		BigInteger bigInteger = new BigInteger(String.valueOf(decimal));
		// 参数 2 指定的是转化成二进制
		return bigInteger.toString(2);
	}

	/**
	 * 二进制转换成十进制
	 * 
	 * @param binary
	 * @return int
	 */
	public static int binary2Decimal(String binary) {
		// 转换为 BigInteger 类型，参数 2 指定的是二进制
		BigInteger bigInteger = new BigInteger(binary, 2);
		// 默认转换成十进制
		return Integer.parseInt(bigInteger.toString());
	}

	public static void main(String[] args) {
		int a = 53;
		String b = "110101";
		// 获取十进制数 53 的二进制数
		System.out.println(String.format(" 数字 %d 的二进制是 %s", a, decimal2Binary(a)));
		// 获取二进制数 110101 的十进制数
		System.out.println(String.format(" 数字 %s 的十进制是 %d", b, binary2Decimal(b)));
	}
	
	@Test
	public void testHexToBinary() {
		String binary = new BigInteger("f", 16).toString(2);
		System.out.println(binary);
	}
}
