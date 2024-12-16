package com.loserico.general;

import org.junit.Test;

public class SwitchTest {

	@Test
	public void test() {
		String x = "3";
		int i = switch (x) {
			case "1":
				yield 1;
			case "2":
				yield 2;
			default :
				yield 3;
		};
		System.out.println(i);
	}
}
