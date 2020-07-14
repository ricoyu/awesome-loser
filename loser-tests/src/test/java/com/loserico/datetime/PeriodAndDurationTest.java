package com.loserico.datetime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

/**
 * https://www.mkyong.com/java8/java-8-period-and-duration-examples/
 * Few examples to show you how to use Java 8 Duration, Period and ChronoUnit objects to find out the difference between dates.
 * <ul>
 * <li>Duration – Measures time in seconds and nanoseconds.
 * <li>Period – Measures time in years, months and days.
 * </ul>
 * <p>
 * Copyright: Copyright (c) 2017-10-01 08:47
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class PeriodAndDurationTest {

	/**
	 * A java.time.Duration example to find out difference seconds between two LocalDateTime
	 * @on
	 */
	@Test
	public void testDuration() {
		Duration oneHours = Duration.ofHours(1);
		System.out.println(oneHours.getSeconds() + " seconds");

		Duration oneHours2 = Duration.of(1, ChronoUnit.HOURS);
		System.out.println(oneHours2.getSeconds() + " seconds");

		// Test Duration.between
		System.out.println("\n--- Duration.between --- ");

		LocalDateTime oldDate = LocalDateTime.of(2016, Month.AUGUST, 31, 10, 20, 55);
		LocalDateTime newDate = LocalDateTime.of(2016, Month.NOVEMBER, 9, 10, 21, 56);

		System.out.println(oldDate);
		System.out.println(newDate);

		//count seconds between dates
		Duration duration = Duration.between(oldDate, newDate);
		System.out.println(duration.getSeconds() + " seconds");
	}

	/**
	 * A java.time.Period example to find out differently (years, months, days)
	 * between two LocalDates
	 */
	@Test
	public void testPeriod() {
		System.out.println("--- Examples --- ");

		Period tenDays = Period.ofDays(10);
		System.out.println(tenDays.getDays());

		Period oneYearTwoMonthsThreeDays = Period.of(1, 2, 3);
		System.out.println(oneYearTwoMonthsThreeDays.getYears());
		System.out.println(oneYearTwoMonthsThreeDays.getMonths());
		System.out.println(oneYearTwoMonthsThreeDays.getDays());

		System.out.println("\n--- Period.between --- ");
		LocalDate oldDate = LocalDate.of(1982, Month.NOVEMBER, 9);
		LocalDate newDate = LocalDate.of(2017, Month.OCTOBER, 1);

		System.out.println(oldDate);
		System.out.println(newDate);

		// check period between dates
		Period period = Period.between(oldDate, newDate);
		System.out.println(period.getYears() + " years");
		System.out.println(period.getMonths() + " months");
		System.out.println(period.getDays() + " days");
	}

	@Test
	public void testChronoUnit() {
		LocalDateTime oldDate = LocalDateTime.of(1982, Month.NOVEMBER, 9, 11, 59, 59);
		LocalDateTime newDate = LocalDateTime.of(2017, Month.OCTOBER, 1, 9, 05, 10);

		System.out.println(oldDate);
		System.out.println(newDate);

		long years = ChronoUnit.YEARS.between(oldDate, newDate);
		long months = ChronoUnit.MONTHS.between(oldDate, newDate);
		long weeks = ChronoUnit.WEEKS.between(oldDate, newDate);
		long days = ChronoUnit.DAYS.between(oldDate, newDate);
		long hours = ChronoUnit.HOURS.between(oldDate, newDate);
		long minutes = ChronoUnit.MINUTES.between(oldDate, newDate);
		long seconds = ChronoUnit.SECONDS.between(oldDate, newDate);
		long milis = ChronoUnit.MILLIS.between(oldDate, newDate);
		long nano = ChronoUnit.NANOS.between(oldDate, newDate);

		System.out.println("\n--- Total --- ");
		System.out.println(years + " years");
		System.out.println(months + " months");
		System.out.println(weeks + " weeks");
		System.out.println(days + " days");
		System.out.println(hours + " hours");
		System.out.println(minutes + " minutes");
		System.out.println(seconds + " seconds");
		System.out.println(milis + " milis");
		System.out.println(nano + " nano");
	}
}
