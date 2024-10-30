package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 近些年来，我国防沙治沙取得显著成果。某沙漠新种植N棵胡杨（编号1-N），排成一排。一个月后，有M棵胡杨未能成活。<p/>
 * 现可补种胡杨K棵，请问如何补种（只能补种，不能新种），可以得到最多的连续胡杨树？ <p/>
 * <p>
 * 输入格式 <p/>
 * <ul>输入共四行：
 *     <li/>第一行包含一个整数N，表示总种植数量。
 *     <li/>第二行包含一个整数M，表示未成活胡杨数量。
 *     <li/>第三行包含M 个用空格分隔的整数，表示未成活胡杨的编号（按从小到大排列）。
 *     <li/>第四行包含一个整数K，表示最多可以补种的数量。
 * </ul>
 * <p>
 * 输出格式 <p/>
 * 输出一个整数，表示最多的连续胡杨棵树。
 * <p/>
 * 样例输入1
 * <pre> {@code
 * 5
 * 2
 * 2 4
 * 1
 * }</pre>
 * <p>
 * 样例输出1 <p/>
 * 3
 * <p>
 * 样例解释1 <p/>
 * 补种到 2 或 4 结果一样，最多的连续胡杨棵树都是 3。
 * <p/>
 * 样例输入2
 * <pre> {@code
 * 10
 * 3
 * 2 4 7
 * 1
 * }</pre>
 * <p>
 * 样例输出2 <p/>
 * 6
 * <p/>
 * 样例解释2 <p/>
 * 补种第 7 棵树，最多连续胡杨树棵数为 6（5、6、7、8、9、10）。
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-10-04 10:22
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class DesertReforestation2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入N: ");
		int n = scanner.nextInt(); // 总树木数量
		int m = scanner.nextInt(); // 未成活的树木数量

		if (m == 0) {
			System.out.println(n);
		}

		int[] deadTrees = new int[m + 2]; // 增加边界处理，包含0和N+1
		System.out.print("请输入未成活的胡杨编号: ");
		String input = scanner.nextLine();
		String[] parts = input.trim().split(" ");
		for (int i = 1; i <= m; i++) {
			deadTrees[i] = Integer.parseInt(parts[i - 1].trim());
		}
		deadTrees[0] = 0; // 前边界
		deadTrees[m + 1] = n + 1; // 后边界

		int k = scanner.nextInt(); // 补种数量

		int maxAlive = 0;  // 初始化最长连续树的数量

		// 遍历每个间隙，并尝试使用补种树填充
		for (int i = 1; i <= m + 1; i++) {
			int currentGap = deadTrees[i] - deadTrees[i - 1] - 1; // 当前死亡树之间的间隔
			if (currentGap > 0) {
				if (k > currentGap) {
					// 如果K足够填满整个间隔，计算连续段并减少K
					maxAlive = Math.max(maxAlive, deadTrees[i] - deadTrees[i - 1] - 1);
					k -= currentGap;
				} else {
					// 如果K不够，尽可能填充并更新连续段长度
					maxAlive = Math.max(maxAlive, k);
					k = 0;
				}
			}
		}

		// 需要考虑补种树剩余情况，可能会在两端额外增加连续段
		int possibleMaxAlive = deadTrees[1] - 1 + k;  // 尝试从左侧开始补种
		possibleMaxAlive = Math.max(possibleMaxAlive, n - deadTrees[m] + k);// 尝试从右侧开始补种
		maxAlive = Math.max(maxAlive, possibleMaxAlive);// 更新最长连续段

		System.out.println(maxAlive);
	}
}
