package com.loserico.jvm;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2023-02-22 8:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StringConstantPoolTest {
	
	@Test
	public void test() {
		String aa = "ab";
		String bb = "ab";
		assertTrue(aa == bb);
	}
	
	@Test
	public void test2() {
		String aa = "ab";
		String bb = new String("ab");
		assertThat(aa == bb).isFalse();
	}
}
