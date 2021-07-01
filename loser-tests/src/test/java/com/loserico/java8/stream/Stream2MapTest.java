package com.loserico.java8.stream;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

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
	
	
	@Test
	public void testList2MapGroupingByMapping() {
		//3 apple, 2 banana, others 1
		List<Item> items = Arrays.asList(
				new Item("apple", 10, new BigDecimal("9.99")),
				new Item("banana", 20, new BigDecimal("19.99")),
				new Item("orang", 10, new BigDecimal("29.99")),
				new Item("watermelon", 10, new BigDecimal("29.99")),
				new Item("papaya", 20, new BigDecimal("9.99")),
				new Item("apple", 10, new BigDecimal("9.99")),
				new Item("banana", 10, new BigDecimal("19.99")),
				new Item("apple", 20, new BigDecimal("9.99")));
		
		//group by price
		Map<BigDecimal, List<Item>> groupByPriceMap = items.stream().collect(groupingBy(Item::getPrice));
		System.out.println(groupByPriceMap);
		
		// group by price, uses 'mapping' to convert List<Item> to Set<String>
		Map<BigDecimal, Set<String>> result = items.stream()
				.collect(groupingBy(Item::getPrice, mapping(Item::getName, toSet())));
		System.out.println(result);
	}
	
	
	private static class Item {
		private String name;
		private int qty;
		private BigDecimal price;
		
		@Override
		public String toString() {
			return toJson(this);
		}
		
		public Item(String name, int qty, BigDecimal price) {
			this.name = name;
			this.qty = qty;
			this.price = price;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public int getQty() {
			return qty;
		}
		
		public void setQty(int qty) {
			this.qty = qty;
		}
		
		public BigDecimal getPrice() {
			return price;
		}
		
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		
	}
}
