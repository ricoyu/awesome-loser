package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 题目描述:
 * <p/>
 * 程序员小明打了一辆出租车去上班。出于职业敏感，他注意到这辆出租车的计费表有点问题，总是偏大。
 * <p/>
 * 出租车司机解释说他不喜欢数字4，所以改装了计费表，任何数字位置遇到数字4就直接跳过，其余功能都正常。
 *
 * <ul>比如：
 *     <li/>23再多一块钱就变为25；
 *     <li/>39再多一块钱变为50；
 *     <li/>399再多一块钱变为500；
 * </ul>
 * <p>
 * 小明识破了司机的伎俩，准备利用自己的学识打败司机的阴谋。
 * <p/>
 * 给出计费表的表面读数，返回实际产生的费用。
 * <p/>
 * 输入描述: 只有一行，数字N，表示里程表的读数。
 * <p/>
 * (1<=N<=888888888)。
 * <p/>
 * 输出描述: 一个数字，表示实际产生的费用。以回车结束。
 *
 * <ul>示例1:
 *     <li/>输入: 5
 *     <li/>输出: 4
 * </ul>
 * <p>
 * 说明:
 * <p/>
 * 5表示计费表的表面读数。
 * <p/>
 * 4表示实际产生的费用其实只有4块钱。
 *
 * <ul>示例2:
 *     <li/>输入: 17
 *     <li/>输出: 15
 * </ul>
 * 说明:
 * <p/>
 * 17表示计费表的表面读数。
 * <p/>
 * 15表示实际产生的费用其实只有15块钱。
 * <p/>
 *
 * <ul>示例3
 *     <li/>输入: 100
 *     <li/>输出: 81
 * </ul>
 * 说明:
 * <p/>
 * 100表示计费表的表面读数。
 * <p/>
 * 81表示实际产生的费用其实只有81块钱
 * <p/>
 * Copyright: Copyright (c) 2024-09-13 10:38
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class TaxiFeeCalculator {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 4; i++) {
			System.out.print("请输入计价器读数: ");
			int count = scanner.nextInt();
			System.out.println("实际费用: " + realCount(count));
		}
		scanner.close();
	}

	public static int realCount(int count) {
		int realCount = 0;
		for (int i = 0; i < count; i++) {
			// 如果数字i中不包含数字4，那么实际的费用数量需要增加
			if (!containerFour(i)) {
				realCount++;
			}
		}
		return realCount;
	}

	/**
	 * 判断数字是否包含数字4
	 *
	 * @param number
	 * @return
	 */
	public static boolean containerFour(int number) {
		while (number > 0) {
			if (number % 10 == 4) {
				return true;
			}
			number /= 10;
		}
		return false;
	}
}
