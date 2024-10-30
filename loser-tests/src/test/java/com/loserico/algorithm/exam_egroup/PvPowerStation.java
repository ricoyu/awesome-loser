package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 祖国西北部有一片大片荒地，其中零星的分布着一些湖泊，保护区，矿区；整体上常年光照良好，但是也有一些地区光照不太好。
 * 某电力公司希望在这里建设多个光伏电站，生产清洁能源。对每平方公里的土地进行了发电评估，其中不能建设的区域发电量为0kw，
 * 可以发电的区域根据光照，地形等给出了每平方公里年发电量x千瓦。我们希望能够找到其中集中的矩形区域建设电站，能够获得良好的收益。
 * <p>
 * <p>
 * 输入描述
 * <p/>
 * 第一行输入为调研的地区长，宽，以及准备建设的电站【长宽相等，为正方形】的边长，最低要求的发电量
 * <p/>
 * 之后每行为调研区域每平方公里的发电量
 *
 * <ul>例如，输入为：
 *     <li/>2 5 2 6
 *     <li/>1 3 4 5 8
 *     <li/>2 3 6 7 1
 * </ol>
 * <p>
 * 表示调研的区域大小为长2宽5的矩形，我们要建设的电站的边长为2，建设电站最低发电量为6
 * <p>
 * 输出描述
 * <p>
 * 输出为这样的区域有多少个
 * <p>
 * 上述输入长宽为2的正方形满足发电量大于等于6的区域有4个。
 * <p>
 * 则输出为：4
 * <p>
 * 示例1
 *
 * <ul>输入
 *     <li/>2 5 2 6
 *     <li/>1 3 4 5 8
 *     <li/>2 3 6 7 1
 *     <li/>
 * </ol>
 * <p>
 * 说明
 * <p>
 * 输入长为2，宽为5的场地，建设的场地为正方形场地，边长为2，要求场地的发电量大于等于6
 * <p>
 * 输出 4
 * <p>
 * 示例2
 * <ul>输入
 *     <li/>2 5 1 6
 *     <li/>1 3 4 5 8
 *     <li/>2 3 6 7 1
 * </ol>
 * <p>
 * 输出 3
 * <p>
 * <p>
 * 表示调研的区域大小为长2宽5的矩形，我们要建设的电站的边长为2，建设电站最低发电量为6
 *
 * 具体分析看笔记
 * Copyright: Copyright (c) 2024-09-06 16:41
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PvPowerStation {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入调研的地区长，宽，准备建设的电站的边长, 最低要求的发电量: ");
		String input = scanner.nextLine();
		String[] arr = input.split(" ");
		if (arr.length != 4) {
			System.out.println("输入有误");
			System.exit(1);
		}
		//2 5 2 6
		int rows = Integer.parseInt(arr[0]); //调研的地区长
		int columns = Integer.parseInt(arr[1]); //调研的地区宽
		int sideLength = Integer.parseInt(arr[2]); //电站的边长
		int minPower = Integer.parseInt(arr[3]); //最低要求的发电量

		int[][] powers = new int[rows][columns]; //存的是第二行开始的输入的数据

		// 读取每平方公里的发电量
		for (int i = 0; i < rows; i++) {
			System.out.print("请输入每平方公里的发电量: ");
			String power = scanner.nextLine();
			String[] arr2 = power.split(" ");
			if (arr2.length != columns) {
				System.out.println("每平方公里的发电量输入有误");
				System.exit(1);
			}
			for (int j = 0; j < columns; j++) {
				powers[i][j] = Integer.parseInt(arr2[j].trim());
			}
		}

		//构造前缀和数组
		int[][] prefixSum = new int[rows + 1][columns + 1];
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= columns; j++) {
				prefixSum[i][j] = powers[i - 1][j - 1]
						+ prefixSum[i - 1][j]
						+ prefixSum[i][j - 1]
						-prefixSum[i - 1][j - 1];
			}
		}

		int count = 0;

		for (int i = 1; i <= rows - sideLength + 1; i++) {
			for (int j = 1; j <= columns - sideLength + 1; j++) {
				// 计算每个正方形的发电量
				int power = prefixSum[i + sideLength - 1][j + sideLength - 1]
						- prefixSum[i + sideLength - 1][j - 1]
						- prefixSum[i - 1][j + sideLength - 1]
						+ prefixSum[i - 1][j - 1];
				if (power >= minPower) {
					count++;
				}
			}
		}

		System.out.println(count);
		scanner.close();
	}
}
