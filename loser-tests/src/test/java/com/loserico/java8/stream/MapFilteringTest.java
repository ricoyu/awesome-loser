package com.loserico.java8.stream;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapFilteringTest {

	@Test
	public void testMapHostingFilter() {
		Map<Integer, String> hostingMap = new HashMap<>();
		hostingMap.put(1, "linode.com");
		hostingMap.put(2, "heroku.com");
		hostingMap.put(3, "digitalocean.com");
		hostingMap.put(4, "aws.amazon.com");

		String result = "";
		for (Map.Entry<Integer, String> entry : hostingMap.entrySet()) {
			if ("aws.amazon.com".equals(entry.getValue())) {
				result = entry.getValue();
			}
		}
		System.out.println("Before Java 8 : " + result);

		//Map -> Stream -> Filter -> String
		result = hostingMap.entrySet().stream()
				.filter(map -> "aws.amazon.com".equals(map.getValue()))
				.map(map -> map.getValue())
				.collect(Collectors.joining());

		System.out.println("With Java 8 : " + result);
	}

	@Test
	public void testMapFilterByKey() {
		Map<Integer, String> HOSTING = new HashMap<>();
		HOSTING.put(1, "linode.com");
		HOSTING.put(2, "heroku.com");
		HOSTING.put(3, "digitalocean.com");
		HOSTING.put(4, "aws.amazon.com");

		//Map -> Stream -> Filter -> Map
		Map<Integer, String> collect = HOSTING.entrySet().stream()
				.filter(map -> map.getKey() == 2)
				.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

		System.out.println(collect); //output : {2=heroku.com}
	}
}
