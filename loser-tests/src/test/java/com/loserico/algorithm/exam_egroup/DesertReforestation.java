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
 * 样例输出1 <br/>
 * 3
 * <p/>
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
 * 样例输出2 <br/>
 * 6
 * <p/>
 * 样例解释2 <p/>
 * 补种第 7 棵树，最多连续胡杨树棵数为 6（5、6、7、8、9、10）。
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-10-04 9:47
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class DesertReforestation {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入总数N: ");
		int n = scanner.nextInt();
		System.out.print("请输入未成活数M: ");
		int m = scanner.nextInt();

		if (m == 0) {
			// 如果没有死亡的树，直接输出总数
			System.out.println(n);
			return;
		}

		scanner.nextLine();
		int[] deadTrees = new int[m];// 存储未成活的胡杨编号
		System.out.print("请输入未成活的胡杨编号: ");
		String input = scanner.nextLine();
		String[] parts = input.trim().split(" ");
		for (int i = 0; i < parts.length; i++) {
			deadTrees[i] = Integer.parseInt(parts[i].trim());
		}

		System.out.print("请输入最多可以补种的数量K: ");
		int k = scanner.nextInt();

		// 确定最多连续胡杨数
		int maxActive = calculateMaxAlive(n, m, deadTrees, k);
		System.out.println(maxActive);
	}

	/**
	 *
	 * @param n 种树的总数
	 * @param m 死亡数的总数
	 * @param deadTrees 未成活的胡杨编号数组
	 * @param k 补充的树数量
	 * @return
	 */
	public static int calculateMaxAlive(int n, int m, int[] deadTrees, int k) {
		int maxAlive = 0;

		// 索引前的死亡树之前的连续成活树数量
		int startSegment = deadTrees[0] - 1;// 第一个死亡树前的活树数量

		// 索引后的死亡树之后的连续成活树数量
		int endSegment = n - deadTrees[m - 1];

		// 最大连续树初始化为已知的活树段
		maxAlive = Math.max(maxAlive, startSegment);
		maxAlive = Math.max(maxAlive, endSegment);

		// 遍历每对相邻的死亡树，考虑补种情况
		for (int i = 0; i < m - 1; i++) {
			int gap = deadTrees[i + 1] - deadTrees[i] - 1;// 两死亡树之间有多少颗树
			// 如果补种树数量足以覆盖这个gap
			if (k >= gap) {
				int totalAlive =
						gap + (deadTrees[i] - (i > 0 ? deadTrees[i - 1] : 0) - 1) + (deadTrees[i + 1] - deadTrees[i] - 1);
				if (i == 0) {
					totalAlive += startSegment; // 包括序列开始的段
					if (i + 1 == m - 1) {
						totalAlive += endSegment;// 包括序列结束的段
					}
					if (i + 1 == m - 1) {
						totalAlive += endSegment;// 包括序列结束的段
					}
					maxAlive = Math.max(maxAlive, totalAlive);
				}
			}
		}
		// 考虑所有死亡树间距离总和是否小于K，如果是，可能可以连接整个序列
		int totalGaps = 0;
		for (int i = 1; i < m; i++) {
			totalGaps += deadTrees[i] - deadTrees[i - 1] - 1;
		}

		if (k >= totalGaps + m) {
			// 能够补种所有未成活的树
			maxAlive = n;
		}

		return maxAlive;
	}
}
