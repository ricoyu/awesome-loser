package com.loserico;

import org.junit.Test;

public class HexaConvertTest {

	@Test
	public void testHexa2Decimal() {
		System.out.println(Integer.valueOf("A5", 16)); //B1 包头
		System.out.println(Integer.valueOf("AF", 16)); //B4 身高		175
		System.out.println(Integer.valueOf("1E", 16)); //B5 年龄性别	30
		System.out.println(Integer.valueOf("11", 16)); //B6 年份		17
		System.out.println(Integer.valueOf("06", 16)); //B7 月份		6
		System.out.println(Integer.valueOf("01", 16)); //B8 日		1
		System.out.println(Integer.valueOf("0C", 16)); //B9 时		12
		System.out.println(Integer.valueOf("1E", 16)); //B10 分		30
		System.out.println(Integer.valueOf("0F", 16)); //B11 秒		15
	}
	
	@Test
	public void testHexa2Decimal2() {
		System.out.println(Integer.valueOf("5A", 16)); //B1 包头
		System.out.println(Integer.valueOf("188D", 16)); 
	}
	
	@Test
	public void testDecimal2HexString() {
		System.out.println("身高:" + Integer.toHexString(175).toUpperCase());
		System.out.println(Integer.toHexString(384).toLowerCase());
	}
	
	@Test
	public void test() {
		System.out.println(Integer.toHexString(8736));
		System.out.println(Integer.toHexString(8737));
		System.out.println(Integer.toHexString(15993));
	}
}
