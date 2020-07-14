package com.loserico.datetime;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @of
 * The easiest way to convert a java.util.Date to java.time.LocalDate is via Instant,
 * which is the equivalent class of java.util.Date in JDK 8. You can first convert
 * util Date to Instant and then create a LocalDateTime object from that instant at
 * your default timezone. Once you have an instant of LocalDateTime, you can easily
 * convert that to LocalDate or LocalTime in Java.
 * 
 * Here are the steps you need to follow to perform this conversion:
 * 	- Convert java.util.Date to java.time.Instant class
 * 	- Convert java.time.Instant to java.time.LocalDateTime using System's default timezone.
 * 	- Convert java.time.LocalDateTime to java.time.LocalDate in Java
 * 
 * It's easy to remember these steps once you are familiar with new Date and Time API
 * and how it is related to old date API.
 * 
 * For example, the equivalent class of java.util.Date in new API is Instant, hence
 * you will find the toInstant() method in java.util.Date class.
 * 
 * Just like old classes was a millisecond value from Epoch, Instant is also an
 * instant in time scale but it doesn't care about time zone but LocalDatetime class
 * uses local Timezone.
 * 
 * That's why when you convert an Instant to LocalDateTime, it needed a timezone. The
 * LocalDateTime class has both date and time component, so if you just need date
 * part, you can use the toLocalDate() method to convert LocalDatetTime to LocalDate
 * 
 * @on
 * @author Rico Yu ricoyu520@gmail.com
 * @since 2017-04-03 20:14
 * @version 1.0
 *
 */
public class Date2LocalDateTest {

	@Test
	public void testDate2LocalDate() {
		Date date = new Date();
		Instant instant = date.toInstant();
		//		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate localDate = localDateTime.toLocalDate();
		System.out.println("java.util.Date: " + date);
		System.out.println("java.time.LocalDate: " + localDate);
	}
}
