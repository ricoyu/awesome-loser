package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 简单的自动曝光 <p/>
 * <p>
 * 问题描述 <br/>
 * 给定一个长度为 n 的整数数组img, 表示一张图像的像素值，每个像素值的范围是[0,255] 的整数。现在需要给每个像素值加上一个整数
 * k（可以是负数），得到新的图像newImg，使得newImg 中所有像素的平均值最接近中位值 128。请计算并输出这个整数k。
 * <p/>
 * 输入格式 <br/>
 * 一行n 个整数，用空格分隔，表示原图像img 的像素值。
 * <p/>
 * 输出格式 <br/>
 * 一个整数k。
 * <p/>
 * 样例输入
 * <p/>
 * 样例 1
 * <p/>
 * 样例输入 <br/>
 * 0 0 0 0 <br/>
 * 样例输出: 128
 * <p/>
 * 样例 2 <p/>
 * 样例输入 <br/>
 * 129 130 129 130 <p/>
 * 样例输出: -2
 * <p/>
 * 样例解释
 * <p/>
 * 样例 1 <br/>
 * 原图像的四个像素值都为 0，加上 128 后，新图像的所有像素值变为 128，平均值正好是 128。
 * <p/>
 * 样例 2 <br/>
 * 当k=−2 时，新图像为 127 128 127 128，平均值为 127.5。 两种情况下，127.5 更接近 128，且 -2 小于 -1，所以输出 -2。
 * <p/>
 * <p>
 * 数据范围 <br/>
 * 1≤n≤100 <p/>
 * 如果有多个整数k 都满足条件，输出较小的那个k。 <p/>
 * 新图像的像素值会自动截取到[0,255] 范围。当新像素值<0 时，其值会更改为 0；当新像素值>255 时，其值会更改为 255。
 * <p/>
 * Copyright: Copyright (c) 2024-10-01 16:05
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SimpleAutoExposure {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入数组: ");
			String input = scanner.nextLine();
			String[] parts = input.trim().split(" ");
			int[] img = new int[parts.length];
			for (int j = 0; j < parts.length; j++) {
				img[j] = Integer.parseInt(parts[j].trim());
			}
			System.out.println(findBestK(img));
		}
	}

	/**
	 * 计算最接近128平均值的k值。
	 * @param img 原始图像的像素值数组
	 * @return 最接近128平均值的k值
	 */
	public static int findBestK(int[] img) {
		int closestK = Integer.MAX_VALUE; // 最接近的k值
		double minDifference = Double.MAX_VALUE;// 最小差距初始化为最大浮点数

		// 遍历可能的k值范围
		for (int k = -255; k <= 255; k++) {
			double sum = 0;
			for (int pixel : img) {
				int adjustedPixel = pixel + k;
				if (adjustedPixel < 0) {
					adjustedPixel = 0;
				} else if (adjustedPixel > 255) {
					adjustedPixel = 255;
				}
				sum += adjustedPixel;
			}
			double average = sum / img.length;
			// 计算差值并更新最接近的k值
			double difference = Math.abs(average-128);
			if (difference < minDifference ||
				difference == minDifference && k < closestK) {
				minDifference = difference;
				closestK = k;
			}
		}

		return closestK;
	}
}
