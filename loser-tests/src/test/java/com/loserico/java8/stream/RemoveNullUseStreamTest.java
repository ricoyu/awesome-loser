package com.loserico.java8.stream;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * https://github.com/eugenp/tutorials/tree/master/core-java
 * 
 * @on
 * @author Rico Yu	ricoyu520@gmail.com
 * @since 2017-04-19 13:33
 * @version 1.0
 *
 */
public class RemoveNullUseStreamTest {

	@Test
	public void testRemoveNulls() {
		//		asList(null, 1, 2, null, 3, null).parallelStream()
		//				.filter(Objects::nonNull)
		//				.collect(Collectors.toList());

	}

	@Test
	public void testRemoveDuplicate() {
		asList(1, 1, 2, 2, 3, 3).stream()
				.distinct()
				.collect(toList())
				.forEach(System.out::println);
	}
}
