package com.loserico.atomic;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntegerCacheTest {

	@Test
	public void test() {
		Integer i = Integer.valueOf(128);
		int j = 128;
		Integer k = 128;
		assertTrue(i == j);
		assertTrue(k == j);
		assertFalse(i == k);
	}
}
