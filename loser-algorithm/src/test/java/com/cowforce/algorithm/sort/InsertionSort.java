package com.cowforce.algorithm.sort;

/**
 * <p>
 * Copyright: (C), 2022-12-09 11:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InsertionSort {
	
	public static void sort(int[] nums) {
		
		for (int i = 0; i < nums.length-1; i++) {
			int sortedIndex= i; //记录当前最后一个已经排好序的元素的index, 初始是0, 第一个元素认为是已经有序的
			int currentValue = nums[i + 1]; //从第二个元素开始排序
			System.out.println("待排序元素索引:"+(i + 1)+"，值为：" +currentValue+ ",已被排序数据的索引:"+sortedIndex);
			while (sortedIndex >=0 && currentValue<nums[sortedIndex]) {
				nums[sortedIndex+1] = nums[sortedIndex]; //前面排好序的往后挪一位
				sortedIndex--;
			}
			//跳出while循环表示已经找到插入位置了
			nums[sortedIndex+1] = currentValue;
			System.out.println("本轮被插入排序后的数组");
			PrintArray.print(nums);
			System.out.println("--------------------");
		}
	}
	
	public static void main(String[] args) {
		PrintArray.print(PrintArray.SRC);
		System.out.println("============================================");
		sort(PrintArray.SRC);
		PrintArray.print(PrintArray.SRC);
	}
}
