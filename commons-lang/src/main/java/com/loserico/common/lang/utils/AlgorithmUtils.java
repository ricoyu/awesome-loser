package com.loserico.common.lang.utils;

/**
 * 提供随机数
 * <p>
 * Copyright: (C), 2023-09-09 20:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class AlgorithmUtils {
	
	/**
	 * 返回一个数组, 数组长度是<=指定长度, 数组的每个元素不超过指定的最大值
	 * 数组元素可能存在重复, 但是相邻两个数肯定不等
	 *
	 * @param len      数组的最大长度
	 * @param maxValue 数组每个元素的最大值
	 * @return
	 */
	public static int[] randomArr(int len, int maxValue) {
		do {
			len = (int) (Math.random() * len);
		} while (len == 0);
		int[] nums = new int[len];
		nums[0] = (int) (Math.random() * maxValue);
		for (int i = 1; i < len; i++) {
			do {
				nums[i] = (int) (Math.random() * maxValue);
			} while (nums[i - 1] == nums[i]);
		}
		return nums;
	}
	
	public static void printArray(int[] nums) {
		if (nums == null || nums.length == 0) {
			return;
		}
		
		System.out.print("{");
		for (int i = 0; i < nums.length; i++) {
			if (i == nums.length - 1) {
				System.out.print(nums[i]);
			} else {
				System.out.print(nums[i] + ", ");
			}
		}
		System.out.println("}");
	}
	
	/**
	 * 选择排序(Selection Sort()是一种简单的排序算法, 它的基本思想是每次从待排序的元素中选择最小(或最大)的元素, 然后将其放到已排序序列的末尾。
	 * <p>
	 * 具体步骤:
	 * <ol>
	 *     <li/>首先, 找到数组中最小的那个元素(升序)
	 *     <li/>其次, 将它和数组的第一个元素交换位置(如果第一个元素就是最小元素那么它就和自己交换)
	 *     <li/>再次, 在剩下的元素中找到最小的元素, 将它与数组的第二个元素交换位置。如此往复, 直到将整个数组排序。
	 * </ol>
	 *
	 * @param nums
	 */
	public static void selectionSort(int[] nums) {
		if (nums == null || nums.length <= 1) {
			return;
		}
		
		for (int i = 0; i < nums.length; i++) {
			int minIndex = i;
			for (int j = i + 1; j < nums.length; j++) {
				if (nums[j] < nums[minIndex]) {
					minIndex = j;
				}
			}
			swap(nums, i, minIndex);
			
		}
	}
	
	/**
	 * 冒泡排序是一种简单的排序算法, 它通过比较相邻的元素并交换它们的位置, 使得较大的元素逐渐"浮"到数组的末尾
	 */
	public static void bubbleSort(int[] nums) {
		if (nums == null || nums.length<=1) {
			return;
		}
		
		//这外层的for循环用i来代表有多少个最大的元素已经冒泡到数组的末尾了
		//所以内层的for循环j要<j < nums.length-i-1
		for (int i = 0; i < nums.length; i++) {
			boolean sorted= true; //优化点
			for (int j = 0; j < nums.length-i-1; j++) {
				if (nums[j] > nums[j+1]) {
					swap(nums, j, j+1);
					sorted=false;
				}
			}
			//没有发生交换表示整个数组已经有序
			if (sorted) {
				return;
			}
		}
	}
	
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	/**
	 * 二分查找, 找到返回数组下标, 找不到返回-1
	 *
	 * @param nums
	 * @param num
	 * @return
	 */
	public static int binarySearch(int[] nums, int num) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		int left = 0;
		int right = nums.length - 1;
		while (left <= right) {
			int middle = left + (right - left) / 2;
			if (nums[middle] < num) {
				left = middle + 1;
			} else if (nums[middle] > num) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}
}
