package com.loserico;

import org.junit.Test;

public class RuntimeTest {

	@Test
	public void testCPUCores() {
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}
