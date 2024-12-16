package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;
import com.loserico.common.lang.utils.StringUtils;

import java.util.Scanner;

/**
 * 删除有序数组中的重复项
 * <p/>
 * 给你一个 非严格递增排列 的数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
 * 元素的 相对顺序 应该保持 一致 。然后返回 nums 中唯一元素的个数。
 * <p/>
 * 考虑 nums 的唯一元素的数量为 k ，你需要做以下事情确保你的题解可以被通过： <p/>
 * 更改数组 nums ，使 nums 的前 k 个元素包含唯一元素，并按照它们最初在 nums 中出现的顺序排列。nums 的其余元素与 nums 的大小不重要。<br/>
 * 返回 k 。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：nums = [1,1,2] <br/>
 * 输出：2, nums = [1,2,_] <br/>
 * 解释：函数应该返回新的长度 2 ，并且原数组 nums 的前两个元素被修改为 1, 2 。不需要考虑数组中超出新长度后面的元素。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：nums = [0,0,1,1,1,2,2,3,3,4] <br/>
 * 输出：5, nums = [0,1,2,3,4] <br/>
 * 解释：函数应该返回新的长度 5 ， 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4 。不需要考虑数组中超出新长度后面的元素。
 * <p/>
 * 为了删除有序数组中的重复项并返回唯一元素的数量，我们可以使用双指针技术。通过一个慢指针和一个快指针遍历数组，快指针用于查找新元素，慢指针用于记录唯一元素的最终位置。
 *
 * <ol>解题思路
 *     <li/>初始化两个指针：i 作为慢指针，始终指向当前不重复数组的最后一个元素；j 作为快指针，用于探索数组中新的元素。
 *     <li/>探索数组：从第二个元素开始遍历数组（即 j 从 1 开始），如果 nums[j] 与 nums[i] 不同，说明 nums[j] 是一个新的元素。
 *     <li/>更新数组：将 nums[j] 放到 nums[i+1]，然后 i 向前移动一位。
 *     <li/>遍历完成：数组的前 i+1 个元素就是所有的不重复元素，i+1 就是新的数组长度。
 * </ol>
 * 当你执行下面的代码后，输出的 5 表示数组中有 5 个不重复的元素，这些元素被重新排列在数组的前五位：[0, 1, 2, 3, 4]。
 * 数组的剩余部分 [2, 2, 3, 3, 4] 是在原地算法的限制下的无关部分，不需要关心。这是符合题目要求的，即不需要考虑数组中超出新长度后面的元素。
 * <p/>
 * Copyright: Copyright (c) 2024-11-03 15:52
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RemoveDuplicatesFromSortedArray {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数组nums: ");
		String input = scanner.nextLine().trim();
		input = StringUtils.trimTrailingCharacter(input, "]");
		input = StringUtils.trimLeadingCharacter(input, "[");
		String[] parts = input.split(",");
		int[] nums = new int[parts.length];
		for(int i = 0; i < parts.length; i++) {
		  nums[i] = Integer.parseInt(parts[i].trim());
		}
		System.out.println(removeDuplicates(nums));
		Arrays.print(nums);
	}

	public static int removeDuplicates(int[] nums) {
		// 如果数组为空或只有一个元素，直接返回其长度
		if (nums == null || nums.length == 0) {
			return 0;
		}

		// 初始化慢指针i
		int i = 0;
		for (int j = 1; j < nums.length; j++) {
			// 当发现新的不重复元素时
			if (nums[i] != nums[j]) {
				// 移动慢指针，并更新其值为新的不重复元素
				nums[++i] = nums[j];
			}
		}

		// 返回新的长度，慢指针位置+1即为不重复元素的个数
		return i + 1;
	}
}
