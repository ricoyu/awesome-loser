package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 跳跃游戏
 * <p/>
 * 给你一个非负整数数组 nums ，你最初位于数组的 第一个下标 。数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * <p/>
 * 判断你是否能够到达最后一个下标，如果可以，返回 true ；否则，返回 false 。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：nums = [2,3,1,1,4] <br/>
 * 输出：true <br/>
 * 解释：可以先跳 1 步，从下标 0 到达下标 1, 然后再从下标 1 跳 3 步到达最后一个下标。
 * <p/>
 *
 * 为了解决跳跃游戏的问题，我们可以使用贪心算法。思路是记录一个能达到的最远位置， <p/>
 * 如果当前下标在最远位置的范围内，则继续前进并更新最远位置；否则，如果已经超过最远位置，说明无法到达最后一个下标。
 *
 * <ul>解题思路
 *     <li/>初始化一个变量 farthest，表示能够到达的最远位置。初始值为0。
 *     <li/>遍历数组中的每一个位置 i：
 *     <li/>如果当前位置 i 大于 farthest，说明已经超出了当前能到达的最远位置，返回 false。
 *     <li/>否则，更新 farthest 为 Math.max(farthest, i + nums[i])，表示在当前位置的跳跃能力下可以到达的最远位置。
 *     <li/>在遍历结束后，如果 farthest 大于等于最后一个下标，说明可以到达最后一个下标，返回 true；否则返回 false。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-11-08 9:09
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class JumpGame {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数组: ");
		String input = scanner.nextLine().trim();
		String[] parts = input.split(",");
		int[] nums = new int[parts.length];
		for(int i = 0; i < parts.length; i++) {
		  nums[i] = Integer.parseInt(parts[i].trim());
		}
		boolean result = canJump(nums);
		System.out.println(result);
		scanner.close();
	}

	public static boolean canJump(int[] nums) {
		// farthest 记录当前能到达的最远位置
		int farthest = 0;

		// 遍历数组中的每个位置 i
		for (int i = 0; i < nums.length; i++) {
			// 如果当前位置 i 大于 farthest，说明已经超出了当前能到达的最远位置，返回 false
			if (i > farthest) {
				return false;
			}
			// 更新 farthest 为 Math.max(farthest, i + nums[i])，表示在当前位置的跳跃能力下可以到达的最远位置
			farthest = Math.max(farthest, i + nums[i]);
		}
		// 在遍历结束后，如果 farthest 大于等于最后一个下标
		return farthest >= nums.length - 1;
	}
}
