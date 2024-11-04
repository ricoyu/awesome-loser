package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 多数元素
 * <p/>
 * 给定一个大小为 n 的数组 nums ，返回其中的多数元素。多数元素是指在数组中出现次数 大于 ⌊ n/2 ⌋ 的元素。
 * <p/>
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：nums = [3,2,3] <br/>
 * 输出：3
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：nums = [2,2,1,1,1,2,2] <br/>
 * 输出：2
 * <p/>
 *
 * 这道题可以使用几种不同的方法来解决，例如排序、哈希表统计或者摩尔投票法。 <p/>
 * 在这里，我将选择摩尔投票法（Boyer-Moore Voting Algorithm），因为它的时间复杂度是O(n)，空间复杂度是O(1)，非常高效。
 * <p/>
 * 摩尔投票法的基本思想：
 * <p/>
 * 我们维护一个候选元素candidate和一个计数器count。 <br/>
 * 遍历数组nums，对于每个元素： <br/>
 *  如果count为0，我们将当前的元素设置为候选元素，然后将count设置为1。 <br/>
 *  如果count不为0，我们将当前元素与candidate比较： <br/>
 *      如果它们相等，则增加count。 <br/>
 *      如果不相等，则减少count。
 * <p/>
 * 经过一轮遍历后，candidate就是出现次数超过半数的元素。
 * <p/>
 * Copyright: Copyright (c) 2024-11-04 9:55
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MajorityElementFinder {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for(int j = 0; j < 2; j++) {
			System.out.print("请输入数组nums: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] nums = new int[parts.length];
			for(int i = 0; i < parts.length; i++) {
			  nums[i] = Integer.parseInt(parts[i].trim());
			}

			System.out.println(majorityElement(nums));
		}
	}

	public static int majorityElement(int[] nums) {
		int candidate = 0; // 初始化候选元素
		int count = 0; // 初始化计数器

		for (int num : nums) {
			if (count == 0) {
				candidate = num;
				count = 1;
			} else if (num == candidate) {
				// 如果当前元素等于候选人，计数器增加
				count++;
			} else {
				// 如果当前元素不等于候选人，计数器减少
				count--;
			}
		}

		// 返回候选人，根据题目描述，可以假设总是存在多数元素
		return candidate;
	}
}
