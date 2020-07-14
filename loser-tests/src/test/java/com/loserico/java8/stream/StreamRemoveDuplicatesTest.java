package com.loserico.java8.stream;

import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public class StreamRemoveDuplicatesTest {

	@Test
	public void testRemoveDuplicates() {
		List<Employee> employee = Arrays.asList(new Employee(1, "John"), new Employee(1, "Bob"),
				new Employee(2, "Alice"));
		List<Employee> unique = employee.stream()
				.collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparingInt(Employee::getId))),
						ArrayList::new));
		System.out.println(JacksonUtils.toJson(unique));
	}

	private class Employee {
		private int id;

		private String name;

		public Employee(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
