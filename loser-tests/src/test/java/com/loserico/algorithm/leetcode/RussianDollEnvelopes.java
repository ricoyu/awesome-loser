package com.loserico.algorithm.leetcode;

import java.util.Arrays;
import java.util.Scanner;

import static com.loserico.common.lang.utils.Arrays.parseTwoDimensionArray;

/**
 * 俄罗斯套娃信封问题
 * <p/>
 * 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
 * <p/>
 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
 * <p/>
 * 请计算 最多能有多少个 信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）
 * <p/>
 * 注意：不允许旋转信封。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：envelopes = [[5,4],[6,4],[6,7],[2,3]] <br/>
 * 输出：3 <br/>
 * 解释：最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。 <br/>
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：envelopes = [[1,1],[1,1],[1,1]] <br/>
 * 输出：1 <br/>
 * <p/>
 * 这是一种典型的动态规划问题，与最长递增子序列（LIS）问题有相似之处。主要挑战在于正确地定义状态转移方程和处理二维数据的排序规则
 * <ul>解题思路:
 *     <li/>排序：首先对信封数组进行排序，先按宽度升序排序，宽度相同时按高度降序排序。这样做是为了确保在宽度相同时，不会错误地将高度小的信封套入高度大的信封中。
 *     <li/>动态规划：定义一个数组 dp，其中 dp[i] 表示以 envelopes[i] 为最外层信封时的最大套娃信封数量。
 *                  对于每个信封，检查它之前的所有信封，如果之前的某个信封可以套进当前信封中（即宽和高都比当前信封小），则尝试更新 dp[i]。
 *     <li/>最大值：在所有 dp[i] 中找到最大值，即为答案。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-12-03 8:50
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RussianDollEnvelopes {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字对: ");
		String input = scanner.nextLine();
		int[][] envelopes = parseTwoDimensionArray(input);
		System.out.println("最长套娃信封数量: " + maxEnvelopes(envelopes));
		scanner.close();
	}

	public static int maxEnvelopes(int[][] envelopes) {

		// 对信封进行排序, 宽度升序, 宽度相同则高度降序
		Arrays.sort(envelopes, (prev, next) -> {
			if (prev[0] == next[0]) {
				return prev[1] - next[1];
			} else {
				return prev[0] - next[0];
			}
		});

		// 初始化动态规划数组
		int[] dp = new int[envelopes.length];
		// 每个信封至少可以单独成为一个套娃
		Arrays.fill(dp, 1);

		int maxEnvelopes = 1; // 至少有一个信封时的最大套娃数
		// 动态规划计算最大套娃数
		for (int i = 1; i < envelopes.length; i++) {
			for (int j = 0; j < i; j++) {
				// 如果前一个信封可以套入当前信封中
				if (envelopes[j][1] < envelopes[i][1]) {
					dp[i] = Math.max(dp[i], dp[j] + 1);
				}
			}
			// 更新最大套娃数
			maxEnvelopes = Math.max(maxEnvelopes, dp[i]);
		}

		return maxEnvelopes;
	}
}
