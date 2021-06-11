package com.loserico.java8.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * <p>
 * Copyright: (C), 2021-06-02 21:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Stream2MapTest {
	
	@Test
	public void testListToMap() {
		List<Hosting> list = new ArrayList<>();
		list.add(new Hosting(1, "liquidweb.com", 80000));
		list.add(new Hosting(2, "linode.com", 90000));
		list.add(new Hosting(3, "digitalocean.com", 120000));
		list.add(new Hosting(4, "aws.amazon.com", 200000));
		list.add(new Hosting(5, "mkyong.com", 1));
		
		// key = id, value - websites
		Map<Integer, String> result1 = list.stream().collect(
				toMap(Hosting::getId, Hosting::getName));
		System.out.println("Result 1 : " + result1);
		
		// key = name, value - websites
		Map<String, Long> result2 = list.stream().collect(toMap(Hosting::getName, Hosting::getWebsites));
		System.out.println("Result 2 : " + result2);
		
		// Same with result1, just different syntax
		// key = id, value = name
		Map<Integer, String> result3 = list.stream().collect(toMap(x -> x.getId(), x -> x.getName()));
		System.out.println("Result 3 : " + result3);
		
		Map<String, Hosting> result4 = list.stream().collect(toMap(Hosting::getName, identity()));
		System.out.println("Result 4: " + toJson(result4));
	}
}
