package com.loserico.datetime;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * <p>
 * Copyright: (C), 2020-12-10 9:38
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ZoneIdTest {
	
	@Test
	public void testGetSpecificZone() {
		System.out.println(ZoneId.of("Europe/Paris"));
	}
	
	@Test
	public void testAllZoneIds() {
		Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
		availableZoneIds.stream()
				.sorted()
				.forEach(System.out::println);
	}
	
	@Test
	public void testLocalDateTime2ZonedDateTime() {
		//代表法国巴黎时间 2020-12-10T09:44:22+01:00[Europe/Paris]
		ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2020, 12, 10, 9, 44, 22), ZoneId.of("Europe/Paris"));
		System.out.println(zonedDateTime);
	}
}

