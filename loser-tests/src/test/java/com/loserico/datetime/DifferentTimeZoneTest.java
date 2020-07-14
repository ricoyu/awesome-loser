package com.loserico.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

public class DifferentTimeZoneTest {

	/**
	 * 模拟一架从吉隆坡(+08:00)飞往东京(+09:00)的航班，飞行时间7小时
	 * Malaysia (KUL) -> Japan (HND)
	 * Review a flight information from Malaysia Kuala Lumpur (UTC+08:00) to Japan Tokyo Haneda (UTC+09:00)
	 * 
	 * ---Flight Detail---
	 * Kuala Lumpur (KUL) -> Tokyo Haneda (HND)
	 * Flight Duration : 7 hours
	 * 
	 * (KUL-Depart) 1430, 22 Aug 2016 ->  2230, 22 Aug 2016 (HND-Arrive)
	 * 
	 * https://www.mkyong.com/java8/java-8-zoneddatetime-examples/
	 * @on
	 */
	@Test
	public void testFlightFromKualaLumpur2Tokyo() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HHmm, dd MMM yyyy");

		LocalDateTime ldt = LocalDateTime.of(2016, Month.AUGUST, 22, 14, 30);
		System.out.println("LocalDateTime : " + format.format(ldt)); //LocalDateTime : 1430, 22 八月 2016

		//UTC+8
		ZonedDateTime klDateTime = ldt.atZone(ZoneId.of("Asia/Kuala_Lumpur"));
		System.out.println("Depart : " + format.format(klDateTime)); //Depart : 1430, 22 八月 2016

		//UTC+9 and flight duration = 7 hours
		ZonedDateTime japanDateTime = klDateTime.withZoneSameInstant(ZoneId.of("Asia/Tokyo")).plusHours(7);
		System.out.println("Arrive : " + format.format(japanDateTime)); //Arrive : 2230, 22 八月 2016

		/*
		 * ---Detail---
		 * Depart : 2016-08-22T14:30+08:00[Asia/Kuala_Lumpur]
		 * Arrive : 2016-08-22T22:30+09:00[Asia/Tokyo]
		 * @on
		 */
		System.out.println("\n---Detail---");
		System.out.println("Depart : " + klDateTime);
		System.out.println("Arrive : " + japanDateTime);
	}

	/**
	 * 另一架航班从法国巴黎(+02:00) 飞往美国纽约(-05:00)
	 * Another time zone example from France, Paris (UTC+02:00, DST) to a hard coded (UTC-05:00) time zone (e.g New York)
	 * 
	 * 航班信息：
	 * ---Flight Detail---
	 * France, Paris -> UTC-05:00
	 * Flight Duration : 8 hours 10 minutes
	 * 
	 * (Depart) 1430, 22 Aug 2016 ->  1540, 22 Aug 2016 (Arrive)
	 * 2016-08-22 14:30 出发，从巴黎飞往纽约，飞行时间8小时10分钟，到达时间2016-08-22 15:40
	 * 2016-08-22 14:30 出发,飞8时10分，则最终时间为2016-08-22 22:40，纽约比巴黎晚7小时，所以到达纽约时，纽约当地时间为2016-08-22 22:40 减7小时即 2016-08-22 15:40
	 * 
	 * @on
	 */
	@Test
	public void testFlightFromParis2NewYork() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HHmm, dd MMM yyyy");

		//Convert String to LocalDateTime
		String date = "2016-08-22 14:30";
		LocalDateTime ldt = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		System.out.println("LocalDateTime : " + format.format(ldt)); //LocalDateTime : 1430, 22 八月 2016

		//Paris, 2016 Apr-Oct = DST, UTC+2, other months UTC+1
		//UTC+2
		ZonedDateTime parisDateTime = ldt.atZone(ZoneId.of("Europe/Paris"));
		System.out.println("Depart : " + format.format(parisDateTime)); //Depart : 1430, 22 八月 2016

		//hard code a zoneoffset like this, UTC-5
		ZoneOffset nyOffSet = ZoneOffset.of("-05:00");
		ZonedDateTime nyDateTime = parisDateTime.withZoneSameInstant(nyOffSet).plusHours(8).plusMinutes(10);
		System.out.println("Arrive : " + format.format(nyDateTime)); //Arrive : 1540, 22 八月 2016

		//withZoneSameInstant 和 plusXXX的顺序是无关的
		ZonedDateTime nyDateTime2 = parisDateTime.plusHours(8).plusMinutes(10).withZoneSameInstant(nyOffSet);
		System.out.println("Arrive : " + format.format(nyDateTime2)); //Arrive : 1540, 22 八月 2016
	}

	/**
	 * Example to convert a Instant UTC+0 to a Japan ZonedDateTime UTC+9
	 */
	@Test
	public void testConvertInstant2ZonedDateTime() {
		// Z = UTC+0
		Instant instant = Instant.now();
		System.out.println("Instant : " + instant);

		// Japan = UTC+9
		ZonedDateTime jpTime = instant.atZone(ZoneId.of("Asia/Tokyo"));
		System.out.println("ZonedDateTime : " + jpTime);
		System.out.println("OffSet : " + jpTime.getOffset());
	}
	
	@Test
	public void testConvertLocalDateTime2ZonedDateTime() {
		// Z = UTC+0
		LocalDateTime now = LocalDateTime.now();
		System.out.println("LocalDateTime : " + now);
		
		/*
		 * LocalDateTime : 2018-03-06T10:26:30.836
		 * ZonedDateTime : 2018-03-06T10:26:30.836+09:00[Asia/Tokyo]
		 * OffSet : +09:00
		 * @on
		 */
		ZonedDateTime jpTime = now.atZone(ZoneId.of("Asia/Tokyo"));
		System.out.println("ZonedDateTime : " + jpTime);
		System.out.println("OffSet : " + jpTime.getOffset());
	}
}
