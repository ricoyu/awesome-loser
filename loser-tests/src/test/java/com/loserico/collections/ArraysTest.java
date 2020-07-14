package com.loserico.collections;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ArraysTest {

	@Test
	public void testParallelSort() {
		long[] arrayOfLong = new long[20000];
		Arrays.parallelSetAll(arrayOfLong,
				index -> ThreadLocalRandom.current().nextInt(1000000));
		// 273431 967460 815469 664191 504613 61249 625971 339805 886400 532959
		Arrays.stream(arrayOfLong).limit(10).forEach(i -> System.out.print(i + " "));
		System.out.println();
		Arrays.parallelSort(arrayOfLong);
		// 31 131 207 250 349 377 436 498 536 589
		Arrays.stream(arrayOfLong).limit(10).forEach(System.out::println);
		System.out.println();
	}

	@Test
	public void testArrayVsArrayList() {
		LocalDateTime[] localDateTimes = new LocalDateTime[10];
		List<LocalDateTime> localDateTimes2 = new ArrayList<>();

		long begin = System.currentTimeMillis();
		//		for (int i = 0; i < 100000; i++) {
		//			for (int j = 0; j < 10; j++) {
		//				localDateTimes[j] = LocalDateTime.now();
		//			}
		//		}
		//		System.out.println("数组形式花费" + (System.currentTimeMillis() - begin) + " 毫秒");

		for (int i = 0; i < 100000; i++) {
			for (int j = 0; j < 10; j++) {
				localDateTimes2.set(j, LocalDateTime.now());
			}
		}
		System.out.println("ArrayList形式花费" + (System.currentTimeMillis() - begin) + " 毫秒");
	}

	@Test
	public void testList2ArrayJava8() {
		List<Integer> sourceList = Arrays.asList(0, 1, 2, 3, 4, 5);
		Integer[] targetArray = sourceList.stream().toArray(Integer[]::new);
		for (int i = 0; i < targetArray.length; i++) {
			Integer value = targetArray[i];
			System.out.println(value);
		}
	}
	
	@Test
	public void testList2Array() {
		List<Integer> sourceList = Arrays.asList(0, 1, 2, 3, 4, 5);
		Integer[] targetArray = sourceList.toArray(new Integer[sourceList.size()]);
		for (int i = 0; i < targetArray.length; i++) {
			Integer value = targetArray[i];
			System.out.println(value);
		}
	}

	@Test
	public void testList2ArrayUsingGuava() {
		List<Integer> sourceList = Lists.newArrayList(0, 1, 2, 3, 4, 5);
		int[] targetArray = Ints.toArray(sourceList);
		for (int i = 0; i < targetArray.length; i++) {
			Integer value = targetArray[i];
			System.out.println(value);
		}
	}
}
