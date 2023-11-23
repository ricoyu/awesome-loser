package com.cowforce.algorithm.sort;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2022-12-16 10:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SortTest {
	
	@Test
	public void testBubbleSort() {
		int[] nums = PrintArray.SRC;
		PrintArray.print(nums);
		System.out.println("======================================");
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums.length - i - 1; j++) {
				if (nums[j] > nums[j + 1]) {
					int temp = nums[j + 1];
					nums[j + 1] = nums[j];
					nums[j] = temp;
				}
			}
		}
		PrintArray.print(nums);
	}
	
	@Test
	public void testSelectSort() {
		int[] nums = PrintArray.SRC;
		PrintArray.print(nums);
		System.out.println("======================================");
		for (int i = 0; i < nums.length; i++) {
			int minIndex = i;
			for (int j = i + 1; j < nums.length; j++) {
				if (nums[j] < nums[minIndex]) {
					minIndex = j;
				}
			}
			swap(nums, i, minIndex);
		}
		PrintArray.print(nums);
	}
	
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	@Test
	public void testQuickSort() {
		int start = 0;
		int end = PrintArray.SRC.length - 1;
		PrintArray.print(PrintArray.SRC);
		System.out.println("==================");
		quickSort(PrintArray.SRC, start, end);
		PrintArray.print(PrintArray.SRC);
	}
	
	public static int[] quickSort(int[] nums, int start, int end) {
		if (nums == null || nums.length < 2 || start == end) {
			return nums;
		}
		
		int position = partition(nums, start, end);
		if (start < position && position < end) {
			quickSort(nums, start, position);
			quickSort(nums, position + 1, end);
		}
		
		return nums;
	}
	
	public static int partition(int[] nums, int start, int end) {
		if (start == end) {
			return start;
		}
		int value = nums[start];
		swap(nums, start, end);
		int zoneIndex = start - 1; //分区指示器
		for (int i = start; i <= end; i++) {
			if (nums[i] <= value) {
				zoneIndex++;
				if (i > zoneIndex) {
					swap(nums, zoneIndex, i);
				}
			}
		}
		
		return zoneIndex;
	}
}
