package com.loserico.algorithm.leetcode;

import java.util.Scanner;

/**
 * 分发糖果
 * <p/>
 * n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
 * 你需要按照以下要求，给这些孩子分发糖果：
 * 每个孩子至少分配到 1 个糖果。
 * 相邻两个孩子评分更高的孩子会获得更多的糖果。
 * 请你给每个孩子分发糖果，计算并返回需要准备的 最少糖果数目 。
 * <p>
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：ratings = [1,0,2] <br/>
 * 输出：5 <br/>
 * 解释：你可以分别给第一个、第二个、第三个孩子分发 2、1、2 颗糖果。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：ratings = [1,2,2] <br/>
 * 输出：4 <br/>
 * 解释：你可以分别给第一个、第二个、第三个孩子分发 1、2、1 颗糖果。 <br/>
 * 第三个孩子只得到 1 颗糖果，这满足题面中的两个条件。
 * <p/>
 * 由于需要给每个孩子分配糖果，并且相邻的孩子若评分更高则需获得更多糖果，我们可以将这个问题分解为两个遍历过程：
 * <ol>
 *     <li/>第一次从左到右遍历：如果右边的孩子评分高于左边的孩子，则右边孩子的糖果数比左边孩子多 1。
 *     <li/>第二次从右到左遍历：如果左边的孩子评分高于右边的孩子，则左边的糖果数至少要比右边的多 1。
 * </ol>
 * 经过两次遍历后，数组中每个位置记录了该孩子应分得的糖果数，最终所有元素的和即为最少的糖果总数。
 * <p/>
 * Copyright: Copyright (c) 2024-11-14 8:43
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CandyDistributor {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for(int j = 0; j < 2; j++) {
			System.out.print("请输入评分: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int[] ratings = new int[parts.length];
			for(int i = 0; i < ratings.length; i++) {
				ratings[i] = Integer.parseInt(parts[i].trim());
			}

			System.out.println(distributeCandy(ratings));
		}

		scanner.close();
	}

	public static int distributeCandy(int[] ratings) {
		if (ratings == null || ratings.length == 0) {
			return 0;
		}
		if (ratings.length == 1) {
			return 1;
		}
		int n = ratings.length;
		int[] candies = new int[n];

		// 初始化每个孩子至少分到一颗糖果
		for (int i = 0; i < n; i++) {
			candies[i] = 1;
		}

		// 第一次遍历，从左到右，如果右边孩子的评分更高，则右边孩子的糖果数比左边孩子多 1
		for (int i = 1; i < n; i++) {
			if (ratings[i] > ratings[i - 1]) {
				candies[i] = candies[i - 1] + 1;
			}
		}

		// 第二次遍历，从右到左，如果左边孩子的评分更高，且糖果数不满足条件，则左边孩子糖果数更新为右边孩子的糖果数加 1
		for (int i = n - 2; i >= 0; i--) {
			if (ratings[i] > ratings[i + 1]) {
				candies[i] = Math.max(candies[i], candies[i + 1] + 1);
			}
		}

		// 计算总糖果数
		int totalCandies = 0;
		for (int candy : candies) {
			totalCandies += candy;
		}

		return totalCandies;
	}
}
