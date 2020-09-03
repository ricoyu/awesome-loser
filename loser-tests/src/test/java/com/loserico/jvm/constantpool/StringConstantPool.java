package com.loserico.jvm.constantpool;

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
	public void testStrInternEqual() {
		String s1 = new String("hi") + new String("llo");
		String s2 = s1.intern();
		System.out.println(s1 == s2);
	}
	
	@Test
	public void test() {
		String s0 = "ricoyu";
		String s1 = "ricoyu";
		String s2 = "rico" + "yu";
		System.out.println(s0 == s1);
		System.out.println(s1 == s2);
	}
	
	@Test
	public void test2() {
		String s0 = "ricoyu";
		String s1 = new String("rico") + new String("rico");
		String s2 = "rico" + new String("yu");
		System.out.println(s0 == s1);
		System.out.println(s1 == s2);
	}
	
	@Test
	public void testJavaStr() {
		String s1 = new String("ja") + new String("va");
		String s2 = s1.intern();
		System.out.println(s1 == s2);
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
