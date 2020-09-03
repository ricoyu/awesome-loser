package com.loserico.jvm.primitives;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-08-21 9:07
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ObjectPoolTest {
	
	@Test
	public void test() {
		Integer i = 127;
		Integer j = 127;
		System.out.println(i == j);
		
		Integer k = 128;
		Integer l = 128;
		System.out.println(k == l);
	}
}
