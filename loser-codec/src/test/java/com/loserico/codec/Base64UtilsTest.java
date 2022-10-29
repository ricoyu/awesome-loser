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
		//String encode = Base64Utils.encode("{\"code\":0,\"data\":{},\"error\":{},\"extra\":{\"cost\":0.012025283,\"request-id\":\"8fc10a982e575318b4f7cacc2cd7800a@2@infoq\"}}");
		//System.out.println(encode);
		//String source = Base64Utils.decode(encode, encode);
		//System.out.println(source);
		
		System.out.println(Base64Utils.encode("123456"));
	}
}
