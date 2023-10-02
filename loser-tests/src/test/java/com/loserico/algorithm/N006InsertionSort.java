package com.loserico.algorithm;

import org.junit.Test;

import java.util.Arrays;

/**
 * <p>
 * Copyright: (C), 2023-09-08 11:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class N006InsertionSort {
	
	@Test
	public void test() {
		int[] nums = {6, 5, 4, 3, 7, 8, 9, 1, 0};
		System.out.println(Arrays.toString(nums));
		insertionSort(nums);
		System.out.println(Arrays.toString(nums));
	}
	
	public void insertionSort(int[] nums) {
		if (nums == null || nums.length <= 1) {
			return;
		}
		
		for (int i = 1; i < nums.length; i++) {
			int key = nums[i];
			int j = i - 1;
			while (j >= 0 && nums[j] > key) {
				nums[j + 1] = nums[j]; //后移一位
				j--;
			}
			nums[j + 1] = key;
		}
	}
}
