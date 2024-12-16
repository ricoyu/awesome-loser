package com.loserico.jdk12;

import org.junit.Test;

public class TestStringIndent {

	@Test
	public void test() {
		String result = "Java\nGolang\nMCA".indent(3);
		System.out.println(result);
	}
}
