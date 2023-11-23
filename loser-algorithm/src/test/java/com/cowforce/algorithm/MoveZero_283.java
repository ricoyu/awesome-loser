package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-11-30 11:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MoveZero_283 {
	
	public void moveZeros(int[] nums) {
		if (nums == null || nums.length == 0) {
			return;
		}
		
		//第一次遍历的时候，j指针记录非0的个数，只要是非0的统统都赋给nums[j]
		int j=0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != 0) {
				nums[j++] = nums[i];
			}
		}
		
		/*
		 * 非0元素统计完了，剩下的都是0了
		 * 所以第二次遍历把末尾的元素都赋为0即可
		 */
		for (int i = j; i < nums.length; i++) {
			nums[i] = 0;
		}
	}
	
	/**
	 * 一次遍历,快速排序的思想
	 * @param nums
	 */
	public void moveZero2(int[] nums) {
		if (nums == null || nums.length ==0) {
			return;
		}
		
		int j=0;
		for (int i = 0; i < nums.length; i++) {
			//当前元素!=0，就把其交换到左边，等于0的交换到右边
			if (nums[i] !=0) {
				int tmp = nums[i];
				nums[i] = nums[j];
				nums[j++] = tmp;
			}
		}
	}
	
	public void moveZero3(int[] nums) {
		if (nums == null || nums.length ==0) {
			return;
		}
		int j = 0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != 0) {
				nums[j] = nums[i];
				if (i!=j) {
					nums[i] = 0;
				}
				j++;
			}
		}
	}
}
