package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 找出到每个位置为止最长的有效障碍赛跑路线
 * <p/>
 * 你打算构建一些障碍赛跑路线。给你一个下标从 0 开始的整数数组 obstacles ，数组长度为 n ，其中obstacles[i]表示第 i 个障碍的高度。
 * <p/>
 * 对于每个介于 0 和 n - 1 之间（包含 0 和 n - 1）的下标  i ，在满足下述条件的前提下，请你找出 obstacles 能构成的最长障碍路线的长度：
 * <p/>
 * 你可以选择下标介于 0 到 i 之间（包含 0 和 i）的任意个障碍。
 * <p/>
 * 在这条路线中，必须包含第 i 个障碍。
 * <p/>
 * 你必须按障碍在 obstacles 中的 出现顺序布置这些障碍。
 * <p/>
 * 除第一个障碍外，路线中每个障碍的高度都必须和前一个障碍相同 或者更高 。
 * <p/>
 * 返回长度为 n 的答案数组 ans ，其中 ans[i] 是上面所述的下标 i 对应的最长障碍赛跑路线的长度。
 *
 * 示例 1：
 * <p/>
 * 输入：obstacles = [1,2,3,2] <br/>
 * 输出：[1,2,3,3] <br/>
 * 解释：每个位置的最长有效障碍路线是： <br/>
 * - i = 0: [1], [1] 长度为 1 <br/>
 * - i = 1: [1,2], [1,2] 长度为 2 <br/>
 * - i = 2: [1,2,3], [1,2,3] 长度为 3 <br/>
 * - i = 3: [1,2,3,2], [1,2,2] 长度为 3
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：obstacles = [2,2,1] <br/>
 * 输出：[1,2,1] <br/>
 * 解释：每个位置的最长有效障碍路线是： <br/>
 * - i = 0: [2], [2] 长度为 1 <br/>
 * - i = 1: [2,2], [2,2] 长度为 2 <br/>
 * - i = 2: [2,2,1], [1] 长度为 1
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：obstacles = [3,1,5,6,4,2] <br/>
 * 输出：[1,1,2,3,2,2] <br/>
 * 解释：每个位置的最长有效障碍路线是： <br/>
 * - i = 0: [3], [3] 长度为 1 <br/>
 * - i = 1: [3,1], [1] 长度为 1 <br/>
 * - i = 2: [3,1,5], [3,5] 长度为 2, [1,5] 也是有效的障碍赛跑路线 <br/>
 * - i = 3: [3,1,5,6], [3,5,6] 长度为 3, [1,5,6] 也是有效的障碍赛跑路线 <br/>
 * - i = 4: [3,1,5,6,4], [3,4] 长度为 2, [1,4] 也是有效的障碍赛跑路线 <br/>
 * - i = 5: [3,1,5,6,4,2], [1,2] 长度为 2
 * <p/>
 * 这个问题可以用动态规划来解决。我们的目标是找到一个以 obstacles[i] 结尾的最长且合法的障碍路线长度。
 * <p/>
 * 我们需要创建一个数组 dp，其中 dp[i] 表示以 obstacles[i] 结尾的最长障碍路线长度。
 * 对于每一个 i，我们需要检查之前的所有障碍 j（0 到 i-1），并且如果 obstacles[j] <= obstacles[i]，则 obstacles[i] 可以接在 obstacles[j] 后面，
 * 形成一个有效的序列。我们更新 dp[i] 为所有这样的 j 中 dp[j] + 1 的最大值。
 *
 * <ul>解法：
 *     <li/>初始化：我们创建一个长度为 n 的数组 dp，每个位置初始化为 1，因为每个障碍至少可以自成一路线。
 *     <li/>动态规划计算：对于每个 i 从 1 到 n-1，我们检查所有在它之前的障碍 j（0 到 i-1），如果 obstacles[j] <= obstacles[i]，则尝试更新 dp[i] 为 dp[j] + 1
 *     中的最大值。
 *     <li/>返回结果：最终，dp 数组就是我们的答案。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-12-04 8:49
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestObstacleCourse {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字对: ");
		String input = scanner.nextLine();
		int[] obstacles = Arrays.parseOneDimensionArray(input);
		Arrays.print(longestObstacleCourseAtEachPosition(obstacles));
	}

	public static int[] longestObstacleCourseAtEachPosition(int[] obstacles) {
		int n = obstacles.length;

		// dp[i] 表示以 obstacles[i] 结尾的最长有效障碍路线长度
		int[] dp = new int[n];

		// 初始化每个障碍的最小长度都为1
		for (int i = 0; i < n; i++) {
			dp[i] = 1;
		}

		// 对每个障碍点进行处理，计算以该障碍为终点的最长路径
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < i; j++) {
				// 只有当前一个障碍不高于当前障碍时，才能构成有效路径
				if (obstacles[j] <= obstacles[i]) {
					// 更新 dp[i] 为到达该障碍的最长路径长度
					dp[i] = Math.max(dp[i], dp[j] + 1);
				}
			}
		}

		return dp;
	}
}
