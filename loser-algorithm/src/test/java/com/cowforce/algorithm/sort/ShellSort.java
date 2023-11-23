package com.cowforce.algorithm.sort;

/**
 * <p>
 * Copyright: (C), 2022-12-12 10:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ShellSort {
	
	public static void sort(int[] nums) {
		if (nums == null || nums.length <= 1) {
			return;
		}
		int len = nums.length;
		//按增量分组后，每个分组中, currentValue代表当前待排序数据, 该元素之前的组内元素均已被排序过
		int currentValue = 0;
		//gap指用来分组的增量，会依次递减
		int gap = len / 2; //第一次是6
		//86, 39, 77, 23, 32, 45, 58, 63, 93, 4, 37, 22
		while (gap > 0) {
			for (int i = gap; i < len; i++) {
				currentValue = nums[i]; //第一次是58
				//组内已被排序数据的索引
				int sortedIndex = i - gap; // 第一次sortedIndex是0
				/*
				 * 在组内已被排序过数据中倒序寻找合适的位置, 如果当前待排序数据比比较的元素要小, 则将比较的元素在组内后移一位
				 */
				while (sortedIndex >= 0 && nums[sortedIndex] > currentValue) {
					nums[sortedIndex + gap] = nums[sortedIndex]; //把第一个元素86移到58的位置上
					sortedIndex -= gap; //第一次变成-6?
				}
				//while循环结束时，说明已经找到了当前待排序数据的合适位置，插入
				nums[sortedIndex + gap] = currentValue; //将58插入到index为0的位置
			}
			System.out.println("本轮增量【" + gap + "】排序后的数组");
			PrintArray.print(nums);
			System.out.println("--------------------");
			gap /= 2;
		}
	}
	
	public static void main(String[] args) {
		PrintArray.print(PrintArray.SRC);
		System.out.println("============================================");
		ShellSort.sort(PrintArray.SRC);
		PrintArray.print(PrintArray.SRC);
	}
}
