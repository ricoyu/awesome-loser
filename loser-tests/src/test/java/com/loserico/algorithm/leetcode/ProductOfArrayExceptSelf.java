package com.loserico.algorithm.leetcode;

/**
 * 除自身以外数组的乘积
 * <p/>
 * 给你一个整数数组 nums，返回 数组 answer ，其中 answer[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积 。 <p/>
 * 题目数据 保证 数组 nums之中任意元素的全部前缀元素和后缀的乘积都在  32 位 整数范围内。
 * <p/>
 * 请 不要使用除法，且在 O(n) 时间复杂度内完成此题。
 * <p/>
 * 示例 1:
 * <p/>
 * 输入: nums = [1,2,3,4]
 * 输出: [24,12,8,6]
 * <p/>
 * 示例 2:
 * <p/>
 * 输入: nums = [-1,1,0,-3,3]
 * 输出: [0,0,9,0,0]
 * <p/>
 * 为了实现这个问题，我们可以使用“前缀积”和“后缀积”的方法。这种方法可以避免使用除法，并且可以在O(n) 的时间复杂度内解决问题。
 *
 * <ol>具体思路如下：
 *     <li/>前缀积数组：构建一个数组 left，其中 left[i] 表示在 nums[i] 之前所有元素的乘积。
 *     <li/>后缀积数组：构建一个数组 right，其中 right[i] 表示在 nums[i] 之后所有元素的乘积。
 *     <li/>计算结果：对于每个 i，结果 answer[i] 就等于 left[i] * right[i]。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-11-12 9:17
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ProductOfArrayExceptSelf {

	public static int[] productExceptSelf(int[] nums) {
		int length = nums.length;

		// 用于存储结果的数组 answer
		int[] answer = new int[length];

		// 计算左边乘积
		// left 表示到当前元素为止左边所有元素的乘积
		// 初始化 answer[0] 为 1，因为第一个元素左边没有任何元素
		answer[0] = 1;
		for (int i = 1; i < length; i++) {
			// 当前元素左边的乘积等于前一个位置的乘积乘以前一个元素的值
			answer[i] = answer[i - 1] * nums[i - 1];
		}

		// 计算右边乘积，并直接更新到 answer 数组中
		// right 表示到当前元素为止右边所有元素的乘积
		int right = 1;
		for (int i = length - 1; i >= 0; i--) {
			// answer[i] 此时已经包含左边的乘积，只需乘上右边的乘积即可
			answer[i] = answer[i] * right;
			// 更新 right 为当前元素右边所有元素的乘积
			right *= nums[i];
		}

		return answer;
	}
}
