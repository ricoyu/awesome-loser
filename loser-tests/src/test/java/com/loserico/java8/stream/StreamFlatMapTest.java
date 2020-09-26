package com.loserico.java8.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * https://mkyong.com/java8/java-8-flatmap-example/
 * In Java 8, Stream can hold different data types, for examples:
 * <p>
 * Stream<String[]>
 * Stream<Set<String>>
 * Stream<List<String>>
 * Stream<List<Object>>
 * <p>
 * But, the Stream operations (filter, sum, distinct…) and collectors do not support it, so, we need flatMap() to do the following conversion :
 * <p>
 * Stream<String[]>		-> flatMap ->	Stream<String>
 * Stream<Set<String>>	-> flatMap ->	Stream<String>
 * Stream<List<String>>	-> flatMap ->	Stream<String>
 * Stream<List<Object>>	-> flatMap ->	Stream<Object>
 * <p>
 * How flatMap() works :
 * <p>
 * { {1,2}, {3,4}, {5,6} } -> flatMap -> {1,2,3,4,5,6}
 * <p>
 * { {'a','b'}, {'c','d'}, {'e','f'} } -> flatMap -> {'a','b','c','d','e','f'}
 *
 * <p>
 * Copyright: Copyright (c) 2018-04-13 12:13
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class StreamFlatMapTest {
	
	@Test
	public void testStringArrayMap() {
		String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
		//Stream<String[]>
		Stream<String[]> temp = Arrays.stream(data);
		
		//filter a stream of string[], and return a string[]?
		Stream<String[]> stream = temp.filter(x -> "a".equals(x.toString()));
		stream.forEach(System.out::println); //什么都不输出
	}
	
	@Test
	public void testStringArrayFlatMap() {
		String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
		//Stream<String[]>
		Stream<String[]> temp = Arrays.stream(data);
		
		//Stream<String>, GOOD!
		Stream<String> stringStream = temp.flatMap(x -> Arrays.stream(x));
		Stream<String> stream = stringStream.filter(x -> "a".equals(x));
		stream.forEach(System.out::println); //输出a
	}
	
	@Test
	public void testFlatMapSet() {
		Student obj1 = new Student();
		obj1.setName("mkyong");
		obj1.addBook("Java 8 in Action");
		obj1.addBook("Spring Boot in Action");
		obj1.addBook("Effective Java (2nd Edition)");
		
		Student obj2 = new Student();
		obj2.setName("zilap");
		obj2.addBook("Learning Python, 5th Edition");
		obj2.addBook("Effective Java (2nd Edition)");
		
		List<Student> list = new ArrayList<>();
		list.add(obj1);
		list.add(obj2);
		
		List<String> bookList = list.stream()
				.map(Student::getBooks) //Stream<Set<String>>
				.flatMap(x -> x.stream()) //Stream<String>
				.distinct()
				.collect(toList());
		bookList.forEach(System.out::println);
	}
	
	@Test
	public void testFlatMapToInt() {
		int[] intArray = {1, 2, 3, 4, 5, 6};
		//1. Stream<int[]>
		Stream<int[]> streamArray = Stream.of(intArray);
		
		//2. Stream<int[]> -> flatMap -> IntStream
		IntStream intStream = streamArray.flatMapToInt(x -> Arrays.stream(x));
		intStream.forEach(System.out::println);
	}
	
	/**
	 * A stream can hold complex data structures like Stream<List<String>>. 
	 * In cases like this, flatMap() helps us to flatten the data structure to simplify further operations:
	 */
	@Test
	public void testwhenFlatMapEmployeeNames_thenGetNameStream() {
		List<List<String>> namedNested = asList(
				asList("jeff", "Bezos"),
				asList("Bill", "Gates"),
				asList("Mark", "Zuckerberg")
		);
		
		List<String> namesFlatStream = namedNested.stream()
				.flatMap(Collection::stream)
				.collect(toList());
		
		assertEquals(namesFlatStream.size(), namedNested.size() * 2);
	}
}
