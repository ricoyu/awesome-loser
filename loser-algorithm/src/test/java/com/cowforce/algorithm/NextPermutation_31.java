package com.cowforce.algorithm;

/**
 * (LeetCode-31) 下一个排列
 * 实现获取 下一个排列 的函数,
 * 算法需要将给定数字序列重新排列成字典序中下一个更大的排列
 * (即, 组合出下一个更大的整数)。
 * 如果不存在下一个更大的排列, 则将数字重新排列成最小的排列(即升序排列)。
 * 必须 原地 修改, 只允许使用额外常数空间。
 * <p>
 * Copyright: (C), 2023-01-31 17:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NextPermutation_31 {
	
	public void nextPermutation(int[] nums) {
		int i = nums.length - 2; //i 从数组的倒数第二个元素开始
		//找到数组中的“小数”，退出循环就说明nums[i] < nums[i + 1]
		while (i >= 0 && nums[i] >= nums[i + 1]) {
			i--;
		}
		if (i >= 0) {
			int j = nums.length - 1;
			//找到数组中的尽可能小的“大数，退出循环就说明nums[i] < nums[j]”
			while (j >= 0 && nums[i] >= nums[j]) {
				j--;
			}
			//交换“小数”和“大数”的位置
			swap(nums, i, j);
		}
		//将“大数”后面的所有数重置为升序
		reverse(nums, i + 1);
	}
	
	public void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	public void reverse(int[] nums, int start) {
		int left = start, right = nums.length - 1;
		while (left < right) {
			swap(nums, left, right);
			left++;
			right--;
		}
	}
	
	public static void main(String[] args) {
		int[] nums1 = {5, 3, 1, 6, 2, 4};
		int[] nums2 = {1, 2, 3, 4, 5, 6};
		new NextPermutation_31().nextPermutation(nums1);
	}
}
