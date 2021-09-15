package com.loserico.java8.stream;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * <p>
 * Copyright: (C), 2021-09-10 15:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StreamHashMapTest {
	
	
	@Test
	public void testListToMap() {
		List<Hosting> list = new ArrayList<>();
		list.add(new Hosting(1, "liquidweb.com", 80000));
		list.add(new Hosting(2, "linode.com", 90000));
		list.add(new Hosting(3, "digitalocean.com", 120000));
		list.add(new Hosting(4, "aws.amazon.com", 200000));
		list.add(new Hosting(5, "mkyong.com", 1));
		
		// key = id, value - websites
		Map<Integer, String> result1 = list.stream().collect(toMap(Hosting::getId, Hosting::getName));
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
	
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}
	
	@Test
	public void testList2MapGroupingByMapping() {
		//3 apple, 2 banana, others 1
		List<StreamHashMapTest.Item> items = Arrays.asList(
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
	
	
	@Test
	public void testList2MapWithDuplicateKey() {
		List<Hosting> list = new ArrayList<>();
		list.add(new Hosting(1, "liquidweb.com", 80000));
		list.add(new Hosting(2, "linode.com", 90000));
		list.add(new Hosting(3, "digitalocean.com", 120000));
		list.add(new Hosting(4, "aws.amazon.com", 200000));
		list.add(new Hosting(5, "mkyong.com", 1));
		
		list.add(new Hosting(6, "linode.com", 100000)); // new line
		
		// key = name, value - websites , but the key 'linode' is duplicated!?
		//Map<String, Long> result1 = list.stream().collect(toMap(Hosting::getName, Hosting::getWebsites));//java.lang.IllegalStateException: Duplicate key 90000
		//System.out.println("Result 1 : " + result1);
		
		//To solve the duplicated key issue above, pass in the third mergeFunction argument like this
		Map<String, Long> result1 = list.stream()
				//(oldValue, newValue) -> oldValue ==> If the key is duplicated, do you prefer oldKey or newKey?
				//.collect(toMap(Hosting::getName, Hosting::getWebsites, (oldValue, newValue) -> oldValue));
				.collect(toMap(Hosting::getName, Hosting::getWebsites, (oldValue, newValue) -> newValue));
		System.out.println("Result 1 : " + result1);
	}
	
	
	/**
	 * The below example will print an empty result, because filter() has no
	 * idea how to filter a stream of String[]
	 */
	@Test
	public void testFlatMap1() {
		String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
		// Stream<String[]>
		Stream<String[]> temp = Arrays.stream(data);
		// filter a stream of string[], and return a string[]?
		Stream<String[]> stream = temp.filter(x -> "a".equals(x.toString()));
		// 不打印
		stream.forEach(System.out::println);
	}
	
	@Test
	public void testFlatMap2() {
		String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
		// Stream<String[]>
		Stream<String[]> temp = Arrays.stream(data);
		// Stream<String>, GOOD!
		Stream<String> stringStream = temp.flatMap(x -> Arrays.stream(x));
		Stream<String> stream = stringStream.filter(x -> "a".equals(x));
		// a
		stream.forEach(System.out::println);
	}
	
	@Test
	public void testFlatMap() {
		Map<String, List<LocalDateTime>> dates = new HashMap<String, List<LocalDateTime>>();
		dates.put("today", asList(LocalDateTime.now(), LocalDateTime.of(2017, 03, 29, 11, 9)));
		dates.put("yestoday", asList(LocalDateTime.of(2017, 03, 28, 11, 9), LocalDateTime.of(2017, 03, 28, 11, 9)));
		dates.values().stream()
				.flatMap(dateList -> dateList.stream())
				.forEach(obj -> System.out.println(obj.getClass()));
	}
	
	@Test
	public void testList2MapWithSort() {
		List<Hosting> list = new ArrayList<>();
		list.add(new Hosting(1, "liquidweb.com", 80000));
		list.add(new Hosting(2, "linode.com", 90000));
		list.add(new Hosting(3, "digitalocean.com", 120000));
		list.add(new Hosting(4, "aws.amazon.com", 200000));
		list.add(new Hosting(5, "mkyong.com", 1));
		list.add(new Hosting(6, "linode.com", 100000));
		
		Map<String, Long> result1 = list.stream()
				.sorted(Comparator.comparingLong(Hosting::getWebsites).reversed())
				.collect(toMap(
						Hosting::getName,
						Hosting::getWebsites, // key = name, value = websites
						(oldValue, newValue) -> oldValue, // if same key, take the old key
						LinkedHashMap::new)); //returns a LinkedHashMap, keep order
		System.out.println("Result 1 : " + result1);
	}
	
	/*
	 * Group by the name + Count or Sum the Qty.
	 */
	@Test
	public void testList2MapCountSum() {
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
		
		Map<String, Long> counting = items.stream().collect(groupingBy(Item::getName, counting()));
		System.out.println(counting);
		
		Map<String, Integer> sum = items.stream().collect(groupingBy(Item::getName, summingInt(Item::getQty)));
		System.out.println(sum);
	}
	
	@Test
	public void testList2MapLocale() {
		Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
		Map<String, String> languageNames = locales.collect(toMap(
				l -> l.getDisplayLanguage(),
				l -> l.getDisplayLanguage(l),
				(existingValue, newValue) -> existingValue));
		System.out.println(toJson(languageNames));
	}
	
	/**
	 * However, suppose we want to know all languages in a given country.
	 * Then we need a Map<String, Set<String>>. For example, the value for
	 * "Switzerland" is the set [French, German, Italian]. At first, we
	 * store a singleton set for each language. Whenever a new language is
	 * found for a given country, we form the union of the existing and the
	 * new set.
	 */
	@Test
	public void testList2MapSet() {
		Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
		Map<String, Set<String>> countryLanguageSets = locales.collect(toMap(
				l -> l.getDisplayCountry(),
				l -> Collections.singleton(l.getDisplayLanguage()),
				(a, b) -> {
					// Union of a and b
					Set<String> r = new HashSet<>(a);
					r.addAll(b);
					return r;
				}));
		System.out.println(toJson(countryLanguageSets));
	}
	
	@Test
	public void testGroupingByCount() {
		//3 apple, 2 banana, others 1
		List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
		Map<String, Long> result = items.stream().collect(groupingBy(identity(), counting()));
		System.out.println(result);
	}
	
	@Test
	public void testGroupingByCountWithSorting() {
		//3 apple, 2 banana, others 1
		List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
		Map<String, Long> result = items.stream().collect(groupingBy(identity(), counting()));
		System.out.println(result);
		
		Map<String, Long> finalMap = new LinkedHashMap<>();
		//Sort a map and add to finalMap
		result.entrySet().stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
		System.out.println(finalMap);
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
