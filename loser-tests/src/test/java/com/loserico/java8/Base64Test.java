package com.loserico.java8;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <p>
 * Copyright: (C), 2020/6/12 10:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Base64Test {
	
	@Test
	public void testBase64InJava8() {
		String text = "Base64 finally in Java 8!";
		String encodered = Base64.getEncoder()
				.encodeToString(text.getBytes(StandardCharsets.UTF_8));
		System.out.println(encodered);
		
		String decodered = new String(Base64.getDecoder().decode(encodered), StandardCharsets.UTF_8);
		System.out.println(decodered);
	}
}
