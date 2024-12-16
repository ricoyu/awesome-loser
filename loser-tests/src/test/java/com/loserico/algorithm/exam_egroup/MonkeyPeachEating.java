package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 孙悟空吃蟠桃 <p/>
 * 题目描述
 * <p/>
 * 孙悟空喜欢吃蟠桃，一天他乘守卫蟠桃园的天兵天将离开了而偷偷的来到王母娘娘的蟠桃园偷吃蟠桃。
 * <p/>
 * 已知蟠桃园有N棵蟠桃树，第i棵蟠桃树上有N(大于0)个蟠桃，天兵天将将在H(不小于蟠桃树棵数)小时后回来。
 * <p/>
 * 孙悟空可以决定他吃蟠桃的速度K(单位:个/小时)，每个小时他会选择一颗蟠桃树，从中吃掉K个蟠桃，如果这棵树上的蟠桃数小于K，他将吃掉这棵树上所有蟠桃，
 * 然后这一小时内不再吃其余蟠桃树上的蟠桃。
 * <p/>
 * 孙悟空喜欢慢慢吃，但仍想在天兵天将回来前将所有蟠桃吃完。
 * <p/>
 * 求孙悟空可以在H小时内吃掉所有蟠桃的最小速度K(K为整数)。
 * <p/>
 * 输入描述 <p/>
 * 从标准输入中读取一行数字，前面数字表示每棵数上蟠桃个数，最后的数字表示天兵天将将离开的时间。
 * <p/>
 * 输出描述
 * 吃掉所有蟠桃的最小速度K(K为整数）或输入异常时输出-1。
 *
 * <ul>用例:
 *     <li/>输入: 3 11 6 7 8
 *     <li/>输出: 4
 * </ul>
 *
 * <ul>说明:
 *     <li/>天兵天将8个小时后回来，孙悟空吃掉所有蟠桃的最小速度4。
 *     <li/>第1小时全部吃完第—棵树，吃3个，
 *     <li/>第2小时吃4个，第二棵树剩7个，
 *     <li/>第3小时吃4个，第二棵树剩3个，
 *     <li/>第4小时吃3个，第二棵树吃完，
 *     <li/>第5小时吃4个，第三棵树剩2个，
 *     <li/>第6小时吃2个，第三棵树吃完，
 *     <li/>第7小时吃4个，第4棵树剩3个，
 *     <li/>第8小时吃3个，第4棵树吃完。
 * </ul>
 *
 * <p>
 * Copyright: Copyright (c) 2024-09-11 18:41
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MonkeyPeachEating {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数据: ");
		String input = scanner.nextLine();
		// 读取一行输入，并以空格分割
		String[] parts = input.split(" ");
		// 数组最后一个元素是天兵天将回来的时间
		int h = Integer.parseInt(parts[parts.length-1]);
		// 蟠桃树的数量
		int n = parts.length - 1;

		/*
		 * 如果天兵天将的时间小于蟠桃树的数量，即使吃的速度再快也无法在时间内吃完
		 * 因为有条件: 如果这棵树上的蟠桃数小于K，他将吃掉这棵树上所有蟠桃，然后这一小时内不再吃其余蟠桃树上的蟠桃。
		 */
		if (h < n) {
			System.out.println(-1);
			return;
		}

		int[] peaches = new int[n];
		int maxPeaches = 0;  // 记录单棵树最多蟠桃的数量，用于确定K的上界
		for (int i = 0; i < n; i++) {
			peaches[i] = Integer.parseInt(parts[i]);
			if (peaches[i] > maxPeaches) {
				maxPeaches = peaches[i];
			}
		}

		// 二分查找最小的速度K
		int low = 1, high = maxPeaches;
		while (low < high) {
			int mid = low + (high - low)/2;
			// 如果当前速度mid可以吃完所有蟠桃，则继续缩小范围，否则扩大范围
			if (canFinish(peaches, h, mid)) {
				high = mid; // 如果能在H小时内以mid的速度吃完，尝试更小的速度
			} else {
				low = mid + 1;  // 否则尝试更大的速度
			}
		}

		// 输出最终确定的最小速度K
		System.out.println(low);
	}

	/**
	 * 判断是否可以在指定小时内以给定的速度吃完所有蟠桃
	 * @param peaches 蟠桃数量数组
	 * @param h 天兵天将回来时间(可用小时数)
	 * @param k 当前假设的吃蟠桃速度
	 * @return 如果可以在H小时内吃完返回true，否则返回false
	 */
	public static boolean canFinish(int[] peaches, int h, int k) {
		int timeNeeded = 0;
		for (int peachCount : peaches) {
			// 计算每棵树需要的时间，向上取整
			timeNeeded+= (peachCount +k -1)/k; //为什么要这么算参考: 向上取整技巧
			if (timeNeeded > h) {
				return false; // 如果所需时间超过了可用时间，返回false
			}
		}
		return true;
	}
}
