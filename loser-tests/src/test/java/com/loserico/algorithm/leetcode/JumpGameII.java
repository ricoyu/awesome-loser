package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 跳跃游戏 II
 * <p/>
 * 给定一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。
 * <p/>
 * 每个元素 nums[i] 表示从索引 i 向前跳转的最大长度。换句话说，如果你在 nums[i] 处，你可以跳转到任意 nums[i + j] 处:
 * <p/>
 * 0 <= j <= nums[i] <br/>
 * i + j < n <br/>
 * 返回到达 nums[n - 1] 的最小跳跃次数。生成的测试用例可以到达 nums[n - 1]。
 * <p/>
 * 示例 1:
 * <p/>
 * 输入: nums = [2,3,1,1,4] <br/>
 * 输出: 2 <br/>
 * 解释: 跳到最后一个位置的最小跳跃数是 2。 <br/>
 * 从下标为 0 跳到下标为 1 的位置，跳 1 步，然后跳 3 步到达数组的最后一个位置。
 * <p/>
 * 示例 2:
 * <p/>
 * 输入: nums = [2,3,0,1,4] <br/>
 * 输出: 2 <br/>
 * <p/>
 * 该问题可以通过贪心算法有效解决。关键思路是每次尽可能跳到能够到达的最远位置。具体的解题步骤如下：
 * <ol>
 *     <li/>维护当前能到达的最远距离：在遍历数组的过程中，记录下当前能到达的最远距离。
 *     <li/>更新跳跃次数和能到达的最远距离：当遍历到当前能到达的最远距离时，说明需要进行一次新的跳跃，此时更新跳跃次数并重新计算新的最远距离。
 *     <li/>继续遍历直到数组末尾：重复上述步骤，直到到达数组的最后一个元素。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-11-09 19:32
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class JumpGameII {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for(int j = 0; j < 3; j++) {
			System.out.print("请输入数组: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] nums = new int[parts.length];
			for(int i = 0; i < parts.length; i++) {
				nums[i] = Integer.parseInt(parts[i].trim());
			}
			int jumps = jumps(nums);
			System.out.println(jumps);
		}
		scanner.close();
	}

	public static int jumps(int[] nums) {
		int jumps = 0; // 记录跳跃次数
		int currentEnd = 0; // 当前跳跃可以到达的最远位置
		int fastestEnd = 0; // 从当前位置和当前跳的能力能到达的最远位置

		// 遍历数组，但不包括最后一个元素，因为到达最后我们就不需要再跳了
		for (int i = 0; i < nums.length - 1; i++) {
			// 更新能到达的最远位置
			fastestEnd = Math.max(fastestEnd, i + nums[i]);

			// 如果到达当前跳跃的极限（最远距离）
			if (i == currentEnd) {
				jumps++;// 增加跳跃次数
				currentEnd = fastestEnd;  // 更新当前跳跃可以到达的最远位置

				if (currentEnd >= nums.length - 1) {
					return jumps;
				}
			}
		}
		return jumps; // 返回需要的最小跳跃次数
	}
}
