package com.loserico.java16;

import org.junit.Test;

public class TestInstanceOf {

	@Test
	public void test() {
		Object str = "Hello";
		if (str instanceof String s) {
			System.out.println(s.toUpperCase());
			s = "你好, 三少爷";
			System.out.println(s.toUpperCase());
		}
	}
}
