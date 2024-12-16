package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 最长定差子序列
 * <p/>
 * 给你一个整数数组 arr 和一个整数 difference，请你找出并返回 arr 中最长等差子序列的长度，该子序列中相邻元素之间的差等于 difference 。
 * <p/>
 * 子序列 是指在不改变其余元素顺序的情况下，通过删除一些元素或不删除任何元素而从 arr 派生出来的序列。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：arr = [1,2,3,4], difference = 1 <br/>
 * 输出：4 <br/>
 * 解释：最长的等差子序列是 [1,2,3,4]。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：arr = [1,3,5,7], difference = 1 <br/>
 * 输出：1 <br/>
 * 解释：最长的等差子序列是任意单个元素。
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：arr = [1,5,7,8,5,3,4,2,1], difference = -2 <br/>
 * 输出：4 <br/>
 * 解释：最长的等差子序列是 [7,5,3,1]。
 * <p/>
 * 定义子问题： 假设我们使用一个 dp 数组来存储答案，dp[x] 表示以元素 x 为结尾的最长等差子序列的长度。我们的目标是通过遍历数组，找出 dp 数组中的最大值。 <br/>
 * 状态转移： 对于每个元素 arr[i]，我们希望找出一个前面的元素 arr[j]，使得它们之间的差为 difference，即：arr[i]−arr[j]=difference <br/>
 * 如果找到了这样的元素 arr[j]，那么可以将 arr[i] 加到以 arr[j] 为结尾的等差子序列上，即：dp[arr[i]]=dp[arr[j]]+1 <br/>
 * 否则，arr[i] 就是一个新的子序列，dp[arr[i]] = 1。
 * <p/>
 * 优化： 使用哈希映射（Map<Integer, Integer>）来存储 dp 数组，其中键是数值，值是对应数值的最大子序列长度。这样可以避免使用不必要的数组，并且查找和更新操作的时间复杂度是 O(1)。 <br/>
 * 时间复杂度： 遍历一次数组，并且对于每个元素进行常数时间的查找和更新，因此时间复杂度为 O(n)，其中 n 是数组的长度。
 * <p/>
 * Copyright: Copyright (c) 2024-11-29 8:49
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LongestArithmeticSubsequence {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数字对: ");
		String input = scanner.nextLine();
		int[] arr = Arrays.parseOneDimensionArray(input);
		System.out.print("请输入差值: ");
		int difference = scanner.nextInt();
		System.out.println(longestSubsequence(arr, difference));
	}

	public static int longestSubsequence(int[] arr, int difference) {
		// 使用一个HashMap来存储以某个数为结尾的最长等差子序列的长度
		Map<Integer, Integer> dp = new HashMap<>();

		//最大的子序列长度
		int maxLength = 0;

		for (int num : arr) {
			// 如果以num-difference为结尾的子序列存在，那么可以将num添加进去，形成新的子序列
			int prev = num - difference;

			// 计算当前数字作为等差子序列末尾时的长度
			int length = dp.getOrDefault(prev, 0) + 1;
			// 更新以num为结尾的最长等差子序列的长度
			dp.put(num, length);
			// 更新最大长度
			maxLength = Math.max(maxLength, length);
		}

		// 返回最长等差子序列的长度
		return maxLength;
	}
}
