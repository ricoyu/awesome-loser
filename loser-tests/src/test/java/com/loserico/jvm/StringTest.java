package com.loserico.jvm;

/**
 * <p>
 * Copyright: (C), 2020-08-21 8:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StringTest {
	
	public static void main(String[] args) {
		String s = "a" + "b" + "c"; //就等价于String s = "abc";
		String a = "a";
		String b = "b";
		String c = "c";
		String s1 = a + b + c;
	}
}
