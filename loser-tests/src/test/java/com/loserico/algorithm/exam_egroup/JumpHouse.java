package com.loserico.algorithm.exam_egroup;

import com.loserico.common.lang.utils.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 跳房子，也叫跳飞机，是一种世界性的儿童游戏
 * <p/>
 * 游戏参与者需要分多个回合按顺序跳到第1格直到房子的最后一格。
 * <p/>
 * 跳房子的过程中，可以向前跳，也可以向后跳。
 * <p/>
 * 假设房子的总格数是count，小红每回合可能连续跳的步教都放在数组steps中，请问数组中是否有一种步数的组合，可以让小红两个回合跳到量后一格?如果有，请输出索引和最小的步数组合.
 * <p/>
 * 注意:
 * <p/>
 * 数组中的步数可以重复，但数组中的元素不能重复使用 <p/>
 * 提供的数据保证存在满足题目要求的组合，且索引和最小的步数组合是唯一的
 * <p/>
 * 输入描述
 * <br/>
 * 第一行输入为每回合可能连续跳的步数，它是int整数数组类型。实际字符串中整数与逗号间可能存在空格。
 * <br/>
 * 第二行输入为房子总格数count，它是int整数类型。
 * <p/>
 * 输出描述
 * <p/>
 * 返回索引和最小的满足要求的步数组合(顺序保持steps中原有顺序)
 * <p/>
 * 示例1
 * <p/>
 * 输入: <br/>
 * 1 4 5 2 2<br/>
 * 7 <p/>
 * 输出: <br/>
 * [5, 2]
 * <p/>
 * 示例2：
 * <p/>
 * 输入: <br/>
 * -1 2 4 9 6 <br/>
 * 8
 * <p/>
 * 输出:<br/>
 * [-1, 9]
 * <p/>
 * 说明
 * <p/>
 * 此样例有多种组合满足两回合跳到最后，譬如:[-1,9]，[2,6]，其中[-1,9]的索引和为0+3=3,[2,6]的索引和为1+4=5，所以索引和最小的步数组合[-1,9]
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-09-27 10:05
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class JumpHouse {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入步数组合: ");
		String input = scanner.nextLine();
		String[] parts = input.trim().split(" ");
		int[] steps = new int[parts.length];
		for (int i = 0; i < steps.length; i++) {
			steps[i] = Integer.parseInt(parts[i].trim());
		}
		System.out.print("请输入房子的总格数: ");
		int count = scanner.nextInt();
		int[] combination = findCombination(steps, count);
		Arrays.print(combination);
		scanner.close();
	}

	/**
	 * 找到跳房子的组合
	 *
	 * @param steps 连续跳的步数
	 * @param count 房子的总格数
	 * @return
	 */
	public static int[] findCombination(int[] steps, int count) {
		int n = steps.length;
		int minIndexSum = Integer.MAX_VALUE; // 初始化最小索引和为最大值
		List<int[]> validCombinations = new ArrayList<>(); // 存储满足条件的组合

		// 遍历每对不同的步数组合
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					continue; // 跳过相同的步数
				}
				int totalJump = steps[i] + steps[j]; // 计算总跳跃
				if (totalJump == count) {
					int indexSum = i + j; // 计算索引和
					if (indexSum < minIndexSum) {
						minIndexSum = indexSum; // 更新最小索引和
						validCombinations.clear(); // 清空之前的组合列表
						validCombinations.add(new int[]{steps[i], steps[j]}); // 添加当前组合
					} else if (indexSum == minIndexSum) {
						validCombinations.add(new int[]{steps[i], steps[j]});// 添加当前组合到列表中
					}
				}
			}
		}
		// 返回找到的组合
		return validCombinations.isEmpty() ? null : validCombinations.get(0);
	}
}
