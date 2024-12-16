package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;
import java.util.Stack;

/**
 * 题目描述：
 * <p/>
 * 一个荒岛上有若干人，岛上只有一条路通往岛屿两端的港口，大家需要逃往两端的港口才可逃生。假定每个人移动的速度一样，且只可选择向左或
 * 向右逃生。若两个人相遇，则进行决斗，战斗力强的能够活下来，并损失掉与对方相同的战斗力；若战斗力相同，则两人同归于尽。
 * <p/>
 * 输入描述：
 * <p/>
 * 给定一非0整数数组，元素个数不超过30000；正负表示逃生方向（正表示向右逃生，负表示向左逃生），绝对值表示战斗力，越左边的数字表示离左边港口越近，逃生方向相同的人永远不会发生决斗。
 * <p/>
 * 输出描述：<br/>
 * 能够逃生的人总数,没有人逃生输出0，输入异常时输出-1。
 * <p/>
 * 补充说明：
 * <p/>
 * 示例1 <p/>
 * 输入：<br/>
 * 5 10 8 -8 -5 <p/>
 * 输出： <p/>
 * 2
 * <p/>
 * 说明：<br/>
 * 第3个人和第4个人同归于尽，第2个人杀死第5个人并剩余5战斗力，第1个人没有遇到敌人。
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-09-25 19:44
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class EscapeIsland {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入整数数组: ");
		String input = scanner.nextLine();
		String[] parts = input.trim().split(" ");
		int[] arr = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			arr[i] = Integer.parseInt(parts[i].trim());
		}

		System.out.println(countSurvivors(arr));
	}

	/**
	 * 解决思路: <p/>
	 * 数据结构选择：使用一个栈来追踪当前还在逃生中的人。每次遇到一个新的逃生者时，检查栈顶的逃生者：
	 * <p/>
	 * 如果新逃生者的方向是右（正），则直接将其加入栈。<br/>
	 * 如果新逃生者的方向是左（负），则与栈顶的逃生者进行对决。
	 * <p/>
	 * 对决规则：
	 * <p/>
	 * 如果栈顶的逃生者的战斗力大于新逃生者，栈顶逃生者减去新逃生者的战斗力，保持在栈中。<br/>
	 * 如果栈顶的逃生者的战斗力小于新逃生者，则新逃生者胜出，栈顶逃生者出栈（战斗力减少至零），新逃生者的战斗力减去栈顶的战斗力。<br/>
	 * 如果两者战斗力相同，则两者都出局。<br/>
	 * 结果统计：最后栈中剩下的逃生者即为能够逃生的人，返回栈的大小。  <p/>s
	 *
	 * @param arr
	 * @return
	 */
	public static int countSurvivors(int[] arr) {
		if (arr == null || arr.length == 0) {
			return -1;
		}

		Stack<Integer> stack = new Stack<>();

		for (int power : arr) {
			/*
			 * 处理逃生者
			 * 大于0的入栈, 小于0的跟栈顶元素PK
			 */
			if (power > 0) {
				stack.push(power); // 向右逃生，直接入栈
			} else {
				// 向左逃生，需要与栈中的人对决
				while (!stack.isEmpty() && stack.peek() > 0) {
					Integer topPower = stack.pop();
					power = topPower + power;
					if (power > 0) {
						// 栈顶逃生者胜出，剩余战斗力入栈
						stack.push(power);
						break;
					} else if (power < 0) {
						// 新逃生者胜出，继续对决
					} else {
						// 两者战斗力相同，都出局
						break;// 直接退出循环
					}
				}
				if (power < 0) {
					// 如果新逃生者的战斗力还在，表示他活下来了
					stack.push(power);
				}
			}
		}
		return stack.size(); // 栈中剩余的逃生者数即为能够逃生的人数
	}

}
