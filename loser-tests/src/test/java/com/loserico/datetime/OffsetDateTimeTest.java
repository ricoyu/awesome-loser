package com.loserico.datetime;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * The OffsetDateTime is an immutable representation of a date-time with an offset.
 * This class stores all date and time fields, to a precision of nanoseconds, as well as the offset from UTC/Greenwich.
 * <p>
 * Copyright: (C), 2020-12-10 9:46
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OffsetDateTimeTest {
	
	@Test
	public void testZoneOffset() {
		//2015-2-20 6:30
		LocalDateTime localDateTime = LocalDateTime.of(2015, Month.FEBRUARY, 20, 06, 30);
		ZoneOffset zoneOffset = ZoneOffset.of("+08:00");
		OffsetDateTime offsetByTwo = OffsetDateTime.of(localDateTime, zoneOffset);
		System.out.println(offsetByTwo);
	}
}
