package com.loserico.codec;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-09-18 9:56
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Base64UtilsTest {
	
	@Test
	public void testDecode() {
		String decode = Base64Utils.decode("/5cBAAABAAAAAAAACDVlMjZlNGJlA25zMQdkbnN0ZXN0A2NvbQAAAQAB");
		System.out.println(decode);
	}
	
	@Test
	public void testEncode() {
		String encode = Base64Utils.encode("  ");
		System.out.println(encode);
		String source = Base64Utils.decode(encode);
		System.out.println(source);
	}
}
