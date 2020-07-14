package com.loserico.java8.stream;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Example3Test {

	public static void main(String[] args) {
		int[] intArray = { 1, 2, 3, 4, 5, 6 };
		// 1. Stream<int[]>
		Stream<int[]> streamArray = Stream.of(intArray);
		// 2. Stream<int[]> -> flatMap -> IntStream
		IntStream intStream = streamArray.flatMapToInt(x -> Arrays.stream(x));
//		intStream.forEach(x -> System.out.println(x));
		intStream.forEach(System.out::println);
	}

}