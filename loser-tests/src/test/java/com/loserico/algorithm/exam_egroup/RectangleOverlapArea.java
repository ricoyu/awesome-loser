package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 矩形相交的面积 <p/>
 *
 * 问题描述 <p/>
 * 在平面直角坐标系中，给出 3 个矩形的位置信息。每个矩形由左上角坐标
 * (x,y) 和宽度w、高度h 表示。矩形的边平行于坐标轴，宽度向右延伸，高度向下延伸。请计算这 3 个矩形重叠部分的面积。
 * <p/>
 * 输入格式 <p/>
 * 输入共 3 行，每行包含 4 个整数x、y、w、h，分别表示一个矩形的左上角x 坐标、左上角y 坐标、宽度和高度。
 * <p/>
 * 输出格式 <p/>
 * 输出一个整数，表示 3 个矩形重叠部分的面积。如果没有重叠，则输出 0。
 * <p/>
 * 样例输入1
 * <pre> {@code
 * 1 6 4 4
 * 3 5 3 4
 * 0 3 7 3
 * }</pre>
 *
 * 样例输出1 <br/>
 * 2
 * <p/>
 * 样例解释 <p/>
 * 样例1 三个矩形分别为： <p/>
 * 1. (1,6) 到 (5,2) <br/>
 * 2. (3,5) 到 (6,1) <br/>
 * 3. (0,3) 到 (7,0) <p/>
 * 重叠部分面积为 2 <p/>
 *
 * 题解
 * <p/>
 * 推公式
 * <p/>
 * 首先，需要理解矩形在坐标系中的表示方式。每个矩形由左上角坐标 (x,y) 和宽度 w、高度 h 定义。矩形的右下角坐标可以通过 (x+w,y−h) 计算得到。
 * <p/>
 * 解决这个问题的关键在于找出三个矩形重叠区域的边界。我们可以这样思考：
 * <p/>
 * 重叠区域的左边界是三个矩形左边界的最大值。<br/>
 * 重叠区域的右边界是三个矩形右边界的最小值。<br/>
 * 重叠区域的上边界是三个矩形上边界的最小值。<br/>
 * 重叠区域的下边界是三个矩形下边界的最大值。<br/>
 * <p/>
 * 有了这些信息，我们就可以计算重叠区域的宽度和高度：
 * <p/>
 * 宽度 = 右边界 - 左边界 <br/>
 * 高度 = 上边界 - 下边界 <br/>
 * <p/>
 * 如果计算出的宽度或高度小于等于 0，说明没有重叠区域，此时面积为 0。否则，重叠区域的面积就是宽度乘以高度。
 * <p/>
 * Copyright: Copyright (c) 2024-10-16 10:55
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RectangleOverlapArea {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int[][] rectangles = new int[3][4];
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入第"+(i+1)+"个矩形左上角坐标和宽高: ");
			String input = scanner.nextLine();
			String[] parts = input.trim().split(" ");
			for (int j = 0; j < 4; j++) {
				rectangles[i][j] = Integer.parseInt(parts[j].trim());
			}
		}
		scanner.close();

		System.out.println(calculateOverlapArea(rectangles));
	}

	public static int calculateOverlapArea(int[][] rectangles) {
		int left = Math.max(Math.max(rectangles[0][0], rectangles[1][0]), rectangles[2][0]);
		int right = Math.min(Math.min(rectangles[0][0] + rectangles[0][2], rectangles[1][0] + rectangles[1][2]), rectangles[2][0] + rectangles[2][2]);
		int top = Math.min(Math.min(rectangles[0][1], rectangles[1][1]), rectangles[2][1]);
		int buttom = Math.max(Math.max(rectangles[0][1] - rectangles[0][3], rectangles[1][1] - rectangles[1][3]), rectangles[2][1] - rectangles[2][3]);

		// 计算重叠区域的宽度和高度
		int width = right - left;
		int height = top - buttom;

		// 如果计算出的宽度或高度小于等于0，说明没有重叠区域
		if (width <= 0 || height <= 0) {
			return 0;
		}

		return width * height;
	}
}
