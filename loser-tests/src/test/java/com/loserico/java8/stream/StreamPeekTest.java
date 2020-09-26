package com.loserico.java8.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

/**
 * https://stackify.com/streams-guide-java-8/
 *
 * <p>
 * Copyright: (C), 2020-09-24 15:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StreamPeekTest {
	
	/**
	 * We saw forEach() earlier in this section, which is a terminal operation.
	 * However, sometimes we need to perform multiple operations on each element of the stream before any terminal operation is applied.
	 * <p>
	 * peek() can be useful in situations like this.
	 * Simply put, it performs the specified operation on each element of the stream and returns a new stream which can be used further.
	 * peek() is an intermediate operation:
	 */
	@Test
	public void testwhenIncrementSalaryUsingPeek_thenApplyNewSalary() {
		Employee[] arrayOfEmps = {
				new Employee(1, "Jeff Bezos", 100000.0),
				new Employee(2, "Bill Gates", 200000.0),
				new Employee(3, "Mark Zuckerberg", 300000.0)
		};
		
		List<Employee> empList = asList(arrayOfEmps);
		
		empList.stream()
				.peek(emp -> emp.salaryIncrement(10.0))
				.peek(System.out::println)
				.collect(toList());
		
		assertThat(empList, contains(
				hasProperty("salary", equalTo(100010.0)),
				hasProperty("salary", equalTo(200010.0)),
				hasProperty("salary", equalTo(300010.0))
		));
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Employee {
		private int id;
		
		private String name;
		
		private double salary;
		
		public void salaryIncrement(double incr) {
			this.salary += incr;
		}
	}
}
