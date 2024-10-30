package com.loserico.algorithm.exam_egroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 问题描述: <p/>
 * 有一台绘图机器，其绘图笔的初始位置在原点(0,0)。机器启动后按照以下规则绘制直线：
 * <p/>
 * 1. 沿着横坐标正向绘制直线，直到给定的终点E。<br/>
 * 2. 在绘制过程中，可以通过指令在纵坐标方向进行偏移。 offsetY 为正数表示向上偏移，为负数表示向下偏移。 <p/>
 * 给定横坐标终点值E 以及若干条绘制指令，请计算绘制的直线与横坐标轴以及x=E 的直线所围成的图形面积。
 * <p>
 * 输入格式: <p/>
 * 第一行包含两个整数N 和 E，表示有N 条指令，机器运行的横坐标终点值为E。
 * <p/>
 * 接下来N 行，每行两个整数表示一条绘制指令𝑥 offsetY。
 * <p/>
 * 保证横坐标x 以递增排序的方式出现，且不会出现相同横坐标x。
 * <p/>
 * 输出格式 <br/>
 * 输出一个整数，表示计算得到的面积。
 * <p/>
 * 样例输入 <br/>
 * 4 10 <br/>
 * 1 1 <br/>
 * 2 1 <br/>
 * 3 1 <br/>
 * 4 -2  <p/>
 * <p>
 * 样例输出: 12
 * <p/>
 * 样例输入 <br/>
 * 2 4 <br/>
 * 0 1 <br/>
 * 2 -2  <p/>
 * <p>
 * 样例输出: 4
 * <p/>
 * Copyright: Copyright (c) 2024-09-26 15:32
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class DrawingMachine {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入指令条数N：");
		int n = scanner.nextInt();
		System.out.print("请输入横坐标终点值E：");
		int e = scanner.nextInt();
		scanner.nextLine();

		List<Integer> positionYs = new ArrayList<>();
		int currentY = 0;

		for (int i = 0; i < n; i++) {
			System.out.print("请输入第" + (i + 1) + "条指令: ");
			String[] aprts = scanner.nextLine().trim().split(" ");
			int offsetY = Integer.parseInt(aprts[1]);
			currentY += offsetY;
			positionYs.add(currentY);
		}

		// 计算面积
		int area = 0;
		for (int i = 0; i < positionYs.size() - 1; i++) {
			// 计算当前和下一个点之间形成的梯形的面积
			int height1 = positionYs.get(i);
			int height2 = positionYs.get(i + 1);
			area += (height1 + height2) * 1; // 每个梯形的宽度为1
		}

		// 处理最后一段到 E 的区域
		int lastHeight = positionYs.get(positionYs.size() - 1);
		area += lastHeight * (e - n); // N是最后一个 x 坐标（横坐标）

		System.out.println(area);
	}
}
