package com.loserico.algorithm.exam_egroup;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * 题目描述:
 * <p/>
 * 给定一个正整数数组，设为nums，最大为100个成员，求从第一个成员开始，正好走到数组最后一个成员，所使用的最少步骤数。
 * <ul>要求:
 *     <li/>1、第一步必须从第一元素开始，且1<=第一步的步长<len/2;(len为数组的长度，需要自行解析)。
 *     <li/>2、从第二步开始，只能以所在成员的数字走相应的步数，不能多也不能少,如果目标不可达返回-1，只输出最少的步骤数量。
 *     <li/>3、只能向数组的尾部走，不能往回走。
 * </ul>
 * 输入描述:
 * <p/>
 * 由正整数组成的数组，以空格分隔，数组长度小于100，请自行解析数据数量。
 * <p/>
 * 输出描述: 正整数，表示最少的步数，如果不存在输出-1
 * <p/>
 * 示例1
 * <p/>
 * 输入: <br/>
 * 7 5 9 4 2 6 8 3 5 4 3 9
 * <p/>
 * 输出: 2
 * <p/>
 * 说明
 * <p/>
 * 第一步:第一个可选步长选择2，从第一个成员7开始走2步，到达9;第二步:从9开始，经过自身数字9对应的9个成员到最后。
 * <p/>
 * 示例2:
 * <p/>
 * 输入: <br/>
 * 1 2 3 7 1 5 9 3 2 1
 * <p/>
 * 输出: -1
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-09-14 14:30
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MinStepsToReachEnd {

	public static void main(String[] args) {
		System.out.print("请输入正整数数组: ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();

		String[] parts = input.trim().split(" ");
		int[] nums = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			nums[i] = Integer.parseInt(parts[i].trim());
		}

		int result = minSteps(nums);
		System.out.print(result);
	}

	/**
	 * 队列用于广度优先搜索 (BFS)：
	 * <p/>
	 * 每次从队列中取出当前所在的位置，并根据该位置的值确定下一步的目的地。
	 * 记录已经访问过的位置，避免重复访问。
	 * @param nums
	 * @return int
	 */
	public static int minSteps(int[] nums) {
		int n = nums.length;
		if (n < 2) {
			return -1; // 数组过短，直接返回不可达
		}

		// 第一步步长范围必须在1到len/2之间
		int maxFirstStep = n / 2;

		// 使用队列进行广度优先搜索(BFS)
		Queue<Integer[]> queue = new LinkedList<>();
		boolean[] visited = new boolean[n]; // 记录已经访问过的节点

		// 第一层选择从1到maxFirstStep的步长
		for (int firstStep = 1; firstStep < maxFirstStep; firstStep++) {
			//数组第一个元素表示当前所处位置, 第二个元素表示已走的步数
			queue.offer(new Integer[]{firstStep, 1});
			visited[firstStep] = true;
		}

		// 开始BFS搜索
		while (!queue.isEmpty()) {
			Integer[] current = queue.poll();
			int position = current[0];  // 当前所处的位置
			int step = current[1]; // 已走的步数

			// 如果已经到达数组的最后一个位置，返回步数
			if (position == n - 1) {
				return step;
			}

			// 下一步根据当前所在位置的数字前进
			int nextStep = nums[position];
			int nextPosition = position + nextStep;

			if (nextPosition < n && !visited[nextPosition]) {
				queue.offer(new Integer[]{nextPosition, step + 1});
				visited[nextPosition] = true;
			}
		}

		// 如果所有可能的路径都遍历完了仍然没到达终点，返回-1
		return -1;
	}


}
