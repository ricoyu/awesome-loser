package com.cowforce.algorithm.search;

/**
 * <p>
 * Copyright: (C), 2022-12-16 9:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BinarySearch {
	
	public static int search(int[] nums, int value) {
		int start = 0; 
		int end = nums.length -1;
		while (start <= end) {
			int mid = (start+end) /2;
			if (value < nums[mid]) {
				end = mid-1;
			} else if (value > nums[mid]) {
				start = mid+1;
			} else {
				return mid;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		int[] nums = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10};
		int index = search(nums, 10);
		System.out.println("查找7的index是: " + index);
	}
}
