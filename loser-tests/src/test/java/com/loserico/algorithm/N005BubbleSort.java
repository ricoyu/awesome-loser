package com.loserico.algorithm;

import com.loserico.common.lang.utils.AlgorithmUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2023-09-08 11:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class N005BubbleSort {
	
	@Test
	public void test() {
		//int[] nums = AlgorithmUtils.randomArr(10, 9);
		//AlgorithmUtils.printArray(nums);
		int[] nums = {6, 0, 1, 8, 6};
		AlgorithmUtils.bubbleSort(nums);
		AlgorithmUtils.printArray(nums);
	}
	
	/**
	 * 冒泡排序是一种简单的排序算法, 它通过比较相邻的元素并交换它们的位置, 使得较大的元素逐渐"浮"到数组的末尾
	 */
	public void bubbleSort(int[] nums) {
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
	
	public void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i]= nums[j];
		nums[j]= temp;
	}
}
