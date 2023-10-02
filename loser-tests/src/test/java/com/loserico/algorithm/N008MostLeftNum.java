package com.loserico.algorithm;

import com.loserico.common.lang.utils.AlgorithmUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2023-09-09 21:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class N008MostLeftNum {
	
	@Test
	public void test() {
		//int[] nums = AlgorithmUtils.randomArr(9, 10);
		//AlgorithmUtils.selectionSort(nums);
		//AlgorithmUtils.printArray(nums);
		int[] nums = {0, 0, 2, 5, 7, 7, 8, 8, 9};
		int value = 5;
		int index = AlgorithmUtils.binarySearch(nums, 5);
		if (index == -1) {
			System.out.println("数组中不存在" + value);
			index = nums.length - 1; //找不到和指定数相等的就从右边挨个找起
		}
		while (index >= 0) {
			if (nums[index] >= value) {
				index--;
			} else {
				break;
			}
		}
		System.out.println("有序数组中找到>=" + value + "最左的位置" + (index + 1));
	}
	
	@Test
	public void test1() {
		for (int i = 0; i < 100; i++) {
			int[] nums = AlgorithmUtils.randomArr(10, 9);
			AlgorithmUtils.printArray(nums);
		}
	}
}
