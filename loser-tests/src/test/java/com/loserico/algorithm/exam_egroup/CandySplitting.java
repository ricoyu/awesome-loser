package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 分糖果 <p/>
 * 题目描述: <p/>
 * 小明从糖果盒中随意抓一把糖果，每次小明会取出一半的糖果分给同学们。
 * <p/>
 * 当糖果不能平均分配时，小明可以选择从糖果盒中(假设盒中糖果足够)取出一个糖果或放回一个糖果
 *  <p/>
 * 小明最少需要多少次(取出、放回和平均分配均记一次)，能将手中糖果分至只剩一颗。
 *  <p/>
 * 输入描述: <p/>
 * 抓取的糖果数 (<10000000000) : 15
 * <p/>
 * 输出描述:
 * 最少分至一颗糖果的次数: 5
 * <p/>
 * 输入 15
 * <p/>
 * 输出 5
 *
 * <ol>说明:
 *     <li/>15+1=16;
 *     <li/>16/2=8;
 *     <li/>8/2=4;
 *     <li/>4/2=2;
 *     <li/>2/2=1;
 * </ol>
 *
 *
 * 输入:
 * 3
 * <p/>
 * 输出:
 * 2
 * <p/>
 * <ul>说明:
 *     <li/>3-1=2
 *     <li/>2/2=1
 * </ul>
 * <p/>
 * 输入: 6
 * <p/>
 * 输出:
 * 4
 * <ul>说明:
 *     <li/>6-1=5
 *     <li/>5-1=4
 *     <li/>4/2=2
 *     <li/>2/2=1
 * </ul>
 *
 * <p>
 * Copyright: Copyright (c) 2024-09-10 19:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CandySplitting {

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入小明第"+(i+1)+"次抓取的糖果数: ");
			Scanner scanner = new Scanner(System.in);
			int candies = scanner.nextInt();
			System.out.println("小明最少需要" + minSteps(candies) + "次，能将手中糖果分至只剩一颗");
		}
	}

	public static int minSteps(int candies) {
		int steps = 0;
		// 当糖果数量大于1时，继续执行操作
		while (candies >1) {
			if (candies % 2 == 0) {
				candies /= 2;
			} else {
				/*
				 * 如果是3颗, 再拿一颗出来, 需要除2再除2, 一共三步
				 * 如果是3颗, 放回一颗, 只需要除2就结束了, 两步
				 */
				if ((candies +1)%4==0 && candies !=3) {
					candies++;
				} else {
					candies--;
				}
			}
			steps++; // 每执行一次操作，步骤数增加1
		}

		return steps;
	}
}
