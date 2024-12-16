package com.loserico.algorithm.exam_egroup;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * 题目描述: <p/>
 * 给定两个整数数组array1、array2，数组元素按升序排列。
 * <p/>
 * 假设从array1、array2中分别取出一个元素可构成一对元素，现在需要取出k对元素，
 * <p/>
 * 并对取出的所有元素求和，计算和的最小值。
 * <p/>
 * 注意：
 * <p/>
 * 两对元素如果对应于array1、array2中的两个下标均相同，则视为同一对元素。
 * <p/>
 * 输入描述: <p/>
 * 输入两行数组array1、array2，每行首个数字为数组大小size(0 < size <= 100);
 * <p/>
 * 0 < array1[i] <= 1000
 * <p/>
 * 0 < array2[i] <= 1000
 * <p/>
 * 接下来一行为正整数k
 * <p/>
 * 0 < k <= array1.size() * array2.size()
 * <p/>
 * 输出描述 <p/>
 * 满足要求的最小和
 * <p/>
 * <p>
 * 用例 <p/>
 * 输入        3 1 1 2
 * <br/>
 * 3 1 2 3
 * <br/>
 * 2
 * <p/>
 * 输出        4
 * <p/>
 * 说明:
 * <p/>
 * 用例中，需要取2对元素 <p/>
 * 取第一个数组第0个元素与第二个数组第0个元素组成1对元素[1,1];
 * <p/>
 * 取第一个数组第1个元素与第二个数组第0个元素组成1对元素[1,1];
 * <p/>
 * 求和为1+1+1+1=4，为满足要求的最小和。
 * <p/>
 * Copyright: Copyright (c) 2024-09-25 16:05
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MinPairSum {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入第一个数组: ");
		String input = scanner.nextLine();
		String[] parts = input.trim().split(" ");
		int arr1Len = Integer.parseInt(parts[0].trim());
		int[] arr1 = new int[arr1Len];
		for (int i = 0; i < arr1Len; i++) {
			arr1[i] = Integer.parseInt(parts[i + 1].trim());
		}
		System.out.print("请输入第二个数组: ");
		input = scanner.nextLine();
		parts = input.trim().split(" ");
		int arr2Len = Integer.parseInt(parts[0].trim());
		int[] arr2 = new int[arr2Len];
		for (int i = 0; i < arr2Len; i++) {
			arr2[i] = Integer.parseInt(parts[i + 1].trim());
		}

		System.out.print("请输入需要取对数个数k: ");
		int k = scanner.nextInt();

		System.out.println(findMinPairSum(arr1, arr2, k));
	}

	public static int findMinPairSum(int[] arr1, int[] arr2, int k) {
		// 创建一个优先队列，存储配对和及其对应的索引
		PriorityQueue<Pair> minHeap = new PriorityQueue<>(Comparator.comparingInt(pair -> pair.sum));

		// 初始将所有的最小配对（array1[0]与array2[i]）加入优先队列
		for (int i = 0; i < arr1.length; i++) {
			for (int j = 0; j < arr2.length; j++) {
				minHeap.offer(new Pair(arr1[i] + arr2[j], i, j));
			}
		}

		// 结果和
		int minSum = 0;
		// 取出k个最小和
		for (int i = 0; i < k; i++) {
			// 从最小堆中取出最小配对
			Pair pair = minHeap.poll();
			// 将最小配对加入结果和
			minSum += pair.sum;

			// 如果当前配对的array1索引小于size1-1，则可以生成下一个配对
			if (pair.index1 + 1 < arr1.length) {
				// 生成下一个配对并加入最小堆
				int newSum = arr1[pair.index1 + 1] + arr2[pair.index2];
				minHeap.offer(new Pair(newSum, pair.index1 + 1, pair.index2));
			}
		}

		return minSum;
	}

	static class Pair {
		int sum;
		int index1;// array1的索引
		int index2;// array2的索引

		public Pair(int sum, int index1, int index2) {
			this.sum = sum;
			this.index1 = index1;
			this.index2 = index2;
		}
	}
}
