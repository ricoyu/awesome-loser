package com.loserico.bitop;

public class URShift {
	
	public static void main(String[] args) {
		int i = -1;
		System.out.println(Integer.toBinaryString(i)); //11111111111111111111111111111111
		i >>>= 10;
		System.out.println(Integer.toBinaryString(i)); //1111111111111111111111
		long l = -1;
		System.out.println(Long.toBinaryString(l)); //1111111111111111111111111111111111111111111111111111111111111111
		l >>>= 10;
		System.out.println(Long.toBinaryString(l)); //111111111111111111111111111111111111111111111111111111
		
		System.out.println("=========== short =============");
		short s = -1;
		System.out.println(Integer.toBinaryString(s)); //11111111111111111111111111111111
		System.out.println(Integer.toBinaryString(s>>>10));//1111111111111111111111 
		s >>>= 10;
		System.out.println(Integer.toBinaryString(s)); //11111111111111111111111111111111 错误的结果, 是int -1
		
		System.out.println("=========== byte =============");
		byte b = -1;
		System.out.println(Integer.toBinaryString(b)); //11111111111111111111111111111111
		b >>>= 10;
		System.out.println(Integer.toBinaryString(b)); //11111111111111111111111111111111
		b = -1;
		System.out.println(Integer.toBinaryString(b)); //11111111111111111111111111111111
		System.out.println(Integer.toBinaryString(b >>> 10)); //1111111111111111111111
		
		System.out.println("=============================");
		int n = 1000;
		System.out.println("1000的二进制" + Integer.toBinaryString(n)); //1000的二进制1111101000
		System.out.println("1000右移3位" + Integer.toBinaryString((n>>3))); //1000右移3位1111101
		n = -1000;
		System.out.println("-1000的二进制" + Integer.toBinaryString(n)); //-1000的二进制11111111111111111111110000011000
		System.out.println("-1000右移3位" + Integer.toBinaryString((n>>3))); //-1000右移3位11111111111111111111111110000011
		
		System.out.println("=============================");
		
	}
}