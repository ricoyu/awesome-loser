package com.loserico.datetime;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * <p>
 * Copyright: (C), 2020/4/22 11:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ZonedDateTimeTest {
	
	@Test
	public void testZonedDatetime() {
		System.out.println(ZonedDateTime.now());
		System.out.println(LocalDateTime.now());
	}
}
