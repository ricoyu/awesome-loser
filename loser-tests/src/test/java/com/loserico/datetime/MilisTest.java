package com.loserico.datetime;

import com.loserico.common.lang.utils.DateUtils;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class MilisTest {

	@Test
	public void testDate2LocalDateTime() {
		Instant instant = new Date().toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		System.out.println(localDateTime);
	}

	@Test
	public void testDateToLocalDate() {
		Date date = new Date();
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate localDate = localDateTime.toLocalDate();
		System.out.println(localDate);
	}
	
	@Test
	public void testLocalDateTimeToDate() {
		LocalDateTime now = LocalDateTime.now();
		Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
		Date date = Date.from(instant);
		System.out.println(date);
	}
	
	@Test
	public void testLocalDate2Date() {
		LocalDate now = LocalDate.now();
		Instant instant = now.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date date = Date.from(instant);
		System.out.println(date);
	}
	
	@Test
	public void testString2EpocMilis() {
		//2018-01-02 10:53:03
		System.out.println(DateUtils.toEpochMilis("2018-01-02 10:53:03"));
		System.out.println(DateUtils.toEpochMilis("2018-05-10 23:53:03"));
	}
	@Test
	public void testLocalDateTimeToEpochMilis() {
//		System.out.println(LocalDateTime.of(1, 1, 1, 0, 0).atZone(ZoneOffset.UTC).toInstant().toEpochMilli());;
		System.out.println(LocalDateTime.now().atZone(ZoneOffset.UTC));
		/*
		 * (milis2-milis1)/1000/60/60 = -8
		 * 我现在的理解：
		 * 我当前电脑是北京时间2018-03-05 21:56
		 * 
		 * LocalDateTime.now().atZone(ZoneOffset.UTC)
		 * 这段代码的意思是：
		 */
		long milis1 = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
		long milis2 = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		System.out.println(milis1);;
		System.out.println(milis2);;
		assertEquals(milis1, milis2);
		
		LocalDateTime now = LocalDateTime.now();
		long milis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		System.out.println(milis);
	}
	
	@Test
	public void testLocalDate2EpochMilis() {
//		LocalDate now = LocalDate.of(2017, 12, 31);
		LocalDate now = LocalDate.of(1, 1, 1);
		System.out.println(now.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());;
	}
	
	@Test
	public void testMilis2LocalDate() {
		long milis = -62135625943000L;
//		long milis = 1510070400000L;
		System.out.println(Instant.ofEpochMilli(milis).atZone(ZoneId.systemDefault()).toLocalDate());;
	}
	
	@Test
	public void testMilis2LocalDateTime() {
		long milis = 1510070400000L;
		System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli(milis), ZoneId.systemDefault()));
	}
	
	@Test
	public void testCSTTimeZone() {
		TimeZone timeZone = TimeZone.getTimeZone("CST");
		System.out.println(timeZone);
		
		System.out.println(ZoneId.of("Asia/Shanghai"));
	}
}
