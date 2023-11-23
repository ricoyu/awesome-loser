package com.cowforce.algorithm;

import com.cowforce.algorithm.sort.PrintArray;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2022-12-19 8:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AlgorithmTest {
	static Map<Integer, Integer> storeMap = new HashMap<>();
	
	@Test
	public void testClimingStairs() {
		
	}
	
	@Test
	public void testSelectSort() {
		selectSort(PrintArray.SRC);
		PrintArray.print(PrintArray.SRC);
	}
	
	public static int climbStairs(int n) {
		if (n == 1) {
			return 1;
		}
		if (n == 2) {
			return 2;
		}
		
		Integer result = storeMap.get(n);
		if (result == null) {
			result = climbStairs(n - 1) + climbStairs(n - 2);
			storeMap.put(n, result);
		}
		return result;
	}
	
	@Test
	public void testMergeArray() {
		int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
		int[] nums2 = new int[]{2, 5, 6};
		merge2(nums1, 3, nums2, 3);
		PrintArray.print(nums1);
	}
	
	@Test
	public void testBubbleSort() {
		int[] result = bubbleSort(PrintArray.SRC);
		PrintArray.print(result);
	}
	
	public static int[] bubbleSort(int[] nums) {
		if (nums == null || nums.length < 2) {
			return nums;
		}
		
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums.length - 1 - i; j++) {
				if (nums[j] > nums[j + 1]) {
					swap(nums, j, j + 1);
				}
			}
		}
		
		return nums;
	}
	
	@Test
	public void testInsertSort() {
		PrintArray.print(PrintArray.SRC);
		System.out.println("=======================");
		int[] result = insertSort(PrintArray.SRC);
		PrintArray.print(result);
	}
	
	public static int[] insertSort(int[] nums) {
		for (int i = 0; i < nums.length-1; i++) {
			int sortedIndex = i;
			int currentValue = nums[i+1];
			while (sortedIndex >=0 && currentValue < nums[sortedIndex]) {
				nums[sortedIndex+1] = nums[sortedIndex];
				sortedIndex--;
			}
			nums[sortedIndex+1] = currentValue;
		}
		return nums;
	}
	
	public static int[] selectSort(int[] nums) {
		if (nums == null || nums.length < 2) {
			return nums;
		}
		
		for (int i = 0; i < nums.length; i++) {
			int minIndex = 0;
			for (int j = 0; j < nums.length; i++) {
				if (nums[j] < nums[minIndex]) {
					minIndex = j;
				}
			}
			swap(nums, 0, minIndex);
		}
		
		return nums;
	}
	
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	public static void merge2(int[] nums1, int m, int[] nums2, int n) {
		int k = m + n;
		int inserted = 0;
		int beginIndex = m - 1;
		for (int i = 0; i < n; i++) {
			int index = beginIndex;
			while (index > 0 && nums2[i] < nums1[index]) {
				nums1[index + 1] = nums1[index]; //后移一位
				index--;
				beginIndex++;
			}
			if (index == -1) {
				index = 0;
			}
			nums1[index] = nums2[i];
		}
	}
	
	public static void merge(int[] nums1, int m, int[] nums2, int n) {
		int k = m + n;
		int[] temp = new int[k];
		for (int index = 0, num1Index = 0, num2Index = 0; index < k; index++) {
			if (num1Index >= m) {
				while (num2Index < n) {
					temp[index] = nums2[num2Index++];
				}
			} else if (num2Index >= n) {
				while (num1Index < m) {
					temp[index] = nums1[num1Index++];
				}
			} else if (nums1[num1Index] < nums2[num2Index]) {
				temp[index] = nums1[num1Index++];
			} else {
				temp[index] = nums2[num2Index++];
			}
		}
		
		for (int i = 0; i < k; i++) {
			nums1[i] = temp[i];
		}
	}
}
