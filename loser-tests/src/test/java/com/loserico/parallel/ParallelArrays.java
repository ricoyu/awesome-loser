package com.loserico.parallel;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelArrays {
	public static void main(String[] args) {
		long[] arrayOfLong = new long[20000];
		Arrays.parallelSetAll(arrayOfLong,
				index -> ThreadLocalRandom.current().nextInt(1000000));
		// 273431 967460 815469 664191 504613 61249 625971 339805 886400 532959
		Arrays.stream(arrayOfLong).limit(10).forEach(
				i -> System.out.print(i + " "));
		System.out.println();
		Arrays.parallelSort(arrayOfLong);
		// 31 131 207 250 349 377 436 498 536 589
		Arrays.stream(arrayOfLong).limit(10).forEach(
				i -> System.out.print(i + " "));
		System.out.println();
	}
}