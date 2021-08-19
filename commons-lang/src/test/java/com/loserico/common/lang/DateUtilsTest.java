package com.loserico.common.lang;

import com.loserico.common.lang.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2019/11/7 9:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class DateUtilsTest {
	
	@Test
	public void testDate() {
		System.out.println(new Date(1629045321942L));
		
		ZoneId zoneId = ZoneId.of("+08:00");
		ZoneId zoneId1 = ZoneId.of("Asia/Shanghai");
		ZoneId zoneId2 = TimeZone.getTimeZone("Asia/Shanghai").toZoneId();
		System.out.println(zoneId.equals(zoneId1));
		System.out.println(zoneId2.equals(zoneId1));
	}
	
	@Test
	public void testStartUpTimes() throws ClassNotFoundException {
		long begin = System.nanoTime();
		//Class.forName("com.loserico.common.lang.utils.DateUtils");
		long end = System.nanoTime();
		System.out.println("初始化DateUtils需要: " + (end - begin) / 1000000 + "毫秒");
		log.info("初始化DateUtils需要: " + (end - begin) / 1000000 + "毫秒");
		
		System.out.println(new Date(1616045447386L));
		System.out.println(new Date(1616046347386L));
	}
	
	@Test
	public void testParse() {
		String startDateTime = "2021-03-18 13:30:47";
		String endDateTime = "2021-03-19 13:45:47";
		
		Date date = DateUtils.parse("2021-07-22 10:58:17");
		assertNotNull(date);
		
		long startTime = DateUtils.toEpochMilis(startDateTime);
		System.out.println("startTime:" + startTime);
		long endTime = DateUtils.toEpochMilis(endDateTime);
		System.out.println("endTime:" + endTime);
		
		System.out.println(new Date(1616045447386L));
		System.out.println(new Date(1616046347386L));
	}
	
	@Test
	public void testReverseParse() {
		LocalDateTime startDateTime = DateUtils.toLocalDateTime(1615132800000L);
		System.out.println(startDateTime);
		LocalDateTime endDateTime = DateUtils.toLocalDateTime(1615737599000L);
		System.out.println(endDateTime);
	}
	
	@Test
	public void testDynamicParse() throws ParseException {
		String dateStr1 = "2020-12-23T12:51:18.456019+0200";
		String dateStr2 = "2020-12-23T12:51:18.456+0200";
		String dateStr3 = "2020-12-23T12:51:18.456+0800";
		String dateStr4 = "2020-12-23 12:51:18.456+0800";
		String dateStr5 = "2020-12-23 12:51:18+0800";
		String dateStr6 = "2020-12-23 12:51:18";
		
		List<String> dates = asList(dateStr1, dateStr2, dateStr3, dateStr4, dateStr5, dateStr6);
		StringBuilder patternBuilder;
		Pattern pattern = Pattern.compile("(\\d{2,4})-(\\d{1,2})-(\\d{1,2})(T?)\\s*(\\d{1,2}):(\\d{1,2}):(\\d{1,2})\\.?(\\d*)(\\+\\d+)?");
		//                                 1          2          3         4       5          6          7              8     9   
		for (String dateStr : dates) {
			Matcher matcher = pattern.matcher(dateStr);
			if (matcher.matches()) {
				String group1 = matcher.group(1);
				String group2 = matcher.group(2);
				String group3 = matcher.group(3);
				String group0 = matcher.group(0);
				
				System.out.println("group1:" + matcher.group(1));
				System.out.println("group2:" + matcher.group(2));
				System.out.println("group3:" + matcher.group(3));
				System.out.println("group4:" + matcher.group(4));
				System.out.println("group5:" + matcher.group(5));
				System.out.println("group6:" + matcher.group(6));
				System.out.println("group7:" + matcher.group(7));
				System.out.println("group8:" + matcher.group(8));
				System.out.println("group9:" + matcher.group(9));
				System.out.println(matcher.group(0));
				
				String year = matcher.group(1);
				String month = matcher.group(2);
				String day = matcher.group(3);
				String t = matcher.group(4);
				String hour = matcher.group(5);
				String minute = matcher.group(6);
				String second = matcher.group(7);
				String milli = matcher.group(8);
				String zone = matcher.group(9);
				
				StringBuilder format = new StringBuilder();
				//yyyy
				if (isNotBlank(year)) {
					for (int i = 0; i < year.length(); i++) {
						format.append("y");
					}
				}
				format.append("-");
				
				//MM
				if (isNotBlank(month)) {
					for (int i = 0; i < month.length(); i++) {
						format.append("M");
					}
				}
				format.append("-");
				
				//dd
				if (isNotBlank(day)) {
					for (int i = 0; i < day.length(); i++) {
						format.append("d");
					}
				}
				
				//"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
				if (isNotBlank(t)) {
					format.append("'T'");
				} else {
					format.append(" ");
				}
				
				//HH
				if (isNotBlank(hour)) {
					for (int i = 0; i < hour.length(); i++) {
						format.append("H");
					}
				}
				
				//:mm
				if (isNotBlank(minute)) {
					format.append(":");
					for (int i = 0; i < minute.length(); i++) {
						format.append("m");
					}
				}
				
				//:ss
				if (isNotBlank(second)) {
					format.append(":");
					for (int i = 0; i < second.length(); i++) {
						format.append("s");
					}
				}
				//.SSSSSS
				if (isNotBlank(milli)) {
					format.append(".");
					for (int i = 0; i < milli.length(); i++) {
						format.append("S");
					}
				}
				//Z
				if (isNotBlank(zone)) {
					format.append("Z");
				}
				System.out.println(format.toString());
				System.out.println(new SimpleDateFormat(format.toString()).parse(dateStr));
				System.out.println();
			}
			
		}
	}
	
	@Test
	public void testReverse() {
		Date date = new Date(1611715800000L);
		String dateStr = DateUtils.format(date);
		System.out.println(dateStr);
		Date date1 = DateUtils.parse(dateStr);
		assertTrue(date1.getTime() == date.getTime());
	}
	
	@Test
	public void testLocalDateTimeToGMT() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now);
		
		ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
		ZonedDateTime utc = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		System.out.println(utc);
	}
	
	@Test
	public void testDateDiff() {
		LocalDate begin = LocalDate.of(2021, 05, 25);
		LocalDate end = LocalDate.of(2021, 05, 18);
		assertEquals(7, DateUtils.dateDiff(begin, end));
		assertEquals(-7, DateUtils.dateDiff(end, begin));
		assertEquals(0, DateUtils.dateDiff(begin, LocalDate.of(2021, 05, 25)));
		System.out.println(DateUtils.dateDiff(begin, LocalDate.of(2021, 06, 14)));
	}
	
	@Test
	public void testUTCParse() {
		//String dateStr = "2021-05-22T02:01:43.003Z";
		String dateStr = "08/03/2019T16:20:17:717 UTC+05:30";
		//Pattern utcPattern = Pattern.compile("^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?(Z)?$");
		//boolean matches = utcPattern.matcher(dateStr).matches();
		//System.out.println(matches);
		Date date = DateUtils.parse(dateStr);
		
		System.out.println(date);
	}
	
	@Test
	public void testFMTGMTparse() throws ParseException {
		String dateStr = "Sun, 06 Nov 1994 08:49:37 GMT";
		Date date = DateUtils.parse(dateStr);
		System.out.println(date);
		
		dateStr = "Mon, 16 Apr 2018 00:00:00 GMT+08:00";
		Date date2 = DateUtils.parse(dateStr);
		System.out.println(date2);
		
		assertThat(date2.getDate()).isEqualTo(16);
		assertThat(date2.getMonth()).isEqualTo(3);
		assertThat(date2.getHours()).isEqualTo(0);
		assertThat(date2.getMinutes()).isEqualTo(0);
		assertThat(date2.getSeconds()).isEqualTo(0);
	}
	
	@Test
	public void testGMT() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println(format.parse("Mon, 03 Jun 2013 07:01:29 GMT"));
		System.out.println(format.parse("Mon, 16 Apr 2018 00:00:00 GMT+08:00"));
		System.out.println(DateUtils.parse("Mon, 03 Jun 2013 07:01:29 GMT"));
		System.out.println(DateUtils.parse("Mon, 16 Apr 2018 00:00:00 GMT+08:00"));
	}
	
	@Test
	public void testParseJenkinsDateFormat() {
		String dateStr = "2021-06-01T14:22:04+08:00";
		LocalDateTime date1 = DateUtils.toLocalDateTime(dateStr);
		Date date2 = DateUtils.parse(dateStr);
		assertEquals(date2.getHours(), date1.getHour());
		assertEquals(date1.getDayOfMonth(), date2.getDate());
		System.out.println(date1);
		System.out.println(date2);
	}
	
	@Test
	public void testCurrentMillis() {
		System.out.println(System.currentTimeMillis());
	}
	
	@Test
	public void test() {
		String dateStr = "2021-06-08T15:44:03";
		Date date = DateUtils.parse(dateStr);
		System.out.println(date);
		
		LocalDateTime localDateTime = DateUtils.toLocalDateTime("2021-06-08T15:44:03");
		System.out.println(localDateTime);
	}
	
	@Test
	public void testParseUTC() {
		String dateStr = "2018-05-30T14:24:48.933+0800";
		LocalDateTime date1 = DateUtils.toLocalDateTime(dateStr);
		Date date2 = DateUtils.parse(dateStr);
		assertEquals(date2.getHours(), date1.getHour());
		assertEquals(date1.getDayOfMonth(), date2.getDate());
		System.out.println(date1);
		System.out.println(date2);
	}
	
	@Test
	public void testToLocaldate() {
		Date date = new Date(2021, 7, 20);
		LocalDate localDate = DateUtils.toLocalDate(date);
		System.out.println(localDate);
	}
	
	@Test
	public void testToMillis() {
		LocalDateTime begin = LocalDateTime.of(2021, 7, 28, 10, 41, 0);
		LocalDateTime end = LocalDateTime.of(2021, 7, 28, 10, 43, 2);
		
		System.out.println(DateUtils.toEpochMilis(begin));;
		System.out.println(DateUtils.toEpochMilis(end));;
	}
	
	@Test
	public void testToDate() {
		System.out.println(new Date(1627452912796L));
		System.out.println(new Date(1627439417482L));
	}
}
