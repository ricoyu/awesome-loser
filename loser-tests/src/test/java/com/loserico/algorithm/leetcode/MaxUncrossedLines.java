package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 不相交的线
 * <p/>
 * 在两条独立的水平线上按给定的顺序写下 nums1 和 nums2 中的整数。
 * <p/>
 * 现在，可以绘制一些连接两个数字 nums1[i] 和 nums2[j] 的直线，这些直线需要同时满足：
 * <p/>
 * nums1[i] == nums2[j]
 * 且绘制的直线不与任何其他连线（非水平线）相交。
 * <p/>
 * 请注意，连线即使在端点也不能相交：每个数字只能属于一条连线。
 * <p/>
 * 以这种方法绘制线条，并返回可以绘制的最大连线数。
 * <p/>
 * 示例 1： <p/>
 * 输入：nums1 = [1,4,2], nums2 = [1,2,4] <br/>
 * 输出：2 <br/>
 * 解释：可以画出两条不交叉的线，如上图所示。  <br/>
 * 但无法画出第三条不相交的直线，因为从 nums1[1]=4 到 nums2[2]=4 的直线将与从 nums1[2]=2 到 nums2[1]=2 的直线相交。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：nums1 = [2,5,1,2,5], nums2 = [10,5,2,1,5,2] <br/>
 * 输出：3
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：nums1 = [1,3,7,1,7,5], nums2 = [1,9,2,5,1] <br/>
 * 输出：2
 *
 * 这个问题可以通过一个类似于最长公共子序列（LCS）的动态规划（DP）方法来解决。 <p/>
 * 在两条水平线上分别放置 nums1 和 nums2，问题转化为在两个序列中找出最长的相同子序列，且该子序列的元素在两个数组中的索引应保持一致。
 *
 * <ul>动态规划的解决方法
 *     <li/>初始化 DP 数组：创建一个二维数组 dp，其中 dp[i][j] 表示考虑到 nums1 的前 i 个元素和 nums2 的前 j 个元素时，可以形成的最大不相交连线数。
 *     <li/>如果 nums1[i-1] == nums2[j-1]，说明我们可以在这两个元素之间画一条线，所以 dp[i][j] = dp[i-1][j-1] + 1。
 *     <li/>如果不相等，那么 dp[i][j] 将从左边（dp[i][j-1]）或上面（dp[i-1][j]）继承较大的值，即 dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1])。
 * </ul>
 *
 * 填充 DP 表：遍历 nums1 和 nums2，使用上述转移方程填表。 <p/>
 * 结果：dp[nums1.length][nums2.length] 就是问题的答案。
 *
 * <p/>
 * Copyright: Copyright (c) 2024-12-09 9:59
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MaxUncrossedLines {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入第一个数组: ");
		String input1 = scanner.nextLine();
		int[] nums1 = Arrays.parseOneDimensionArray(input1);
		System.out.print("请输入第二个数组: ");
		String input2 = scanner.nextLine();
		int[] nums2 = Arrays.parseOneDimensionArray(input2);
		System.out.println(maxUncrossedLines(nums1, nums2));
	}

	public static int maxUncrossedLines(int[] nums1, int[] nums2) {
		// 获取输入数组的长度
		int m = nums1.length;
		int n = nums2.length;

		// 创建DP表
		int[][] dp = new int[m + 1][n + 1];

		// 填充DP表
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (nums1[i - 1] == nums2[j - 1]) {
					// 如果当前元素匹配，继承对角线值加一
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					// 如果当前元素不匹配，从左侧或上侧继承较大值
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}

		// 返回表的最后一个元素，即为最大连线数
		return dp[m][n];
	}
}
