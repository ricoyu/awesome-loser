package com.loserico.java14;

import org.junit.Test;

public class TestInstanceOf {

	@Test
	public void test() {
		Object obj = "hello";
		if (obj instanceof String str) {
			System.out.println(str);
		} else {
			System.out.println("not a string");
		}
	}

	@Test
	public void test2() {
		Object obj = "hello java";
		// 这里做的是取反运算
		if (!(obj instanceof String str)) {
			//System.out.println("not a String");
			//			System.out.println(str);// 这里不能使用str
		} else {
			System.out.println(str);// 这里可以使用str
		}
	}
}
