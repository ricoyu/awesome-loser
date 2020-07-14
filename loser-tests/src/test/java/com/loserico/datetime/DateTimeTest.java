package com.loserico.datetime;

import org.junit.Test;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static java.text.MessageFormat.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.format.FormatStyle.FULL;
import static java.time.format.FormatStyle.LONG;
import static java.time.format.FormatStyle.MEDIUM;
import static java.time.format.FormatStyle.SHORT;
import static org.junit.Assert.assertEquals;

/**
 * Issues with the Existing Date/Time APIs
 * 
 * Thread Safety – The Date and Calendar classes are not thread safe, leaving
 * developers to deal with the headache of hard to debug concurrency issues and to
 * write additional code to handle thread safety. On the contrary the new Date and
 * Time APIs introduced in Java 8 are immutable and thread safe, thus taking that
 * concurrency headache away from developers.
 * 
 * APIs Design and Ease of Understanding – The Date and Calendar APIs are poorly
 * designed with inadequate methods to perform day-to-day operations. The new
 * Date/Time APIs is ISO centric and follows consistent domain models for date, time,
 * duration and periods. There are a wide variety of utility methods that support the
 * commonest operations.
 * 
 * ZonedDate and Time – Developers had to write additional logic to handle timezone
 * logic with the old APIs, whereas with the new APIs, handling of timezone can be
 * done with Local and ZonedDate/Time APIs.
 * 
 * http://www.baeldung.com/java-8-date-time-intro <p> Copyright: Copyright (c)
 * 2018-03-06 11:02 <p> Company: DataSense <p>
 * 
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 * 
 */
public class DateTimeTest {

	@Test
	public void testString2LocalDate() {
		String dateStr = "1/1/2018";
//		System.out.println(LocalDate.parse(dateStr, ofPattern("d/M/yyyy")));
		System.out.println(LocalDate.parse(dateStr, ofPattern("dd/MM/yyyy")));
	}
	/**
	 * Let us take a look on key classes and examples of their usages. The first class
	 * is Clock which provides access to the current instant, date and time using a
	 * time-zone. Clock can be used instead of System.currentTimeMillis() and
	 * TimeZone.getDefault().
	 */
	@Test
	public void testGetSystemClock() {
		/*
		 * Clock clock = Clock.systemUTC(); System.out.println(clock.instant());
		 * System.out.println(clock.millis());
		 */

		Clock clock = Clock.systemDefaultZone();
		System.out.println(clock);
		System.out.println(clock.instant());
		System.out.println(clock.millis());

		System.out.println(new Date(1512036571679l));
	}

	@Test
	public void testClock() {
		// Get the system default clock
		Clock clock = Clock.systemDefaultZone();

		// Get the current instant of the clock
		Instant instant1 = clock.instant();
		System.out.println(instant1);
		// Get the current instant using the clock and the Instant class
		Instant instant2 = Instant.now(clock);
		System.out.println(instant2);

		// Get the local date using the clock
		LocalDate localDate = LocalDate.now(clock);
		System.out.println(localDate);

		// Get the zoned datetime using the clock
		ZonedDateTime zonedDateTime = ZonedDateTime.now(clock);
		System.out.println(zonedDateTime);
		System.out.println(ZonedDateTime.now());

		LocalDateTime localDateTime = LocalDateTime.now(clock);
		System.out.println(localDateTime);
		System.out.println(LocalDateTime.now());
	}

	@Test
	public void testNow() {
		LocalTime lt1 = LocalTime.now();
		LocalTime lt2 = LocalTime.now(Clock.systemDefaultZone());
		LocalTime lt3 = LocalTime.now(Clock.systemUTC());
		LocalTime localTime = LocalTime.now(ZoneId.of("America/Los_Angeles"));
		System.out.println(lt1);
		System.out.println(lt2);
		System.out.println(lt3);
		System.out.println(localTime);
	}

	@Test
	public void testDefaultZoneId() {
		System.out.println(Clock.systemDefaultZone().getZone());
	}

