package com.loserico.java8;

import org.junit.Test;

import java.time.LocalDateTime;

/**
 * <p>
 * Copyright: (C), 2020-08-14 11:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalDatetimeTest {
	
	@Test
	public void testString2LocalDateTime() {
		String s = "Fri Aug 14 2020 11:24:06 GMT+0800";
		LocalDateTime localDateTime = LocalDateTime.parse(s);
		System.out.println(localDateTime);
	}
}
