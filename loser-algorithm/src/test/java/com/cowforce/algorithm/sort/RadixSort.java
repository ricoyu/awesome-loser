package com.cowforce.algorithm.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * 基数排序
 * <p>
 * Copyright: (C), 2022-12-15 15:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RadixSort {
	
	public static int[] sort(int[] nums) {
		if (nums == null || nums.length < 2) {
			return nums;
		}
		//找出最大数
		int max = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] > max) {
				max = nums[i];
			}
		}
		
		//先算出最大数的位数,它决定了我们要进行几轮排序
		int maxDigit = 0;
		while (max != 0) {
			max /= 10;
			maxDigit++;
		}
		
		int mod = 10, div = 1;
		
		//构建桶
		List<List<Integer>> buckets = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			buckets.add(new ArrayList<Integer>());
		}
		//按照从右往左的顺序, 依次将每一位都当做一次关键字, 然后按照该关键字对数组排序, 每一轮排序都基于上轮排序后的结果
		for (int i = 0; i < maxDigit; i++, mod *= 10, div *= 10) {
			System.out.println("----第" + i + "轮排序-----");
			//遍历原始数组，投入桶中
			for (int j = 0; j < nums.length; j++) {
				int num = (nums[j] % mod) / div;
				buckets.get(num).add(nums[j]);
			}
			//看看桶中数据的分布
			for (int j = 0; j < buckets.size(); j++) {
				System.out.print("第" + j + "个桶包含数据：");
				PrintArray.printObject(buckets.get(j));
			}
			
			//桶中的数据写回原始数组，清除桶，准备下一轮的排序
			int index = 0;
			for (int j = 0; j < buckets.size(); j++) {
				for (int k = 0; k < buckets.get(j).size(); k++) {
					nums[index++] =  buckets.get(j).get(k);
				}
				buckets.get(j).clear();
			}
		}
		
		return nums;
	}
	
	public static void main(String[] args) {
		PrintArray.print(PrintArray.SRC);
		System.out.println("============================================");
		int[] dest = RadixSort.sort(PrintArray.SRC);
		PrintArray.print(dest);
	}
}
