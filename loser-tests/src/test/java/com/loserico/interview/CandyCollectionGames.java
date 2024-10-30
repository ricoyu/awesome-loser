package com.loserico.interview;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 宝宝和妈妈参加亲子游戏，在一个二维矩阵(N*N)的格子地图上，宝宝和妈妈抽签决定各自的位置，地图上每个格子有不同的糖果数量，部分格子有障碍物。
 * <p>
 * 游戏规则是妈妈必须在最短的时间(每个单位时间只能走一步)到达宝宝的位置，路上的所有糖果都可以拿走，不能走障碍物的格子，只能上下左右走。
 * <p>
 * 请问妈妈在最短到达宝宝位置的时间内最多拿到多少糖果(优先考虑最短时间到达的情况下尽可能多拿糖果)。
 * <p>
 * 输入描述
 * 第一行输入为 N，N 表示二维矩阵的大小。 之后 N 行，每行有 N 个值，表格矩阵每个位置的值，其中:
 * <ol>
 *     <li/> -3: 妈妈
 *     <li/> -2: 宝宝
 *     <li/> -1: 障碍
 *     <li/> ≥0 : 糖果数(0 表示没有糖果，但是可以走)
 * </ol>
 * <p>
 * 输出描述
 * 输出妈妈在最短到达宝宝位置的时间内最多拿到多少糖果，行末无多余空格。
 * <p>
 * 备注: 地图最大 50 * 50
 * <p>
 * 输入示例1
 * <pre> {@code
 * 4
 * 3 2 1 -3
 * 1 -1 1 1
 * 1 1 -1 2
 * -2 1 2 3
 * }</pre>
 * <p>
 * 输出: 9
 * <p>
 * 输入示例2
 * <pre> {@code
 * 4
 * 3 2 1 -3
 * -1 -1 1 1
 * 1 1 -1 2
 * -2 1 -1 3
 * }</pre>
 * 输出: -1
 * 说明: 此地图妈妈无法到达宝宝的位置
 * <p>
 * Copyright: Copyright (c) 2024-09-05 8:37
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CandyCollectionGames {

	static class Cell {

		/**
		 * x坐标
		 */
		int x;

		/**
		 * y坐标
		 */
		int y;

		/**
		 * 步数
		 */
		int distance;

		/**
		 * 拿到的糖果数量
		 */
		int candies;

		/**
		 *
		 * @param x x坐标
		 * @param y y坐标
		 * @param distance 步数
		 * @param candies  拿到的糖果数
		 */
		public Cell(int x, int y, int distance, int candies) {
			/**
			 * x坐标
			 */
			this.x = x;
			/**
			 * y坐标
			 */
			this.y = y;

			/**
			 * 步数
			 */
			this.distance = distance;
			/**
			 * 糖果数
			 */
			this.candies = candies;
		}

		/**
		 * -3: 妈妈
		 * -2: 宝宝
		 * -1: 障碍
		 * @param args
		 */
		public static void main(String[] args) {
			int[][] grid = {
					{3,2, 1, -3},
					{-1, -1, 1, 1},
					{1, 1, -1, 2},
					{-2, 1, 2, 3}
			};
			//int[][] grid = {
			//		{3,2, 1, -3},
			//		{-1, -1, 1, 1},
			//		{1, 1, -1, 2},
			//		{-2, 1, -1, 3}
			//};
			//int[][] grid = {
			//		{2, 3, 1, 0},
			//		{0, -1, 4, 2},
			//		{1, 2, -1, 3},
			//		{3, 0, 2, 1}
			//};
			int startX = 0, startY = 0;
			int endX = 3, endY = 3;

			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					if (grid[i][j] == -3) {
						startX = i;
						startY = j;
						break;
					}
				}
			}

			System.out.println("Maximum candies collected: " + maxCandies(grid, startX, startY, endX, endY));
		}

		/**
		 *
		 * @param grid      这是传入的二位数组
		 * @param startX    起始点x坐标, 从0开始
		 * @param startY    起始点y坐标, 从0开始
		 * @param endX      终点x坐标, 二位数组的x轴长度-1
		 * @param endY      终点y坐标, 二位数组的y轴长度-1
		 * @return 到达终点时收集到的糖果数
		 */
		public static int maxCandies(int[][] grid, int startX, int startY, int endX, int endY) {
			int n = grid.length;

			/**
			 * -1向上, 1向下, 0向左或右
			 */
			int[] dx = {-1, 1, 0, 0};
			/**
			 * 0向左或右, -1向左, 1向右。注意, 这里的0代表不移动。
			 */
			int[] dy = {0, 0, -1, 1};

			/**
			 * 该Java代码段初始化一个三维数组visited，用于存储两个状态：距离和糖果数。初始化时，将所有位置的距离设为整数最大值，糖果数设为-1。其主要功能包括：
			 * 1. 初始化visited数组，将所有位置的距离设为整数最大值，糖果数设为-1。
			 */
			int[][][] visited = new int[n][n][2]; // [distance, candies]
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					visited[i][j][0] = Integer.MAX_VALUE;  // 为每个位置的距离设置一个初始不可达标志（最大值）
					visited[i][j][1] = -1;  // 为每个位置的糖果数设置一个初始未访问标志(-1)
				}
			}

			/*
			 * 该段代码初始化了一个广度优先搜索队列, 并记录了访问信息:
			 * 创建队列queue, 并将起始单元格(startX, startY)加入队列, 包含位置、步数（0）和颜色值。
			 * 更新visited数组，标记起始位置已访问，同时存储两种访问状态（0和颜色值）。
			 */
			Queue<Cell> queue = new LinkedList<>();
			queue.offer(new Cell(startX, startY, 0, grid[startX][startY]));
			visited[startX][startY][0] = 0;
			visited[startX][startY][1] = grid[startX][startY];

			while (!queue.isEmpty()) {
				Cell current = queue.poll();

				if (current.x == endX && current.y == endY) {
					continue;
				}

				//遍历当前位置(current)的四个方向（上、下、左、右）
				for (int i = 0; i < 4; i++) {
					int nextX = current.x + dx[i];
					int nextY = current.y + dy[i];
					//检查新位置(nextX, nextY)是否在网格范围内且不是障碍物。
					if (nextX >= 0 && nextX < n && nextY >= 0 && nextY < n && grid[nextX][nextY] != -1) { // -1 是障碍
						//计算到达新位置的糖果数(newCandies)和距离(newDistance)
						int newCandies = current.candies + grid[nextX][nextY];
						int newDistance = current.distance + 1;

						//如果新距离更短或距离相同但糖果更多, 则更新已访问信息并入队新位置
						if (newDistance < visited[nextX][nextY][0] ||
								(newDistance == visited[nextX][nextY][0] && newCandies > visited[nextX][nextY][1])) {
							visited[nextX][nextY][0] = newDistance;
							visited[nextX][nextY][1] = newCandies;
							queue.offer(new Cell(nextX, nextY, newDistance, newCandies));
						}
					}
				}

			}

			return visited[endX][endY][1]; // Maximum candies collected with the minimum distance
		}
	}
}