	/**
	 * The LocalDate represents a date in ISO format (yyyy-MM-dd) without time. It can
	 * be used to store dates like birthdays and paydays.
	 */
	@Test
	public void testLocalDate() {
		Clock clock = Clock.systemUTC();
		//An instance of current date can be created from the system clock as below:
		LocalDate date = LocalDate.now();
		LocalDate localDateFromClock = LocalDate.now(clock);
		// 2016-07-10
		System.out.println(date);
		// 2016-07-10
		System.out.println(localDateFromClock);

		LocalTime localTime = LocalTime.now();
		LocalTime localTimeFromClock = LocalTime.now(clock);
		// 11:04:24.834
		System.out.println(localTime);
		// 03:04:24.834
		System.out.println(localTimeFromClock);

		//The following code snippet gets the current local date and adds one day:
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		System.out.println(tomorrow);

		//This example obtains the current date and subtracts one month. Note how it accepts an enum as the time unit:
		LocalDate previousMonthSameDay = LocalDate.of(2018, 3, 31).minus(1, ChronoUnit.MONTHS);
		System.out.println(previousMonthSameDay);

		/*
		 * In the following two code examples we parse the date “2016-06-12” and get
		 * the day of the week and the day of the month respectively. Note the return
		 * values, the first is an object representing the DayOfWeek while the second
		 * in an int representing the ordinal value of the month:
		 */
		DayOfWeek dayOfWeek = LocalDate.parse("2018-03-06").getDayOfWeek();
		int twelve = LocalDate.parse("2018-03-06").getDayOfMonth();
		System.out.println(dayOfWeek);
		System.out.println(twelve);

		//We can test if a date occurs in a leap year.
		boolean leapYear = LocalDate.parse("2018-03-06").isLeapYear();
		System.out.println("2018-03-06 is leap year: " + leapYear);

		//The relationship of a date to another can be determined to occur before or after another date:
		boolean notBefore = LocalDate.parse("2016-06-12").isBefore(LocalDate.parse("2016-06-11"));
		boolean isAfter = LocalDate.parse("2016-06-12").isAfter(LocalDate.parse("2016-06-11"));
		System.out.println(notBefore);
		System.out.println(isAfter);

		/*
		 * Date boundaries can be obtained from a given date. In the following two
		 * examples we get the LocalDateTime that represents the beginning of the day
		 * (2016-06-12T00:00) of the given date and the LocalDate that represents the
		 * beginning of the month (2016-06-01) respectively:
		 */
		LocalDateTime beginningOfDay = LocalDate.parse("2016-06-12").atStartOfDay();
		LocalDate firstDayOfMonth = LocalDate.parse("2016-06-12").with(TemporalAdjusters.firstDayOfMonth());
		System.out.println(beginningOfDay);
		System.out.println(firstDayOfMonth);
	}

	@Test
	public void testLocalDate2() {
		// Current Date
		LocalDate today = LocalDate.now();
		System.out.println("Current Date=" + today);// Current Date=2016-07-10

		// Creating LocalDate by providing input arguments
		LocalDate firstDay_2014 = LocalDate.of(2014, Month.JANUARY, 1);
		System.out.println("Specific Date=" + firstDay_2014);// Specific
																// Date=2014-01-01

		// Try creating date by providing invalid inputs
		// LocalDate feb29_2014 = LocalDate.of(2014, Month.FEBRUARY, 29);
		// Exception in thread "main" java.time.DateTimeException:
		// Invalid date 'February 29' as '2014' is not a leap year

		// Current date in "Asia/Kolkata", you can get it from ZoneId javadoc
		LocalDate todayKolkata = LocalDate.now(ZoneId.of("Asia/Kolkata"));
		System.out.println("Current Date in IST=" + todayKolkata);// Current
																	// Date in
																	// IST=2016-07-10

		// java.time.zone.ZoneRulesException: Unknown time-zone ID: IST
		// LocalDate todayIST = LocalDate.now(ZoneId.of("IST"));

		// Getting date from the base date i.e 01/01/1970
		LocalDate dateFromBase = LocalDate.ofEpochDay(365);
		System.out.println("365th day from base date= " + dateFromBase);// 365th
																		// day
																		// from
																		// base
																		// date=
																		// 1971-01-01

		LocalDate hundredDay2014 = LocalDate.ofYearDay(2014, 100);
		System.out.println("100th day of 2014=" + hundredDay2014);// 100th day
																	// of
																	// 2014=2014-04-10
	}

	@Test
	public void testLocalDate3() {
		LocalDate date = LocalDate.now();
		LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
		System.out.println(date);
		System.out.println(lastDay);
	}

