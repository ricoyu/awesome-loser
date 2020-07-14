package com.loserico.datetime;

import java.time.LocalDate;

import org.junit.Test;

public class LeapYearTest {

	@Test
	public void testLeapYear() {
		LocalDate now = LocalDate.of(2018, 11, 8);
		System.out.println(now.isLeapYear());
	}
}
