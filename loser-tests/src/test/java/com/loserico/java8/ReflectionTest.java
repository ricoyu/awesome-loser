package com.loserico.java8;

import org.junit.Test;

public class ReflectionTest {

	@Test
	public void testReflectiveOperationException() {
		try {
			Class<?> clazz = Class.forName("com.biezhi.apple.User");
			clazz.getMethods()[0].invoke("");
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
}
