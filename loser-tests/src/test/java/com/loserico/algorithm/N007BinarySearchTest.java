package com.loserico.algorithm;

import com.loserico.common.lang.utils.AlgorithmUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2023-09-09 20:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class N007BinarySearchTest {
	
	@Test
	public void test() {
		//int[] nums = RandomUtils.randomArr(6, 10);
		//selectionSort(nums);
		int[] nums = {0, 2, 4, 4, 7, 9};
		AlgorithmUtils.printArray(nums);
		int result = binarySearch(nums, 3);
		System.out.println(result);
	}
	
	/**
	 * 选择排序(Selection Sort()是一种简单的排序算法, 它的基本思想是每次从待排序的元素中选择最小(或最大)的元素, 然后将其放到已排序序列的末尾。
	 *
	 * 具体步骤:
	 * <ol>
	 *     <li/>首先, 找到数组中最小的那个元素(升序)
	 *     <li/>其次, 将它和数组的第一个元素交换位置(如果第一个元素就是最小元素那么它就和自己交换)
	 *     <li/>再次, 在剩下的元素中找到最小的元素, 将它与数组的第二个元素交换位置。如此往复, 直到将整个数组排序。
	 * </ol>
	 * @param nums
	 */
	public void selectionSort(int[] nums) {
		if (nums== null || nums.length<=1) {
			return;
		}
		
		for (int i = 0; i < nums.length; i++) {
			int minIndex = i;
			for (int j = i+1; j < nums.length; j++) {
				if (nums[j] < nums[minIndex]) {
					minIndex = j;
				}
			}
			swap(nums, i, minIndex);
			
		}
	}
	
	
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	/**
	 * 二分查找, 找到返回数组下标, 找不到返回-1
	 * @param nums
	 * @param num
	 * @return
	 */
	public int binarySearch(int[] nums, int num) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		int left = 0;
		int right = nums.length-1;
		while (left <right) {
			int middle = (right - left) / 2;
			if (nums[middle] < num) {
				left =middle+1;
			} else if (nums[middle] > num) {
				right = middle-1;
			} else {
				return middle;
			}
		}
		return -1;
	}
}
