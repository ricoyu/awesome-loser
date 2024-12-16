package com.loserico.algorithm.exam_egroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * 构成正方形的数量 <p/>
 * 问题描述 <p/>
 * 给定 N 个互不相同的二维整数坐标点，求这 N 个坐标点可以构成的正方形数量。需要注意的是，两个向量的内积为零时，这两个向量垂直。
 * <p/>
 * 输入格式 <br/>
 * 第一行输入一个正整数 N，表示坐标点的数量。
 * <br/>
 * 接下来的 N 行，每行包含两个整数 x 和 y，以空格分隔，表示一个坐标点 (x,y)。
 * <p/>
 * 输出格式 <br/>
 * 输出一个整数，表示可以构成的正方形数量。
 * <p/>
 * 样例输入1
 * <pre> {@code
 * 3
 * 1 3
 * 2 4
 * 3 1
 * }</pre>
 * <p>
 * 样例输出1 <p/>
 * 0
 * <p/>
 * 样例输入2
 * <pre> {@code
 * 4
 * 0 0
 * 1 2
 * 3 1
 * 2 -1
 * }</pre>
 * <p>
 * 样例输出2 <p/>
 * 1
 * <p/>
 * 样例解释 <p/>
 * 样例1 3个点不足以构成正方形，因此输出0。 <br/>
 * 样例2 4个点可以构成1个正方形，因此输出1。
 * <p/>
 * 题解 <p/>
 * 这道题目要求我们计算给定的 N 个点中可以构成的正方形数量。解决这个问题的关键在于理解正方形的性质和如何利用坐标系中的点来判断正方形。
 * <p/>
 * 首先，我们需要明白，一个正方形可以由任意两个点确定。当我们知道正方形的一条边（即两个点的坐标）时，我们就可以计算出其他两个点的坐标。
 * <p/>
 * 解题思路如下：
 * <p/>
 * 枚举任意两个点作为正方形的一条边。<br/>
 * 根据这两个点，计算出可能的其他两个点的坐标。<br/>
 * 检查计算出的点是否在给定的点集中。<br/>
 * 如果所有四个点都在点集中，则找到了一个正方形。<br/>
 * <p/>
 * Copyright: Copyright (c) 2024-10-18 11:24
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SquareCounter {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入坐标点数量: ");
		int n = scanner.nextInt(); // 坐标点的数量
		// 用于存储所有点的集合，方便后续查找
		Set<String> pointSet = new HashSet<>();
		// 用于存储点的列表
		List<int[]> points = new ArrayList<>();
		scanner.nextLine();
		// 输入所有点，并将点存入列表和集合
		for (int i = 0; i < n; i++) {
			System.out.print("请输入第" + (i + 1) + " 个坐标点: ");
			String[] parts = scanner.nextLine().trim().split(" ");
			int[] point = new int[2];
			point[0] = Integer.parseInt(parts[0]);
			point[1] = Integer.parseInt(parts[1]);
			points.add(point);
			pointSet.add(point[0] + "," + point[1]);// 使用字符串存储点坐标
		}

		// 记录正方形的数量
		int squareCount = 0;
		int pointCount = points.size();

		// 枚举任意两个点作为正方形的一条边
		for (int i = 0; i < pointCount; i++) {
			for (int j = i + 1; j < pointCount; j++) {
				int[] point1 = points.get(i);
				int[] point2 = points.get(j);
				// 提取A和B的坐标
				int x1 = point1[0], y1 = point1[1];
				int x2 = point2[0], y2 = point2[1];

				// 计算旋转90度后的另外两个点的坐标
				//x1 + (y2 - y1)：通过将 A 点的横坐标 x1 加上 B 点的纵坐标与 A 点纵坐标之差y2−y1，得到新的横坐标 C1 的 x 值
				int[] c1 = {x1 + (y2 - y1), y1 - (x2 - x1)};
				int[] d1 = {x2 + (y2 - y1), y2 - (x2 - x1)};

				int[] c2 = {x1 - (y2 - y1), y1 + (x2 - x1)};
				int[] d2 = {x2 - (y2 - y1), y2 + (x2 - x1)};

				// 检查这两个点是否存在于集合中
				if (pointSet.contains(c1[0] + "," + c1[1]) && pointSet.contains(d1[0] + "," + d1[1])) {
					squareCount++;
				} else if (pointSet.contains(c2[0] + "," + c2[1]) && pointSet.contains(d2[0] + "," + d2[1])) {
					squareCount++;
				}
			}
		}
		// 每个正方形被计算了两次，需要除以2
		System.out.println(squareCount / 2);
	}
}