	/**
	 * The LocalDateTime is used to represent a combination of date and time.
	 */
	@Test
	public void testLocalDateTime() {
		Clock clock = Clock.systemUTC();
		// Get the local date/time
		LocalDateTime datetime = LocalDateTime.now();
		LocalDateTime datetimeFromClock = LocalDateTime.now(clock);
		// 2016-07-10T11:09:44.085
		System.out.println(datetime);
		// 2016-07-10T03:09:44.085
		System.out.println(datetimeFromClock);

		LocalDateTime ldt = LocalDateTime.of(2015, Month.FEBRUARY, 20, 06, 30);
		System.out.println(ldt);

		ldt = LocalDateTime.parse("2015-02-20T06:30:00");
		System.out.println(ldt);

		/*
		 * There are utility APIs to support addition and subtraction of specific
		 * units of time like days, months, year and minutes are available. The below
		 * code samples demonstrates the usage of “plus” and “minus” methods. These
		 * APIs behave exactly like their counterparts in LocalDate and LocalTime:
		 */
		System.out.println(ldt.plusDays(1));
		System.out.println(ldt.minusHours(2));

		/*
		 * Getter methods are available to extract specific units similar to the date
		 * and time classes. Given the above instance of LocalDateTime, the below code
		 * sample will return the month February:
		 */
		System.out.println(ldt.getMonth());
		
		/*
		 * The LocalDateTime can be constructed from epoch seconds as below. The
		 * result of the below code would be a LocalDateTime representing
		 * 2016-06-13T11:34:50:
		 */
		LocalDateTime ldtUTC = LocalDateTime.ofEpochSecond(1465817690L, 0, ZoneOffset.UTC);
		LocalDateTime ldtCST = LocalDateTime.ofEpochSecond(1465817690L, 0, ZoneOffset.ofHours(+8));
		System.out.println(ldtUTC);
		System.out.println(ldtCST);
		
		LocalDateTime ldtUTCFromDateStr = LocalDateTime.parse("2016-06-13T11:34:50");
		ZonedDateTime ldtAtUTC = ldtUTCFromDateStr.atZone(ZoneOffset.UTC);
		ZonedDateTime ldtAtCST = ldtUTCFromDateStr.atZone(ZoneOffset.ofHours(+8));
		System.out.println(ldtUTCFromDateStr);
		System.out.println(ldtAtUTC);
		System.out.println(ldtAtCST);

		ZonedDateTime zonedUTC = ldtUTC.atZone(ZoneOffset.UTC);
		System.out.println(zonedUTC);
		ZonedDateTime zonedCST = ldtCST.atZone(ZoneOffset.ofHours(+8));
		System.out.println(zonedCST);
		System.out.println(zonedUTC.toInstant().toEpochMilli());
		System.out.println(zonedCST.toInstant().toEpochMilli());
		
		ZonedDateTime utc2cst = ldtUTC.atZone(ZoneOffset.ofHours(+8));
		System.out.println(utc2cst);
		System.out.println(utc2cst.toInstant().toEpochMilli());
		ZonedDateTime utc2cst2utc = utc2cst.withZoneSameInstant(ZoneOffset.UTC);
		ZonedDateTime cst2utc = ldtCST.atZone(ZoneOffset.UTC);
		ZonedDateTime cst2utc2cst = cst2utc.withZoneSameInstant(ZoneOffset.ofHours(+8));
		System.out.println(cst2utc);
		System.out.println(cst2utc.toInstant().toEpochMilli());
		System.out.println(utc2cst2utc);
		System.out.println(cst2utc2cst);

		long epochMilliUTC = zonedUTC.toInstant().toEpochMilli();
		System.out.println(epochMilliUTC);
		long epochMilliCST = zonedCST.toInstant().toEpochMilli();
		System.out.println(epochMilliCST);
		assertEquals(epochMilliUTC, epochMilliCST);

		/*
		 * The LocalDateTime can be constructed from epoch seconds as below. The
		 * result of the below code would be a LocalDateTime representing
		 * 2016-06-13T11:34:50:
		 */
		LocalDateTime ldt2 = LocalDateTime.ofEpochSecond(1465817690, 0, ZoneOffset.UTC);
		System.out.println(ldt2);
		LocalDateTime ldt3 = LocalDateTime.ofEpochSecond(1465817690, 0, ZoneOffset.ofHours(+8));
		System.out.println(ldt3);
		System.out.println(ldt2.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
		System.out.println(ldt3.atZone(ZoneOffset.ofHours(+8)).toInstant().toEpochMilli());
		
		System.out.println(ldt2.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("+8")));
	}

