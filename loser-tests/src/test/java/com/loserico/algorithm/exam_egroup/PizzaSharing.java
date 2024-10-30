package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 分披萨
 * <p/>
 * 题目描述
 * <p/>
 * "吃货"和"馋嘴"两人到披萨店点了一份铁盘（圆形）披萨，并嘱咐店员将披萨按放射状切成大小相同的偶数个小块。但是粗心的服务员将披萨切成了每块大小都完全不同奇数块，且肉眼能分辨出大小。
 * <p/>
 * 由于两人都想吃到最多的披萨，他们商量了一个他们认为公平的分法：从"吃货"开始，轮流取披萨。除了第一块披萨可以任意选取外，其他都必须从缺口开始选。
 * <p/>
 * 他俩选披萨的思路不同。"馋嘴"每次都会选最大块的披萨，而且"吃货"知道"馋嘴"的想法。
 * <p/>
 * 已知披萨小块的数量以及每块的大小，求"吃货"能分得的最大的披萨大小的总和。
 * <p/>
 * 输入描述
 * <p/>
 * 第 1 行为一个正整数奇数 N，表示披萨小块数量。 <p/>
 * 接下来的第 2 行到第 N + 1 行（共 N 行），每行为一个正整数，表示第 i 块披萨的大小
 * <p/>
 * 1 ≤ i ≤ N <p/>
 * 披萨小块从某一块开始，按照一个方向次序顺序编号为 1 ~ N
 * <p/>
 * 每块披萨的大小范围为 [1, 2147483647] <p/>
 * 输出描述 <p/>
 * "吃货"能分得到的最大的披萨大小的总和。
 * <p/>
 * 用例
 * <p/>
 * 输入:
 * <br/>
 * 5
 * <br/>
 * 8
 * <br/>
 * 2
 * <br/>
 * 10
 * <br/>
 * 5
 * <br/>
 * 7
 * <p/>
 * 输出:
 * <p/>
 * 19
 * <p/>
 * 说明
 * <p/>
 * 此例子中，有 5 块披萨。每块大小依次为 8、2、10、5、7。
 * <p/>
 * 按照如下顺序拿披萨，可以使"吃货"拿到最多披萨：
 * <p/>
 * "吃货" 拿大小为 10 的披萨
 * <p/>
 * "馋嘴" 拿大小为 5 的披萨
 * <p/>
 * "吃货" 拿大小为 7 的披萨
 * <p/>
 * "馋嘴" 拿大小为 8 的披萨
 * <p/>
 * "吃货" 拿大小为 2 的披萨
 * <p/>
 * 至此，披萨瓜分完毕，"吃货"拿到的披萨总大小为 10 + 7 + 2 = 19
 * <p/>
 * 可能存在多种拿法，以上只是其中一种。
 * <p/>
 * Copyright: Copyright (c) 2024-10-12 20:12
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PizzaSharing {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入披萨块数: ");
		int n = scanner.nextInt();
		int[] pizzas = new int[n];
		for (int i = 0; i < n; i++) {
			System.out.print("请输入第" + (i + 1) + "块披萨的大小: ");
			pizzas[i] = scanner.nextInt();
		}
	}

/*	public static int maxPizza(int[] pizzas) {
		int n = pizzas.length;
		// 扩展数组以简化圆形数组的处理
		int[] exctendedPizzas = new int[2*n];
		System.arraycopy(pizzas, 0, exctendedPizzas, 0, n);
		System.arraycopy(pizzas, 0, exctendedPizzas, n, n);

		// 初始化动态规划表
		int[][] dp = new int[2*n][2*n];

		// 填充动态规划表
		for (int len = 1; len <= n; len++) {
			for (int i = 0; i + len -1 < 2*n; i++) {
				int j = i + len - 1;
				if (len == 1) {
					
				}
			}
			dp[i][i] = pizzas[i];
		}
	}*/
}
