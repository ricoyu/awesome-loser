package com.loserico.common.lang;

import com.loserico.common.lang.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
	public void testStartUpTimes() throws ClassNotFoundException {
		long begin = System.nanoTime();
		//Class.forName("com.loserico.common.lang.utils.DateUtils");
		long end = System.nanoTime();
		System.out.println("初始化DateUtils需要: " + (end - begin) / 1000000 + "毫秒");
		log.info("初始化DateUtils需要: " + (end - begin) / 1000000 + "毫秒");
	}
	
	@Test
	public void testParse() {
		String date1 = "2020-12-23T12:51:18.456019+0800";
		String date2 = "2020-12-24T12:51:18.456019+0800";
		Date date = DateUtils.parse(date1);
		System.out.println(date);
		date = DateUtils.parse(date2);
		System.out.println(date);
	}
	
	@Test
	public void testParse2() throws ParseException {
		String date1 = "2020-12-23T12:51:18.456019+0200";
		String date2 = "2020-12-24T12:51:18.456019+0800";
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = simpleDateFormat1.parse(date1);
		Date d2 = simpleDateFormat2.parse(date1);
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d1.getTime() == d2.getTime());
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
				String t =  matcher.group(4);
				String hour =  matcher.group(5);
				String minute =  matcher.group(6);
				String second =  matcher.group(7);
				String milli =  matcher.group(8);
				String zone =  matcher.group(9);
				
				StringBuilder format = new StringBuilder();
				//yyyy
				if (isNotBlank(year)) {
					for(int i=0; i< year.length(); i++) {
						format.append("y");
					}
				}
				format.append("-");
				
				//MM
				if (isNotBlank(month)) {
					for(int i=0; i< month.length(); i++) {
						format.append("M");
					}
				}
				format.append("-");
				
				//dd
				if (isNotBlank(day)) {
					for(int i=0; i< day.length(); i++) {
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
					for(int i=0; i< hour.length(); i++) {
						format.append("H");
					}
				}
				
				//:mm
				if (isNotBlank(minute)) {
					format.append(":");
					for(int i=0; i< minute.length(); i++) {
						format.append("m");
					}
				}
				
				//:ss
				if (isNotBlank(second)) {
					format.append(":");
					for(int i=0; i< second.length(); i++) {
						format.append("s");
					}
				}
				//.SSSSSS
				if (isNotBlank(milli)) {
					format.append(".");
					for(int i=0; i< milli.length(); i++) {
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
}
