package com.cowforce.algorithm.sort;

import java.util.Arrays;

/**
 * 计数排序
 * <p>
 * Copyright: (C), 2022-12-15 9:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CountingSort {
	
	public static int[] sort(int[] nums) {
		if (nums == null ||nums.length==0) {
			return nums;
		}
		
		/*
		 * 寻找数组中最大值, 最小值
		 * bias:偏移量, 用以定位原始数组每个元素在计数数组中的下标位置
		 */
		int bias, min = nums[0], max = nums[0];
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] > max) {
				max = nums[i];
			}
			if (nums[i]< min) {
				min = nums[i];
			}
		}
		bias = 0 - min;
		//获得计数数组的容量
		int[] countingArray = new int[max - min + 1];
		Arrays.fill(countingArray, 0);
		/*
		 * 遍历整个原始数组, 将原始数组中每个元素值转化为计数数组下标, 并将计数数组下标对应的元素值大小进行累加
		 */
		for (int i = 0; i < nums.length; i++) {
			countingArray[nums[i]+ bias]++;
		}
		System.out.println("计数数组为：");
		PrintArray.print(countingArray);
		System.out.println("============================================");
		int index = 0; //访问原始数组时的下标计数器
		int i = 0; //访问计数数组时的下标计数器
		//访问计数数组，将计数数组中的元素转换后，重新写回原始数组
		while (index < nums.length) {
			//只要计数数组中当前下标元素的值不为0，就将计数数组中的元素转换后，重新写回原始数组
			if (countingArray[i] != 0) {
				nums[index] = i-bias;
				countingArray[i]--;
				index++;
			} else {
				i++;
			}
			PrintArray.print(countingArray);
			PrintArray.print(nums);
			System.out.println("--------------------");
		}
		return nums;
	}
	
	final static int[] src = {5,4,5,0,3,6,2,0,2,4,3,3};
	
	public static void main(String[] args) {
		PrintArray.print(src);
		System.out.println("============================================");
		int[] dest = sort(src);
		PrintArray.print(dest);
	}
}
