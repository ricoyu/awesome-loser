package com.loserico.jdk12;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestStrTransform {

	@Test
	public void test() {
		var result = "马士兵".transform(s -> s + "教育");
		System.out.println(result); //马士兵教育
	}

	@Test
	public void test2() {
		List<String> list1 = List.of("Java", " Golang", " MCA ");
		List<String> list2 = new ArrayList<>();
		list1.forEach(s -> list2.add(s.transform(String::strip)
				.transform(String::toUpperCase)
				.transform(l -> "Hello " + l)
		));

		list2.forEach(System.out::println);
	}
}
