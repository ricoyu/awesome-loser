package com.cowforce.algorithm.sort;

/**
 * <p>
 * Copyright: (C), 2022-12-09 8:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ChoiceSort {
	
	public static void sort(int[] nums) {
		if (nums== null || nums.length == 0) {
			return;
		}
		
		for (int i = 0; i < nums.length; i++) {
			int minIndex = i; //最小数的下标，记录每次找到的最小的那个数; 每个循环开始总是假设第一个数最小
			for (int j = i; j < nums.length; j++) {
				if (nums[j] < nums[minIndex]) {
					minIndex = j;
				}
			}
			System.out.println("最小数为: " + nums[minIndex]);
			//交换最小数和i当前所指的元素
			int temp = nums[i];
			nums[i] = nums[minIndex];
			nums[minIndex] = temp;
			System.out.println("---------------");
			PrintArray.print(nums);
		}
	}
	
	public static void main(String[] args) {
		PrintArray.print(PrintArray.SRC);
		System.out.println("============================================");
		ChoiceSort.sort(PrintArray.SRC);
		PrintArray.print(PrintArray.SRC);
	}
}
