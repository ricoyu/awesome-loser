package com.loserico.java8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Example2Test {

	public static void main(String[] args) {

		Student mkyong = new Student();
		mkyong.setName("mkyong");
		mkyong.addBook("Java 8 in Action");
		mkyong.addBook("Spring Boot in Action");
		mkyong.addBook("Effective Java (2nd Edition)");

		Student zilap = new Student();
		zilap.setName("zilap");
		zilap.addBook("Learning Python, 5th Edition");
		zilap.addBook("Effective Java (2nd Edition)");

		List<Student> list = new ArrayList<>();
		list.add(mkyong);
		list.add(zilap);

		List<String> collect = list.stream()
				.map(x -> x.getBooks()) // Stream<Set<String>>
				.flatMap(x -> x.stream()) // Stream<String>
				.distinct()
				.collect(Collectors.toList());

		collect.forEach(x -> System.out.println(x));
	}

}