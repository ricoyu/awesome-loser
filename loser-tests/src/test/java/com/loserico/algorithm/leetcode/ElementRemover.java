package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 移除元素
 * <p>
 * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素。元素的顺序可能发生改变。然后返回 nums 中与 val 不同的元素的数量。<br/>
 * 假设 nums 中不等于 val 的元素数量为 k，要通过此题，您需要执行以下操作：<br/>
 * 更改 nums 数组，使 nums 的前 k 个元素包含不等于 val 的元素。nums 的其余元素和 nums 的大小并不重要。<br/>
 * 返回 k。<br/>
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [3,2,2,3], val = 3 <br/>
 * 输出：2, nums = [2,2,_,_] <br/>
 * 解释：你的函数函数应该返回 k = 2, 并且 nums 中的前两个元素均为 2。 <br/>
 * 你在返回的 k 个元素之外留下了什么并不重要（因此它们并不计入评测）。
 * <p>
 * 示例 2：
 * <p>
 * 输入：nums = [0,1,2,2,3,0,4,2], val = 2 <br/>
 * 输出：5, nums = [0,1,4,0,3,_,_,_] <br/>
 * 解释：你的函数应该返回 k = 5，并且 nums 中的前五个元素为 0,0,1,3,4。 <br/>
 * 注意这五个元素可以任意顺序返回。 <br/>
 * 你在返回的 k 个元素之外留下了什么并不重要（因此它们并不计入评测）。
 * <p>
 * 本题的核心思想是使用双指针技术，其中一个指针（我们称之为 insertPosition）用于确定下一个不等于 val 的元素应该放置的位置，另一个指针（我们称之为 current）用于遍历数组。
 * <ol>
 *     <li/>创建一个指针 insertPosition，初始化为0，这个指针指向当前数组中下一个不应该包含值 val 的位置。
 *     <li/>遍历数组 nums，使用指针 current 从头到尾遍历。
 *     <li/>对于每个元素，检查其是否等于 val。
 *          如果不等于 val，将该元素复制到 insertPosition 指向的位置，然后 insertPosition 向前移动一位。
 *          如果等于 val，则 current 继续前移，不做其他操作。
 *     <li/>遍历完成后，insertPosition 的值就是数组中不等于 val 的元素的个数
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-11-02 15:56
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ElementRemover {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数组nums: ");
		String input = scanner.nextLine().trim();
		String[] parts = input.split(",");
		int[] nums = new int[parts.length];
		for(int i = 0; i < parts.length; i++) {
		  nums[i] = Integer.parseInt(parts[i].trim());
		}
		System.out.print("请输入值val: ");
		int val = scanner.nextInt();
		System.out.println(removeElement(nums, val));
		Arrays.print(nums);
	}

	public static int removeElement(int[] nums, int val) {
		// 新数组的长度，也是新数组最后一个元素的下一个位置
		int insertPosition = 0;

		// 遍历原数组
		for (int current = 0; current < nums.length; current++) {
			// 只处理不等于 val 的元素
			if (nums[current] != val) {
				// 将当前元素复制到 insertPosition 指定的位置
				nums[insertPosition] = nums[current];
				// 更新 insertPosition 为下一个可用位置
				insertPosition++;
			}
		}

		java.util.Arrays.fill(nums, insertPosition, nums.length, -1);
		// 返回新数组的长度，即不含 val 的元素个数
		return insertPosition;
	}
}
