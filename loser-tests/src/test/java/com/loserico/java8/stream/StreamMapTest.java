package com.loserico.java8.stream;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * https://www.mkyong.com/java8/java-8-streams-map-examples/
 * <p>
 * Copyright: Copyright (c) 2018-04-13 11:40
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class StreamMapTest {

	@Test
	public void testMapToUpperCase() {
		List<String> alpha = Arrays.asList("a", "b", "c", "d");

		//Before Java8
		List<String> alphaUpper = new ArrayList<>();
		for (String s : alpha) {
			alphaUpper.add(s.toUpperCase());
		}

		System.out.println(alpha); //[a, b, c, d]
		System.out.println(alphaUpper); //[A, B, C, D]

		// Java 8
		List<String> collect = alpha.stream().map(String::toUpperCase).collect(toList());
		System.out.println(collect);

		// Extra, streams apply to any data type.
		List<Integer> num = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> collect1 = num.stream().map(n -> n * 2).collect(toList());
		System.out.println(collect1);
	}

	@Test
	public void testMapObjectList2StringList() {
		List<Staff> staff = Arrays.asList(
				new Staff("mkyong", 30, new BigDecimal(10000)),
				new Staff("jack", 27, new BigDecimal(20000)),
				new Staff("lawrence", 33, new BigDecimal(30000)));

		//Before Java 8
		List<String> result = new ArrayList<>();
		for (Staff x : staff) {
			result.add(x.getName());
		}
		System.out.println(result); //[mkyong, jack, lawrence]

		//Java 8
		List<String> collect = staff.stream().map(Staff::getName).collect(toList());
		System.out.println(collect);
	}

	@Test
	public void testMapOneObject2AnotherObjectList() {
		List<Staff> staff = Arrays.asList(
				new Staff("mkyong", 30, new BigDecimal(10000)),
				new Staff("jack", 27, new BigDecimal(20000)),
				new Staff("lawrence", 33, new BigDecimal(30000)));

		List<StaffPublic> result = convertToStaffPublic(staff);
		System.out.println(result);

		//java8
		List<StaffPublic> result2 = staff.stream().map((temp) -> {
			StaffPublic obj = new StaffPublic();
			obj.setName(temp.getName());
			obj.setAge(temp.getAge());
			if ("mkyong".equals(temp.getName())) {
				obj.setExtra("this field is for mkyong only!");
			}
			return obj;
		}).collect(toList());
		System.out.println(result2);
	}

	private static List<StaffPublic> convertToStaffPublic(List<Staff> staff) {
		List<StaffPublic> result = new ArrayList<>();

		for (Staff temp : staff) {
			StaffPublic obj = new StaffPublic();
			obj.setName(temp.getName());
			obj.setAge(temp.getAge());
			if ("mkyong".equals(temp.getName())) {
				obj.setExtra("this field is for mkyong only!");
			}
			result.add(obj);
		}

		return result;

	}
}
