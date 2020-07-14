package com.loserico.datetime;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * Java8的日期时间API与旧版相比是线程安全的
 * <p>
 * Copyright: Copyright (c) 2018-11-08 17:09
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class WeekTest {

	@Test
	public void testCurrentWeek() {
		LocalDate date = LocalDate.now();
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
		int weekNumber = date.get(woy);
		System.out.println(weekNumber);
	}
	
	@Test
	public void testDayOfWeek() {
		DayOfWeek dayOfWeek = LocalDate.of(2018, 11, 8).getDayOfWeek();
		System.out.println(dayOfWeek);
	}
}
