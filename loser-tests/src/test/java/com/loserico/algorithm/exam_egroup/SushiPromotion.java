package com.loserico.algorithm.exam_egroup;

import com.loserico.common.lang.utils.Arrays;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * 转盘寿司
 * <p/>
 * 题目描述
 * <p/>
 * 寿司店周年庆，正在举办优惠活动回馈新老客户。
 * <p/>
 * 寿司转盘上总共有 n 盘寿司，prices[i] 是第 i 盘寿司的价格，
 * <p/>
 * 如果客户选择了第 i 盘寿司，寿司店免费赠送客户距离第 i 盘寿司最近的下一盘寿司 j，前提是 prices[j] < prices[i]，如果没有满足条件的 j，则不赠送寿司。
 * <p/>
 * 每个价格的寿司都可无限供应。
 * <p/>
 * 输入描述:
 * <br/>
 * 输入的每一个数字代表每盘寿司的价格，每盘寿司的价格之间使用空格分隔，例如
 * <p/>
 * 3 15 6 14
 * <p/>
 * 表示：
 * <br/>
 * 第 0 盘寿司价格 prices[0] 为 3 <br/>
 * 第 1 盘寿司价格 prices[1] 为 15 <br/>
 * 第 2 盘寿司价格 prices[2] 为 6 <br/>
 * 第 3 盘寿司价格 prices[3] 为 14 <br/>
 * <p/>
 * 寿司的盘数 n 范围为：1 ≤ n ≤ 500
 * <p/>
 * 每盘寿司的价格 price 范围为：1 ≤ price ≤ 1000
 * <p/>
 * 输出描述: <br/>
 * <p>
 * 输出享受优惠后的一组数据，每个值表示客户选择第 i 盘寿司时实际得到的寿司的总价格。使用空格进行分隔，例如： <br/>
 * <p>
 * 3 21 9 17
 * <p/>
 * 用例
 * <p/>
 * 输入: 3 15 6 14 <br/>
 * 输出: 3 21 9 17  <br/>
 * <p/>
 * Copyright: Copyright (c) 2024-10-03 17:02
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SushiPromotion {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入价格: ");
		String input = scanner.nextLine();
		String[] parts = input.trim().split(" ");
		int[] prices = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			prices[i] = Integer.parseInt(parts[i].trim());
		}

		//printTwice(prices);
		//int[] result = calculatePromotionPrices(prices);
		int[] result = calculatePromotionPrices2(prices);
		Arrays.print(result);
	}

	public static void printTwice(int[] prices) {
		int n = prices.length;
		for (int i = 0; i < 2 * n; i++) {
			int index = i % n;
			System.out.print(prices[index] + " ");
		}
	}

	/**
	 * 这个版本的比较好理解
	 *
	 * @param prices
	 * @return
	 */
	public static int[] calculatePromotionPrices2(int[] prices) {
		int n = prices.length;
		int[] result = new int[n];

		// 对每个寿司价格进行处理
		for (int i = 0; i < n; i++) {
			result[i] = prices[i];// 默认结果为当前价格
			boolean found = false; // 用来标记是否找到了更低的价格

			// 开始向右寻找第一个更小的价格，包括循环回头
			for (int j = 1; j < n; j++) { // 循环 n-1 次足够
				int nextIndex = (i + j) % n; // 循环索引计算
				if (prices[nextIndex] < prices[i]) {
					result[i] += prices[nextIndex];
					found = true;
					break;
				}
			}
			// 如果没有找到更低的价格，结果已经是当前价格
		}

		return result;
	}

	public static int[] calculatePromotionPrices(int[] prices) {
		int n = prices.length;
		int[] result = new int[n];

		// 使用双端队列来存储索引，这些索引对应的价格是单调递减的
		Deque<Integer> deque = new ArrayDeque<>();

		// 遍历数组两次，以处理循环数组
		for (int i = 0; i < 2 * n; i++) {
			int index = i % n;

			// 维护一个单调递减的队列，只要当前价格小于队尾价格，就移除队尾
			while (!deque.isEmpty() && prices[index] < prices[deque.peekLast()]) {
				deque.removeLast();
			}

			// 检查队头，看是否有满足条件的寿司
			while (!deque.isEmpty() && deque.peekFirst() < i - n) {
				deque.removeFirst();
			}

			// 如果队列不为空且当前元素是后续元素的有效范围内，那么就使用队头作为赠送寿司
			if (!deque.isEmpty() && i < n) {
				result[index] = prices[index] + prices[deque.peekFirst()];
			} else if (i < n) {
				result[index] = prices[index]; // 没有找到更小的，只能计算自身
			}

			// 将当前索引加入队列
			deque.addLast(index);
		}

		return result;
	}
}
