package com.loserico.java8;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.Arrays.asList;

/**
 * The Comparator interface has a number of useful new methods, taking advantage
 * of the fact that interfaces can now have concrete methods.
 * <p>
 * Copyright: Copyright (c) 2018-03-31 18:41
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class ComparatorTest {

	private static List<Person> persons = null;

	@BeforeClass
	public static void preparePerson() {
		persons = asList(new Person("xuehua", "yu"),
				new Person("yuhai", "lu"),
				new Person("vivi", "qian"),
				new Person("vivi", "born"));
	}

	/*
	 * 先按firstName排序，再按lastName排序
	 */
	@Test
	public void testCompare() {
		persons.sort(Comparator.comparing(Person::getFirstName).thenComparing(Person::getLastName));
		persons.forEach(System.out::println);
	}

	/**
	 * I want to sort the list of strings in a descending manner with length and they
	 * have the same length, I will sort in reverse natural ordering
	 */
	@Test
	public void testCompareTriditionalWay() {
		List<String> words = asList("a", "hello", "hi", "there");
		words.sort(new Comparator<String>() {
			public int compare(String s1, String s2) {
				if (s2.length() == s1.length()) {
					return s1.compareTo(s2);
				} else {
					return s2.length() - s1.length();
				}
			}
		});
		words.forEach(System.out::println);
	}

	/**
	 * But, with Java 8, rather than specifying how to do that, this can be
	 * accomplished in a more declarative way with static methodscomparing(),
	 * thenComparing() and reverseOrder() as:
	 */
	@Test
	public void testCompareWithJava8() {
		List<String> words = asList("a", "hello", "hi", "there");
		words.sort(Comparator.comparing(String::length).thenComparing(Comparator.reverseOrder()).reversed());
		words.forEach(System.out::println);
	}

	public static class Person {

		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		private String firstName;
		private String lastName;

		@Override
		public String toString() {
			return toJson(this);
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

	}
}
