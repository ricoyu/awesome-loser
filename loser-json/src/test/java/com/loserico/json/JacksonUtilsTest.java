package com.loserico.json;

import com.loserico.json.jackson.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2019/12/21 19:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JacksonUtilsTest {
	
	@Test
	public void testDeserialLocalDateTime() {
		String s = "2019-12-21 19:15:34";
		String dateStr = JacksonUtils.toJson(new DateObj(LocalDateTime.of(2019, 12, 21, 19, 15, 34)));
		System.out.println(dateStr);
		DateObj dateObj = JacksonUtils.toObject(dateStr, DateObj.class);
		Assert.assertEquals(dateObj.getDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), s);
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class DateObj {
		private LocalDateTime datetime;
	}
	
	@Test
	public void testJson2Map() {
		String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";
		Map<String, Object> params = JacksonUtils.toMap(jsonString);
		System.out.println(params.toString());
		
		Map<Object, Object> genericMap = JacksonUtils.toGenericMap(jsonString);
		System.out.println(genericMap.toString());
	}
	
	@Test
	public void testPojoMap() {
		Map<String, Object> params = new HashMap<>();
		params.put("fullname", "三少爷");
		params.put("birthday", "2020-03-20");
		Person person = JacksonUtils.mapToPojo(params, Person.class);
		System.out.println(person);
		//Assert.assertEquals("三少爷", person.getFullName());
		Map<String, Object> resultMap = JacksonUtils.pojoToMap(person);
		System.out.println(resultMap);
		Assert.assertEquals("2020-03-20", resultMap.get("birthday"));
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Person{
		
		//private String fullName;
		
		private LocalDate birthday;
	}
}
