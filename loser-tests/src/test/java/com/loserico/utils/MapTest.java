package com.loserico.utils;

import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapTest {

	/*
	 * http://minborgsjavapot.blogspot.sg/2014/12/java-8-initializing-maps-in-smartest
	 * -way.html
	 */
	@Test
	public void testStaticCreate() {
		Map<Integer, String> resultMap = Collections.unmodifiableMap(new HashMap<Integer, String>() {
			{
				put(0, "zero");
				put(1, "one");
				put(2, "two");
				put(3, "three");
				put(4, "four");
				put(5, "five");
				put(6, "six");
				put(7, "seven");
				put(8, "eight");
				put(9, "nine");
				put(10, "ten");
				put(11, "eleven");
				put(12, "twelve");
			}
		});
		System.out.println(JacksonUtils.toJson(resultMap));
	}

}
