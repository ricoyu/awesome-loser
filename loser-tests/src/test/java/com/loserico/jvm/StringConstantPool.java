package com.loserico.jvm;

import org.junit.Test;

public class StringConstantPool {

	@Test
	public void testStrEqual() {
		String str1 = "hello";
		String str2 = "hello";
		System.out.println("str1 == str2: " + (str1 == str2));
	}

	@Test
	public void testStrIntern() {
		String s1 = "Hello";
		String s2 = new StringBuffer("He").append("llo").toString();
		String s3 = s2.intern();

		// Determine which strings are equivalent using the == operator
		System.out.println("s1 == s2? " + (s1 == s2)); // false
		System.out.println("s1 == s3? " + (s1 == s3)); // true
	}

	@Test
	public void testEqual() {
		String m = "hello,world";
		String n = "hello,world";
		String u = new String(m);
		String v = new String("hello,world");

		System.out.println(m == n); // true
		System.out.println(m == u.intern()); // true
		System.out.println(m == u); // false
		System.out.println(m == v.intern()); // true
		System.out.println(m == v); // false
		System.out.println(u.intern() == v.intern()); // true
		System.out.println(u == v); // false
	}
}
