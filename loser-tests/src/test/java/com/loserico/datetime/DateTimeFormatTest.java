package com.loserico.datetime;

import static java.time.format.DateTimeFormatter.*;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.Test;

public class DateTimeFormatTest {

	@Test
	public void testFormat2Month() {
		System.out.println(LocalDate.now().format(ofPattern("MMM", Locale.US)));
		System.out.println(LocalDate.of(2018, 4, 1).format(ofPattern("MMM", Locale.US)));
	}
}
