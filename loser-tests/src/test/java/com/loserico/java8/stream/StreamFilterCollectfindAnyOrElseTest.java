package com.loserico.java8.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamFilterCollectfindAnyOrElseTest {

	@Test
	public void testOldWay() {
		List<String> lines = Arrays.asList("spring", "node", "mkyong");
		List<String> result = getFilterOutput(lines, "mkyong");
		for (String temp : result) {
			System.out.println(temp); //output : spring, node
		}
	}

	@Test
	public void testFilter() {
		List<String> lines = Arrays.asList("spring", "node", "mkyong");

		List<String> result = lines.stream() // convert list to stream
				.filter(line -> !"mkyong".equals(line)) // we dont like mkyong
				.collect(Collectors.toList()); // collect the output and convert streams to a List

		result.forEach(System.out::println); //output : spring, node
	}

	@Test
	public void testFilterToughPersonOldWay() {
		List<ToughPerson> persons = Arrays.asList(
				new ToughPerson("mkyong", 30),
				new ToughPerson("jack", 20),
				new ToughPerson("lawrence", 40));

		ToughPerson result = getStudentByName(persons, "jack");
		System.out.println(result);
	}

	/**
	 * The equivalent example in Java 8, use stream.filter() to filter a List, and
	 * .findAny().orElse (null) to return an object conditional.
	 */
	@Test
	public void testFilterPersonWithfindAny() {
		List<ToughPerson> persons = Arrays.asList(
				new ToughPerson("mkyong", 30),
				new ToughPerson("jack", 20),
				new ToughPerson("lawrence", 40));

		ToughPerson person = persons.stream()
				.filter(x -> "jack".equals(x.getName()))
				.findAny()
				.orElse(null);
		System.out.println(person);

		ToughPerson result2 = persons.stream()
				.filter(x -> "ahmook".equals(x.getName()))
				.findAny()
				.orElse(null);

		System.out.println(result2);
	}

	/**
	 * For multiple condition.
	 */
	@Test
	public void testFilterPersonWithMultipalCondition() {
		List<ToughPerson> persons = Arrays.asList(
				new ToughPerson("mkyong", 30),
				new ToughPerson("jack", 20),
				new ToughPerson("lawrence", 40));

		ToughPerson result1 = persons.stream()
				.filter((p) -> "jack".equals(p.getName()) && 20 == p.getAge())
				.findAny()
				.orElse(null);

		System.out.println("result 1 :" + result1);

		//or like this
		ToughPerson result2 = persons.stream()
				.filter(p -> {
					if ("jack".equals(p.getName()) && 20 == p.getAge()) {
						return true;
					}
					return false;
				}).findAny()
				.orElse(null);

		System.out.println("result 2 :" + result2);
	}

	@Test
	public void testFilterAndMapPerson() {
		List<ToughPerson> persons = Arrays.asList(
				new ToughPerson("mkyong", 30),
				new ToughPerson("jack", 20),
				new ToughPerson("lawrence", 40));

		String name = persons.stream()
				.filter(x -> "jack".equals(x.getName()))
				.map(ToughPerson::getName) //convert stream to String
				.findAny()
				.orElse("");

		System.out.println("name : " + name);

		List<String> collect = persons.stream()
				.map(ToughPerson::getName)
				.collect(Collectors.toList());

		collect.forEach(System.out::println);
	}

	private static List<String> getFilterOutput(List<String> lines, String filter) {
		List<String> result = new ArrayList<>();
		for (String line : lines) {
			if (!"mkyong".equals(line)) { // we dont like mkyong
				result.add(line);
			}
		}
		return result;
	}

	private static ToughPerson getStudentByName(List<ToughPerson> persons, String name) {

		ToughPerson result = null;
		for (ToughPerson temp : persons) {
			if (name.equals(temp.getName())) {
				result = temp;
			}
		}
		return result;
	}
}
