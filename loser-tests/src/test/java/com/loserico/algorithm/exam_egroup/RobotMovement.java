package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 问题描述: <p/>
 * 现有一个机器人，可放置于 M×N 的网格中任意位置，每个网格包含一个非负整数编号，当相邻网格的数字编号差值的绝对值小于等于 1 时，机器人可以在网格间移动。
 * <p/>
 * 问题： 求机器人可活动的最大范围对应的网格点数目。
 * <p/>
 * 说明： 网格左上角坐标为 (0,0)，右下角坐标为(m−1,n−1)，机器人只能在相邻网格间上下左右移动。
 * <p/>
 * 输入格式: <br/>
 * 第 1 行输入为M 和 N，M 表示网格的行数，N 表示网格的列数。 之后M 行表示网格数值，每行N 个数值（数值大小用k 表示），数值间用单个空格分隔，行首行尾无多余空格。
 * <p/>
 * 输出格式:<br/>
 * 输出 1 行，包含 1 个数字，表示最大活动区域的网格点数目，行首行尾无多余空格。
 * <p/>
 * 样例输入:
 * <pre> {@code
 * 4 4
 * 1 2 5 2
 * 2 4 4 5
 * 3 5 7 1
 * 4 6 2 4
 * }</pre>
 * <p>
 * 样例输出: <br/>
 * 6
 * <p/>
 * 样例解释
 * <pre> {@code
 * +---+---+---+---+
 * | 1 | 2 |*5*| 2 |
 * +---+---+---+---+
 * | 2 |*4*|*4*|*5*|
 * +---+---+---+---+
 * | 3 |*5*| 7 | 1 |
 * +---+---+---+---+
 * | 4 |*6*| 2 | 4 |
 * +---+---+---+---+
 * }</pre>
 * <p/>
 * 图中标记为 * 的区域，相邻网格差值绝对值都小于等于 1，且为最大区域，对应网格点数目为 6。
 * <p/>
 * Copyright: Copyright (c) 2024-09-22 11:03
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RobotMovement {

	private static int maxArea = 0;
	private static int row = 0;

	private static int column = 0;
	private static int[][] grid = null;// 网格数据
	private static boolean[][] visited = null; // 访问标记

	private static int[] dirX = new int[]{-1, 1, 0, 0}; //行方向
	private static int[] dirY = new int[]{0, 0, -1, 1}; //列方向

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入行数, 列数: ");
		String input = scanner.nextLine();
		String[] inputs = input.trim().split(" ");
		row = Integer.parseInt(inputs[0].trim());
		column = Integer.parseInt(inputs[1].trim());
		grid = new int[row][column];
		visited = new boolean[row][column];

		for (int i = 0; i < column; i++) {
			System.out.print("请输入第" + (i + 1) + " 行数据: ");
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			for (int j = 0; j < parts.length; j++) {
				grid[i][j] = Integer.parseInt(parts[j].trim());
			}
		}

		// 对每个未访问的网格点进行DFS搜索
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				if (!visited[i][j]) {
					int area = dfs(i, j);
					maxArea = Math.max(maxArea, area);
				}
			}
		}

		// 输出结果
		System.out.println(maxArea);
	}

	public static int dfs(int x, int y) {
		visited[x][y] = true; // 标记当前网格为已访问
		int area = 1; // 当前区域的大小, 初始为1 (当前网格)

		// 遍历四个方向
		for (int i = 0; i < 4; i++) {
			int newX = x + dirX[i];// 新的行坐标
			int newY = y + dirY[i];// 新的列坐标

			// 检查边界和移动条件
			if (isValid(newX, newY) &&
					!visited[newX][newY] &&
					Math.abs(grid[newX][newY] - grid[x][y]) <= 1) {
				area += dfs(newX, newY);
			}
		}
		return area;
	}

	// 边界检查
	private static boolean isValid(int x, int y) {
		return x >= 0 && y >= 0 && x < row && y < column;
	}
}
