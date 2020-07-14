package com.loserico.datetime;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

/**
 * https://www.mkyong.com/java8/java-8-how-to-convert-string-to-localdate/
 * http://www.baeldung.com/java-date-regular-expressions
 * 
 * <p>
 * Copyright: Copyright (c) 2018-05-09 14:12
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class String2LocalDateTest {

	@Test
	public void test1() {
		DateTimeFormatter formatter = ofPattern("yyyy-M-d");
//		DateTimeFormatter formatter = ofPattern("d/M/yyyy");
//		String date = "1/8/2016";
//		String date = "1/08/2016";
//		String date = "16/08/2016";
		String date = "2018-1-1";
		//convert String to LocalDate
		LocalDate localDate = LocalDate.parse(date, formatter);
		System.out.println(localDate);
	}
}
