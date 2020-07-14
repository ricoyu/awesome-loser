package com.loserico.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 对集合类型随机排序
 * http://www.baeldung.com/java-shuffle-collection?utm_source=drip&utm_medium=email&utm_campaign=Latest+article+about+Java+%E2%80%93+on+Baeldung
 * In this quick tutorial, we saw how to use java.util.Collections.shuffle to shuffle
 * various collections in Java.
 * 
 * This naturally works directly with a List, and we can utilize it indirectly to
 * randomize the order of elements in other collections as well. We can also control
 * the shuffling process by providing a custom source of randomness and make it
 * deterministic.
 * 
 * <p>
 * Copyright: Copyright (c) 2018-03-04 11:22
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class ShuffleTest {

	/**
	 * We’ll use the method java.util.Collections.shuffle, which takes as input a List
	 * and shuffles it in-place. By in-place, we mean that it shuffles the same list
	 * as passed in input instead of creating a new one with shuffled elements.
	 */
	@Test
	public void testCollectionsShuffle() {
		List<String> students = Arrays.asList("Foo", "Bar", "Baz", "Qux");
		Collections.shuffle(students);
		students.forEach(System.out::print);
	}

	/**
	 * When using identical sources of randomness (initialized from same seed value),
	 * the generated random number sequence will be the same for both shuffles. Thus,
	 * after shuffling, both lists will contain elements in the exact same order.
	 */
	@Test
	public void testShuffleWithGivenRandomness() {
		List<String> students_1 = Arrays.asList("Foo", "Bar", "Baz", "Qux");
		List<String> students_2 = Arrays.asList("Foo", "Bar", "Baz", "Qux");

		int seedValue = 10;
		Collections.shuffle(students_1, new Random(seedValue));
		Collections.shuffle(students_2, new Random(seedValue));

		students_1.forEach(System.out::print);
		System.out.println();
		students_2.forEach(System.out::print);
	}

	/**
	 * We may want to shuffle other collections as well such as Set, Map, or Queue,
	 * for example, but all these collections are unordered — they don’t maintain any
	 * specific order.
	 * 
	 * Some implementations, such as LinkedHashMap, or a Set with a Comparator – do
	 * maintain a fixed order, thus we cannot shuffle them either.
	 * 
	 * However, we can still access their elements randomly by converting them first
	 * into a List, then shuffling this List.
	 */
	@Test
	public void testShuffleUnorderedCollections() {
		Map<Integer, String> studentsById = new HashMap<>();
		studentsById.put(1, "Foo");
		studentsById.put(2, "Bar");
		studentsById.put(3, "Baz");
		studentsById.put(4, "Qux");

		List<Map.Entry<Integer, String>> shuffledStudentEntries = new ArrayList<>(studentsById.entrySet());
		shuffledStudentEntries.stream().map(Map.Entry::getValue).forEach(System.out::print);
		System.out.println();
		Collections.shuffle(shuffledStudentEntries);
		shuffledStudentEntries.stream().map(Map.Entry::getValue).forEach(System.out::print);

		//Similarly, we can shuffle elements of a Set:
		System.out.println("\n");
		Set<String> students = new HashSet<>(
				Arrays.asList("Foo", "Bar", "Baz", "Qux"));
		List<String> studentList = new ArrayList<>(students);
		studentList.forEach(System.out::print);
		System.out.println();
		Collections.shuffle(studentList);
		studentList.forEach(System.out::print);
	}
}
