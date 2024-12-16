package com.loserico.algorithm.leetcode;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 最长数对链
 * <p/>
 * 给你一个由 n 个数对组成的数对数组 pairs ，其中 pairs[i] = [lefti, righti] 且 lefti < righti 。
 * 现在，我们定义一种 跟随 关系，当且仅当 b < c 时，数对 p2 = [c, d] 才可以跟在 p1 = [a, b] 后面。我们用这种形式来构造 数对链 。
 * <p>
 * 找出并返回能够形成的 最长数对链的长度 。
 * <p>
 * 你不需要用到所有的数对，你可以以任何顺序选择其中的一些数对来构造。
 * <p>
 * 示例 1：
 * <p>
 * 输入：pairs = [[1,2], [2,3], [3,4]] <br/>
 * 输出：2 <br/>
 * 解释：最长的数对链是 [1,2] -> [3,4] 。 <br/>
 * <p>
 *
 * 示例 2：
 * <p/>
 * 输入：pairs = [[1,2],[7,8],[4,5]] <br/>
 * 输出：3 <br/>
 * 解释：最长的数对链是 [1,2] -> [4,5] -> [7,8] 。 <br/>
 *  <p/>
 * 在这个问题中，我们需要找到一个最长的序列，使得每个数对的第二个元素小于下一个数对的第一个元素。这是一个典型的动态规划问题，我们可以通过排序和定义一个合适的状态转移来解决。
 *
 * <ul>解题思路：
 *     <li/>排序：首先按照每个数对的第二个元素（结束元素）进行排序。这样做的原因是我们希望尽可能容易地找到可以连接的数对。
 *     <li/>动态规划：我们定义一个数组 dp，其中 dp[i] 表示以 pairs[i] 结束时的最长数对链的长度。初始情况下，每个数对自己就可以形成一个链，所以 dp[i] = 1。
 *     <li/>状态转移：对于每个数对 i，我们检查之前的所有数对 j（j < i），如果 pairs[j][1] < pairs[i][0]，则 pairs[i] 可以跟在 pairs[j] 后面形成更长的链。
 *          此时，更新 dp[i] = max(dp[i], dp[j] + 1)。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-11-28 9:07
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MaxLengthPairChain {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字对: ");
		String input = scanner.nextLine();
		int[][] pairs = com.loserico.common.lang.utils.Arrays.parseTwoDimensionArray(input);

		System.out.println("最长数对链: " + findLongestChain(pairs));
	}

	public static int findLongestChain(int[][] pairs) {
		// 数对按照第二个元素升序排序
		Arrays.sort(pairs, (a, b) -> a[1] - b[1]);

		// 动态规划数组，初始每个数对至少可以自成一链
		int n = pairs.length;
		int[] dp = new int[n];
		Arrays.fill(dp, 1); // 每个数对至少可以自成一个链

		// 填充dp数组，找出最长数对链
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				if (pairs[j][1] < pairs[i][0]) {
					dp[i] = Math.max(dp[i], dp[j] + 1);
				}
			}
		}

		// 找出dp数组中的最大值，即为所求
		int max = 0;
		for (int length : dp) {
			max = Math.max(length, max);
		}

		return max;
	}
}
