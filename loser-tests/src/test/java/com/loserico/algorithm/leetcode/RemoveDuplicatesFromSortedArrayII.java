package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 删除有序数组中的重复项 II
 * <p/>
 * 给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使得出现次数超过两次的元素只出现两次 ，返回删除后数组的新长度。
 * <p/>
 * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：nums = [1,1,1,2,2,3] <br/>
 * 输出：5, nums = [1,1,2,2,3] <br/>
 * 解释：函数应返回新长度 length = 5, 并且原数组的前五个元素被修改为 1, 1, 2, 2, 3。 不需要考虑数组中超出新长度后面的元素。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：nums = [0,0,1,1,1,1,2,3,3] <br/>
 * 输出：7, nums = [0,0,1,1,2,3,3] <br/>
 * 解释：函数应返回新长度 length = 7, 并且原数组的前七个元素被修改为 0, 0, 1, 1, 2, 3, 3。不需要考虑数组中超出新长度后面的元素。
 * <p/>
 * 请用Java实现并给出详细的解题思路, Java代码中要加入详细的注释以解释清楚代码逻辑, 要给类取一个合理的类名
 * <p/>
 * 这个问题可以使用一个双指针技巧来解决。我们使用两个指针：一个用于遍历数组（i），另一个用于标记可以插入元素的位置（j）。 <p/>
 * 核心思想是当我们遍历数组时，如果一个数字的出现次数不超过两次，我们就将其复制到j指向的位置，然后增加j。如果一个数字出现超过两次，我们就跳过这个数字的后续出现。
 *
 * <ol>下面是具体步骤：
 *     <li/>如果数组长度小于等于2，直接返回数组长度。
 *     <li/>初始化两个指针：j（插入位置指针）初始化为1，因为第一个元素总是保留的；i（遍历数组的指针）从1开始。
 *     <li/>遍历数组，如果nums[i]和nums[j-1]相同，且nums[j]和nums[j-1]也相同，则跳过nums[i]因为这意味着这个数字已经出现了至少三次。
 *     <li/>否则，复制nums[i]到nums[j]，然后j++。
 *     <li/>遍历完成后，j就是新数组的长度。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-11-04 8:52
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RemoveDuplicatesFromSortedArrayII {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int j = 0; j < 2; j++) {
			System.out.print("请输入数组nums: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] nums = new int[parts.length];
			for (int i = 0; i < parts.length; i++) {
				nums[i] = Integer.parseInt(parts[i].trim());
			}

			System.out.println(removeDuplicates(nums));
			Arrays.print(nums);
		}
	}

	public static int removeDuplicates(int[] nums) {
		// 若数组长度小于等于2，则无需修改，直接返回数组长度
		if (nums.length <= 2) {
			return nums.length;
		}

		// 慢指针，表示新数组的有效长度
		int slow = 2;

		// 快指针从索引2开始，遍历整个数组
		for (int fast = 2; fast < nums.length; fast++) {
			// 判断当前元素是否超过出现两次
			// 如果 nums[fast] != nums[slow - 2]，表示该元素最多只出现了两次，可以加入结果数组中
			if (nums[fast] != nums[slow - 2]) {
				// 将当前元素移动到j+1的位置，然后j++
				nums[slow++] = nums[fast];
			}
		}
		return slow;
	}
}
