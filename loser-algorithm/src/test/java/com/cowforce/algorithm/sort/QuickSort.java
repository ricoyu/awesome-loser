package com.cowforce.algorithm.sort;

/**
 * 1. 选择第一个数作为基准数, 和最后一个数交换位置
 * 2. 分隔指示器初始化为第一个元素下标-1, 第一次的话是-1;
 * 3. 一个指针从0开始指向当前元素, 如果当前元素<=基准数时, 分隔指示器右移一位
 * 4. 在3的基础上, 当前元素下标大于分隔指示器下标时, 当前元素和分隔指示器元素交换
 * <p>
 * Copyright: (C), 2022-12-10 15:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class QuickSort {
	
	public static void quickSort(int[] nums) {
		sort(nums, 0, nums.length - 1);
	}
	
	public static void sort(int[] nums, int start, int end) {
		if (nums == null || nums.length <= 1 || start < 0 || start > end || end >= nums.length) {
			return;
		}
		int zoneIndex = partition(nums, start, end);
		
		if (zoneIndex > start && zoneIndex < end) {
			sort(nums, start, zoneIndex);
			sort(nums, zoneIndex+1, end);
		} 
	}
	
	/**
	 * @param nums  要排序的数组
	 * @param start 开始元素的下标
	 * @param end   结束元素的下标
	 * @return
	 */
	public static int partition(int[] nums, int start, int end) {
		//只有一个元素时, 无需再分区
		if (start == end) {
			return start;
		}
		//[35, 63, 48, 9, 86, 24, 53, 72]
		int pivot = start + (int) Math.random() * (end - start + 1); // 随机选择一个作为基准数
		//将基准数和分区尾元素交换位置
		swap(nums, pivot, end);
		//zoneIndex是分区指示器, 初始值为分区头元素下标减
		int zoneIndex = start - 1;
		System.out.println("开始下标：" + start + ",结束下标:" + end + ",基准数下标：" + pivot + ",元素值:" + nums[pivot] + "，分区指示器下标：" + zoneIndex);
		for (int i = start; i <= end; i++) {
			//当前元素小于等于基准数
			if (nums[i] <= nums[end]) {
				//首先分区指示器右移一位
				zoneIndex++;
				
				if (i > zoneIndex) {
					swap(nums, zoneIndex, i);
				}
			}
			System.out.println("分区指示器：" + zoneIndex + ",遍历指示器:" + i);
			PrintArray.printIndex(nums, start, end);
			System.out.println("---------------");
		}
		return zoneIndex;
	}
	
	/**
	 * 交换 pre, next下标的元素
	 *
	 * @param nums
	 * @param pre
	 * @param next
	 */
	public static void swap(int[] nums, int pre, int next) {
		int temp = nums[pre];
		nums[pre] = nums[next];
		nums[next] = temp;
	}
	
	public static void main(String[] args) {
		PrintArray.print(PrintArray.SRC);
		System.out.println("============================================");
		quickSort(PrintArray.SRC);
		PrintArray.print(PrintArray.SRC);
	}
}