	/**
	 * Java 8 provides ZonedDateTime when we need to deal with time zone specific date
	 * and time. The ZoneId is an identifier used to represent different zones. There
	 * are about 40 different time zones and the ZoneId are used to represent them as
	 * follows.
	 */
	@Test
	public void testZonedDateTime() {
		Clock clock = Clock.systemUTC();
		// Get the zoned date/time
		final ZonedDateTime zonedDatetime = ZonedDateTime.now();
		final ZonedDateTime zonedDatetimeFromClock = ZonedDateTime.now(clock);
		final ZonedDateTime zonedDatetimeFromZone = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
		// 2016-07-10T11:08:08.592+08:00[Asia/Shanghai]
		System.out.println(zonedDatetime);
		// 2016-07-10T03:08:08.593Z
		System.out.println(zonedDatetimeFromClock);
		// 2016-07-09T20:08:08.594-07:00[America/Los_Angeles]
		System.out.println(zonedDatetimeFromZone);

		//In this code snippet we create a Zone for Paris:
		ZoneId zoneId = ZoneId.of("Europe/Paris");

		//A set of all zone ids can be obtained as below:
		Set<String> allZoneIds = ZoneId.getAvailableZoneIds();
		allZoneIds.forEach(System.out::println);

		//The LocalDateTime can be converted to a specific zone:
		ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), zoneId);
		System.out.println(zonedDateTime);

		//The ZonedDateTime provides parse method to get time zone specific date time:
		ZonedDateTime zdt = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]");
		System.out.println(zdt);

		ZonedDateTime zdt2 = ZonedDateTime.parse("2018-03-06T13:54:48.846+01:00[Europe/Paris]");
		System.out.println(zdt2);

		/*
		 * Another way to work with time zone is by using OffsetDateTime. The
		 * OffsetDateTime is an immutable representation of a date-time with an
		 * offset. This class stores all date and time fields, to a precision of
		 * nanoseconds, as well as the offset from UTC/Greenwich.
		 * 
		 * The OffSetDateTime instance can be created as below using ZoneOffset. Here
		 * we create a LocalDateTime representing 6:30 am on 20th February 2015:
		 */
		LocalDateTime localDateTime = LocalDateTime.of(2015, Month.FEBRUARY, 20, 06, 30);
		System.out.println(localDateTime);
		//Then we add two hours to the time by creating a ZoneOffset and setting for the localDateTime instance:
		ZoneOffset offset = ZoneOffset.of("+02:00");
		OffsetDateTime offSetByTwo = OffsetDateTime.of(localDateTime, offset);
		//We now have a localDateTime of 2015-02-20 06:30 +02:00. Now let’s move on to how to modify date and time values using the Period and Duration classes.
		System.out.println(offSetByTwo);
	}

	@Test
	public void testDuration() {
		LocalDateTime from = LocalDateTime.of(2010, Month.APRIL, 12, 9, 04, 12);
		LocalDateTime to = LocalDateTime.of(2016, Month.JULY, 10, 11, 12, 59);
		Duration duration = Duration.between(from, to);
		System.out.println(duration);
		System.out.println(duration.toDays());
		System.out.println(duration.toHours());
		System.out.println(duration.toMinutes());
	}

	@Test
	public void testInstant() {
		// Current timestamp, Z = UTC+0
		Instant timestamp = Instant.now();
		System.out.println("Current Timestamp = " + timestamp);// Current Timestamp = 2016-07-10T03:41:24.887Z

		// Instant from timestamp
		Instant specificTime = Instant.ofEpochMilli(timestamp.toEpochMilli());

		System.out.println("Specific Time = " + specificTime);// Specific Time = 2016-07-10T03:41:24.887Z

		Instant instantBefore1970 = Instant.ofEpochMilli(-86400000);
		System.out.println(instantBefore1970);
		System.out.println(LocalDateTime.ofInstant(instantBefore1970, ZoneOffset.UTC));
		System.out.println(LocalDateTime.ofInstant(instantBefore1970, ZoneOffset.ofHours(8)));

		// Japan = UTC+9
		ZonedDateTime jpTime = Instant.now().atZone(ZoneId.of("Asia/Tokyo"));
		System.out.println("ZonedDateTime : " + jpTime);
		ZoneOffset offset = jpTime.getOffset();
		System.out.println("OffSet : " + offset);

		//Convert the Japan ZonedDateTime UTC+9 back to a Instant UTC+0/Z, review the result, the offset is taken care automatically.
		LocalDateTime dateTime = LocalDateTime.of(2016, Month.AUGUST, 18, 6, 57, 38);
		System.out.println(dateTime);
		ZonedDateTime zdt = dateTime.atZone(ZoneId.of("Asia/Tokyo"));
		System.out.println("ZonedDateTime : " + zdt);
		
		// Convert to instant UTC+0/Z , java.time helps to reduce 9 hours
        Instant instant = zdt.toInstant();
        System.out.println("Instant : " + instant);
	}

	@Test
	public void testDateTimeUtilities() {
		LocalDate today = LocalDate.now();

		// Get the Year, check if it's leap year
		// Year 2016 is Leap Year? true
		System.out.println("Year " + today.getYear() + " is Leap Year? " + today.isLeapYear());

		// Compare two LocalDate for before and after
		// Today is before 01/01/2015? false
		System.out.println("Today is before 01/01/2015? " + today.isBefore(LocalDate.of(2015, 1, 1)));

		// Create LocalDateTime from LocalDate
		// Current Time=2016-07-10T11:46:58.305
		System.out.println("Current Time=" + today.atTime(LocalTime.now()));

		// plus and minus operations
		// 10 days after today will be 2016-07-20
		System.out.println("10 days after today will be " + today.plusDays(10));
		// 3 weeks after today will be 2016-07-31
		System.out.println("3 weeks after today will be " + today.plusWeeks(3));
		// 20 months after today will be 2018-03-10
		System.out.println("20 months after today will be " + today.plusMonths(20));

		// 10 days before today will be 2016-06-30
		System.out.println("10 days before today will be " + today.minusDays(10));
		// 3 weeks before today will be 2016-06-19
		System.out.println("3 weeks before today will be " + today.minusWeeks(3));
		// 20 months before today will be 2014-11-10
		System.out.println("20 months before today will be " + today.minusMonths(20));

		// Temporal adjusters for adjusting the dates
		// First date of this month= 2016-07-01
		System.out.println("First date of this month= " + today.with(TemporalAdjusters.firstDayOfMonth()));
		LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
		// Last date of this year= 2016-12-31
		System.out.println("Last date of this year= " + lastDayOfYear);

		Period period = today.until(lastDayOfYear);
		// Period Format= P5M21D
		System.out.println("Period Format= " + period);
		// Months remaining in the year= 5
		System.out.println("Months remaining in the year= " + period.getMonths());
	}

	@Test
	public void testFormat() {
		LocalDateTime localDateTime = LocalDateTime.of(2015, Month.JANUARY, 25, 6, 30);
		String localDate = localDateTime.format(DateTimeFormatter.ISO_DATE);
		System.out.println(localDate);

		localDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		System.out.println(localDate);

		/*
		 * We can pass in formatting style either as SHORT, LONG or MEDIUM as part of
		 * the formatting option. The below code sample would give an output
		 * representing LocalDateTime in 25-Jan-2015 06:30:00:
		 */
		String fmt = localDateTime
				.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.UK));
		System.out.println(fmt);
		System.out.println("====================");
		// Format examples
		LocalDate date = LocalDate.now();
		// Default format of LocalDate=2016-07-10
		System.out.println("Default format of LocalDate=" + date);
		// specific format 2016-07-10
		System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		// 20160710
		System.out.println(date.format(DateTimeFormatter.BASIC_ISO_DATE));
		LocalDateTime dateTime = LocalDateTime.now();
		// default format
		// Default format of LocalDateTime=2016-07-10T11:55:50.466
		System.out.println("Default format of LocalDateTime=" + dateTime);
		// specific format
		// 2016-07-10 11:55:50
		System.out.println(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		// 20160710
		System.out.println(dateTime.format(DateTimeFormatter.BASIC_ISO_DATE));
		Instant timestamp = Instant.now();
		// default format
		// Default format of Instant=2016-07-10T03:55:50.466Z
		System.out.println("Default format of Instant=" + timestamp);
		// Parse examples
		LocalDateTime dt = LocalDateTime.parse("2010-04-12 09:09:12", ofPattern("yyyy-MM-dd HH:mm:ss"));
		// Default format after parsing = 2010-04-12T09:09:12
		System.out.println("Default format after parsing = " + dt);
		// 2010-04-12 09:09:12
		System.out.println(dt.format(ofPattern("yyyy-MM-dd HH:mm:ss")));
		// System.out.println(ofPattern("yyyy-MM-dd HH:mm:ss").format());
		System.out.println(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())
				.format(ofPattern("yyyy-MM-dd HH:mm:ss")));
		System.out.println(ofPattern("yyyy-MM-dd HH:mm:ss")
				.format(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())));
	}

	@Test
	public void testFormat2() {
		// A pattern with an optional section
		String pattern = "MMMM/dd/yyyy[ 'at' HH:mm:ss]";
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
		LocalDate ld = LocalDate.of(2012, Month.AUGUST, 30);
		LocalTime lt = LocalTime.of(17, 30, 12);
		LocalDateTime ldt = LocalDateTime.of(ld, lt);
		LocalDateTime localDateTime = LocalDateTime.of(2016, 7, 26, 10, 40, 12);
		System.out.println(localDateTime);
		// Format a date. Optional section will be skipped because a
		// date does not have time (HH, mm, and ss) information.
		String str1 = fmt.format(ld);
		System.out.println(str1);
		// Format a datetime. Optional section will be output.
		String str2 = fmt.format(ldt);
		System.out.println(str2);
	}

	@Test
	public void testFormatStyle() {
		LocalDate ld = LocalDate.of(2012, Month.APRIL, 19);
		LocalTime lt = LocalTime.of(16, 30, 20);
		LocalDateTime ldt = LocalDateTime.of(ld, lt);
		DateTimeFormatter fmt = DateTimeFormatter.ofLocalizedDate(SHORT);
		// Formatter Default Locale: zh_CN
		System.out.println("Formatter Default Locale: " + fmt.getLocale());
		// Short Date: 12-4-19
		System.out.println("Short Date: " + fmt.format(ld));
		fmt = DateTimeFormatter.ofLocalizedDate(MEDIUM);
		// Medium Date: 2012-4-19
		System.out.println("Medium Date: " + fmt.format(ld));
		fmt = DateTimeFormatter.ofLocalizedDate(LONG);
		// Long Date: 2012年4月19日
		System.out.println("Long Date: " + fmt.format(ld));
		fmt = DateTimeFormatter.ofLocalizedDate(FULL);
		// Full Date: 2012年4月19日 星期四
		System.out.println("Full Date: " + fmt.format(ld));
		fmt = DateTimeFormatter.ofLocalizedTime(SHORT);
		// Short Time: 下午4:30
		System.out.println("Short Time: " + fmt.format(lt));
		fmt = DateTimeFormatter.ofLocalizedTime(MEDIUM);
		System.out.println("Medium Time: " + fmt.format(lt));

		fmt = DateTimeFormatter.ofLocalizedDateTime(SHORT);
		// Short Datetime: 12-4-19 下午4:30
		System.out.println("Short Datetime: " + fmt.format(ldt));
		fmt = DateTimeFormatter.ofLocalizedDateTime(MEDIUM);
		// Medium Datetime: 2012-4-19 16:30:20
		System.out.println("Medium Datetime: " + fmt.format(ldt));
		// Use German locale to format the datetime in medius style
		fmt = DateTimeFormatter.ofLocalizedDateTime(MEDIUM).withLocale(Locale.US);
		// American Medium Datetime: Apr 19, 2012 4:30:20 PM
		System.out.println("American Medium Datetime: " + fmt.format(ldt));
		// Use Indian(English) locale to format datetime in short style
		fmt = DateTimeFormatter.ofLocalizedDateTime(SHORT).withLocale(new Locale("en", "IN"));
		// Indian(en) Short Datetime: 19/4/12 4:30 PM
		System.out.println("Indian(en) Short Datetime: " + fmt.format(ldt));
		// Use Indian(English) locale to format datetime in medium style
		fmt = DateTimeFormatter.ofLocalizedDateTime(MEDIUM).withLocale(new Locale("en", "IN"));
		// Indian(en) Medium Datetime: 19 Apr, 2012 4:30:20 PM
		System.out.println("Indian(en) Medium Datetime: " + fmt.format(ldt));
	}

	@Test
	public void testOldAPI() {
		// Date to Instant
		Instant timestamp = new Date().toInstant();
		System.out.println(MessageFormat.format("timestamp: {0}", timestamp));
		// Now we can convert Instant to LocalDateTime or other similar classes
		LocalDateTime pstDate = LocalDateTime.ofInstant(timestamp, ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
		LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
		System.out.println("pstDate = " + pstDate);
		System.out.println("localDateTime = " + localDateTime);

		// Calendar to Instant
		Instant time = Calendar.getInstance().toInstant();
		System.out.println("time: " + time);
		// TimeZone to ZoneId
		ZoneId defaultZone = TimeZone.getDefault().toZoneId();
		System.out.println(defaultZone);

		// ZonedDateTime from specific Calendar
		ZonedDateTime gregorianCalendarDateTime = new GregorianCalendar().toZonedDateTime();
		System.out.println(gregorianCalendarDateTime);

		// Date API to Legacy classes
		Date dt = Date.from(Instant.now());
		System.out.println(dt);

		TimeZone tz = TimeZone.getTimeZone(defaultZone);
		System.out.println(tz);

		GregorianCalendar gc = GregorianCalendar.from(gregorianCalendarDateTime);
		System.out.println(gc);
	}

	/**
	 * The Period class represents a quantity of time in terms of years, months and
	 * days and the Duration class represents a quantity of time in terms of seconds
	 * and nano seconds.
	 */
	@Test
	public void testPeriodAndDuration() {
		Period p1 = Period.of(2, 3, 5); // 2 years, 3 months, and 5 days
		Period p2 = Period.ofDays(25); // 25 days
		Period p3 = Period.ofMonths(-3); // -3 months
		Period p4 = Period.ofWeeks(3); // 3 weeks (21 days)
		System.out.println(p1); // P2Y3M5D
		System.out.println(p2); // P25D
		System.out.println(p3); // P-3M
		System.out.println(p4); // P21D

		//The Period class is widely used to modify values of given a date or to obtain the difference between two dates:
		LocalDate initialDate = LocalDate.parse("2007-05-10");
		//The Date can be manipulated using Period as shown in the following code snippet:
		LocalDate finalDate = initialDate.plus(Period.ofDays(5));
		System.out.println(finalDate);

		/*
		 * The Period class has various getter methods such as getYears, getMonths and
		 * getDays to get values from a Period object. The below code example returns
		 * an int value of 5 as we try to get difference in terms of days:
		 */
		int five = Period.between(finalDate, initialDate).getDays();
		System.out.println(five);

		/*
		 * The Period between two dates can be obtained in a specific unit such as
		 * days or month or years, using ChronoUnit.between:
		 */
		long f2 = ChronoUnit.DAYS.between(finalDate, initialDate);
		System.out.println(f2);

		/*
		 * Similar to Period, the Duration class is use to deal with Time. In the
		 * following code we create a LocalTime of 6:30 am and then add a duration of
		 * 30 seconds to make a LocalTime of 06:30:30am:
		 */
		LocalTime initialTime = LocalTime.of(6, 30, 0);
		LocalTime finalTime = initialTime.plus(Duration.ofSeconds(30));
		System.out.println(initialTime);
		System.out.println(finalTime);

		/*
		 * The Duration between two instants can be obtained either as a Duration or
		 * as a specific unit. In the first code snippet we use the between() method
		 * of the Duration class to find the time difference between finalTime and
		 * initialTime and return the difference in seconds:
		 */
		long thirty = Duration.between(finalTime, initialTime).getSeconds();
		System.out.println(thirty);

		/*
		 * In the second example we use the between() method of the ChronoUnit class
		 * to perform the same operation:
		 */
		long thirty2 = ChronoUnit.SECONDS.between(finalTime, initialTime);
		System.out.println(thirty2);
	}

	@Test
	public void testPeriodOperation() {
		Period p1 = Period.ofDays(15); // P15D
		Period p2 = p1.plusDays(12); // P27D
		Period p3 = p1.minusDays(12); // P3D
		Period p4 = p1.negated(); // P-15D
		Period p5 = p1.multipliedBy(3); // P45D
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		System.out.println(p5);
	}

	/*
	 * Use the plus() and minus() methods of the Period class to add one period to
	 * another and to subtract one period from another. Use the normalized() method of
	 * the Period class to normalize the years and months. The method ensures that the
	 * month value stays within 0 to 11. For example, a period of “2 years and 15
	 * months” will be normalized to "3 years and 3 months."
	 */
	@Test
	public void testPeriod3() {
		Period p1 = Period.of(2, 3, 5);
		Period p2 = Period.of(1, 15, 28);
		System.out.println("p1: " + p1); // p1: P2Y3M5D
		System.out.println("p2: " + p2); // p2: P1Y15M28D
		System.out.println("p1.plus(p2): " + p1.plus(p2)); // p1.plus(p2): P3Y18M33D
		System.out.println("p1.plus(p2).normalized(): " + p1.plus(p2).normalized()); // p1.plus(p2).normalized():
																						// P4Y6M33D
		System.out.println("p1.minus(p2): " + p1.minus(p2)); // p1.minus(p2):
																// P1Y-12M-23D
	}

	/*
	 * On 2012-03-11T02:00, the clocks in the US Central time zone were moved forward
	 * by one hour, making 2012-03-11 a 23-hour day. Suppose you give a person a
	 * datetime of 2012-03-10T07:30 in the US Central time zone. If you ask him what
	 * would be the datetime after a day, his answer would be 2012-03-11T07:30. His
	 * answer is natural because, for humans, adding one day to the current datetime
	 * gives the next day with the same time. Let’s ask the same question of a
	 * machine. Ask the machine to add 24 hours, which is considered the same as 1
	 * day, to 2012-03-10T07:30. The machine’s response would be 2012-03-11T08:30
	 * because it will add exactly 24 hours to the initial datetime, knowing that the
	 * hour between 02:00 and 03:00 did not exist.
	 */
	@Test
	public void testDiffOfPeriodDuration() {
		ZoneId usCentral = ZoneId.of("America/Chicago");
		// LocalDateTime localDateTime = LocalDateTime.now();
		LocalDateTime localDateTime = LocalDateTime.of(2012, Month.MARCH, 12, 9, 4);
		ZonedDateTime zonedDateTime = ZonedDateTime.of(2012, 3, 12, 9, 4, 0, 0,
				ZoneId.ofOffset("UTC", ZoneOffset.ofHours(8)));
		System.out.println(localDateTime);
		System.out.println(zonedDateTime);
		/*
		 * Period period = Period.ofDays(1); Duration duration = Duration.ofHours(24);
		 * // Add a period of 1 day and a duration of 24 hours ZonedDateTime zdt2 =
		 * zonedDateTime.plus(period); ZonedDateTime zdt3 =
		 * zonedDateTime.plus(duration); System.out.println("Start LocalDatetime: " +
		 * localDateTime); System.out.println("Start Datetime: " + zonedDateTime);
		 * System.out.println("After 1 Day period: " + zdt2);
		 * System.out.println("After 24 Hours duration: " + zdt3); //
		 * System.out.println(ZonedDateTime.of(2012, Month.MARCH, 10, 7, 30, 0, 0,
		 * ZoneId.of(zoneId)));
		 * System.out.println(Clock.systemUTC().getZone().getId());
		 */
	}

	@Test
	public void testGregorianCalendar() {
		// get the supported ids for GMT-08:00 (Pacific Standard Time)
		String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
		// if no ids were returned, something is wrong. get out.
		if (ids.length == 0) {
			System.exit(0);
		}

		// begin output
		System.out.println("Current Time");

		// create a Pacific Standard Time time zone
		SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);

		// set up rules for Daylight Saving Time
		pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

		// create a GregorianCalendar with the Pacific Daylight time zone
		// and the current date and time
		Calendar calendar = new GregorianCalendar(pdt);
		Date trialTime = new Date();
		calendar.setTime(trialTime);

		// print out a bunch of interesting things
		System.out.println("ERA: " + calendar.get(Calendar.ERA));
		System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
		System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
		System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
		System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
		System.out.println("DATE: " + calendar.get(Calendar.DATE));
		System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
		System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
		System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
		System.out.println("DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
		System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
		System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
		System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
		System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
		System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
		System.out.println("ZONE_OFFSET: " + (calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000)));
		System.out.println("DST_OFFSET: " + (calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000)));

		System.out.println("Current Time, with hour reset to 3");
		calendar.clear(Calendar.HOUR_OF_DAY); // so doesn't override
		calendar.set(Calendar.HOUR, 3);
		System.out.println("ERA: " + calendar.get(Calendar.ERA));
		System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
		System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
		System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
		System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
		System.out.println("DATE: " + calendar.get(Calendar.DATE));
		System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
		System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
		System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
		System.out.println("DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
		System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
		System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
		System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
		System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
		System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
		System.out.println("ZONE_OFFSET: " + (calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000))); // in
		// hours
		System.out.println("DST_OFFSET: " + (calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000))); // in
		// hours

	}

	@Test
	public void testDateToLocalDateTime() {
		Date date = new Date();
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
	}

	@Test
	public void testDateToLocalDate() {
		Date date = new Date();
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		LocalDate localDate = localDateTime.toLocalDate();
	}

	@Test
	public void testDateToLocalTime() {
		Date date = new Date();
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		/*
		 * Java 8 has added the toInstant() method which helps to convert existing
		 * Date and Calendar instance to new Date Time API as in the following code
		 * snippet:
		 */
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		LocalTime localTime = localDateTime.toLocalTime();
		System.out.println(localDateTime);
	}

	@Test
	public void testLocalDateTimeToDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		Date date = Date.from(instant);
	}

	@Test
	public void testLocalDateToDate() {
		LocalDate localDate = LocalDate.now();
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
		Date date = Date.from(instant);
	}

	@Test
	public void testBeginDateOfYear() {
		int year = LocalDate.now().getYear();
		System.out.println(year);
		LocalDate yuandan = LocalDate.of(year, 1, 1);
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = yuandan.atStartOfDay().atZone(zone).toInstant();
		Date date = Date.from(instant);
		System.out.println(date);
	}

	@Test
	public void testLocalTimeToDate() {
		LocalTime localTime = LocalTime.now();
		LocalDate localDate = LocalDate.now();
		LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		Date date = Date.from(instant);
	}

	/**
	 * http://www.jianshu.com/p/d017de308ae0 UTC TimeZone获取到的毫秒数跟default
	 * TimeZone获取到的毫秒数是不一样的，差8个小时
	 */
	@Test
	public void testLocalDateTime2EpochMili() {
		long miliseconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long miliseconds2 = System.currentTimeMillis();
		long miliseconds3 = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
		System.out.println("miliseconds: " + miliseconds);
		System.out.println("miliseconds: " + new Date().getTime());
		System.out.println("miliseconds: " + miliseconds2);
		System.out.println("miliseconds: " + miliseconds3);

		//To avoid ZoneId you can do:
		long miliseconds4 = LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
		System.out.println("Initial Epoch (TimeInMillis): " + miliseconds4);
	}

	@Test
	public void testLocalDate2EpochSeconds() {
		ZoneId zoneId = ZoneOffset.UTC;
		LocalDate now = LocalDate.now();
		long epoch = now.atStartOfDay(zoneId).toEpochSecond();
		long epoch2 = now.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
		System.out.println("epoch: " + epoch);
		System.out.println("epoch2: " + epoch2);

		Instant instant = new Date().toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
	}

	@Test
	public void testLocalDate2EpochMilis() {
		LocalDate now = LocalDate.now();
		long epoch = now.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
		//		long epoch = now.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
		long epoch2 = now.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		System.out.println("epoch: " + epoch);
		System.out.println("epoch2: " + epoch2);
	}

	@Test
	public void testEpochMilis2LocalDate() {
		LocalDate date = Instant.ofEpochMilli(405648000000L).atZone(ZoneId.systemDefault()).toLocalDate();
		System.out.println(date);

		LocalDate date2 = Instant.ofEpochMilli(405648000000L).atZone(ZoneOffset.UTC).toLocalDate();
		System.out.println(date2);

		LocalDateTime date3 = LocalDateTime.ofInstant(Instant.ofEpochMilli(405648000000L), ZoneId.systemDefault());
		System.out.println(date3);

		LocalDateTime date4 = LocalDateTime.ofInstant(Instant.ofEpochMilli(405648000000L), ZoneOffset.UTC);
		System.out.println(date4);
	}

	@Test
	public void testGetEpochMilis() {
		System.out.println(LocalDate.of(2017, 11, 1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
		;
	}

	@Test
	public void testGetMilis() {
		System.out.println(LocalDate.of(2017, 11, 7).atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
	}

	@Test
	public void difference_between_two_dates_java8_period() {

		LocalDate sinceGraduation = LocalDate.of(1984, Month.JUNE, 4);
		LocalDate currentDate = LocalDate.now();

		Period betweenDates = Period.between(sinceGraduation, currentDate);

		int diffInDays = betweenDates.getDays();
		int diffInMonths = betweenDates.getMonths();
		int diffInYears = betweenDates.getYears();

		System.out.println(diffInDays);
		System.out.println(diffInMonths);
		System.out.println(diffInYears);
	}

	@Test
	public void difference_between_two_dates_java8_chrono_period() {

		LocalDate sinceGraduation = LocalDate.of(1984, Month.JUNE, 4);
		LocalDate currentDate = LocalDate.now();

		long diffInDays = ChronoUnit.DAYS.between(sinceGraduation, currentDate);
		long diffInMonths = ChronoUnit.MONTHS.between(sinceGraduation,
				currentDate);
		long diffInYears = ChronoUnit.YEARS.between(sinceGraduation,
				currentDate);

		System.out.println(diffInDays);
		System.out.println(diffInMonths);
		System.out.println(diffInYears);

	}

	@Test
	public void testLocalTime() {
		LocalTime now = LocalTime.now();
		System.out.println(now);
		LocalTime sixThirty = LocalTime.parse("06:30");
		System.out.println(sixThirty);
		/*
		 * The Factory method “of” can be used to create a LocalTime. For example the
		 * below code creates LocalTime representing 06:30 AM using the factory
		 * method:
		 */
		sixThirty = LocalTime.of(6, 30);
		System.out.println(sixThirty);

		/*
		 * The below example creates a LocalTime by parsing a string and adds an hour
		 * to it by using the “plus” API. The result would be LocalTime representing
		 * 07:30 AM:
		 */
		LocalTime sevenThirty = LocalTime.parse("06:30").plus(1, ChronoUnit.HOURS);
		System.out.println(sevenThirty);

		/*
		 * Various getter methods are available which can be used to get specific
		 * units of time like hour, min and secs like below:
		 */
		int six = LocalTime.parse("06:30").getHour();

		/*
		 * We can also check if a specific time is before or after another specific
		 * time. The below code sample compares two LocalTime for which the result
		 * would be true:
		 */
		boolean isbefore = LocalTime.parse("06:30").isBefore(LocalTime.parse("07:30"));

		/*
		 * The max, min and noon time of a day can be obtained by constants in
		 * LocalTime class. This is very useful when performing database queries to
		 * find records within a given span of time. For example, the below code
		 * represents 23:59:59.99:
		 */
		LocalTime maxTime = LocalTime.MAX;
		LocalTime noonTime = LocalTime.NOON;
		LocalTime minTime = LocalTime.MIN;
		System.out.println(format("maxTime: {0}, noonTime: {1}, minTime: {2}", maxTime, noonTime, minTime));
	}
	
	@Test
	public void testName() {
		LocalDateTime ldt = LocalDateTime.parse("2016-06-13T11:34:50"); //2016-06-13T11:34:50
	    //下面两个表示 伦敦当地的2016-06-13T11:34:50，北京当地的2016-06-13T11:34:50，所以时间都是一样的
	    ZonedDateTime ldtAtUTC = ldt.atZone(ZoneOffset.UTC);            //2016-06-13T11:34:50Z
	    ZonedDateTime ldtAtCST = ldt.atZone(ZoneOffset.ofHours(+8));    //2016-06-13T11:34:50+08:00
	    ldtAtUTC.toInstant().toEpochMilli();         //1465817690000
	    ldtAtCST.toInstant().toEpochMilli(); //1465817690000
	}
}
