package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 题目描述:
 * <p/>
 * 一群大雁往南飞，给定一个字符串记录地面上的游客听到的大雁叫声，请给出叫声最少由几只大雁发出。具体的:
 * <p/>
 * 1.大雁发出的完整叫声为"quack"，因为有多只大雁同一时间嘎嘎作响，所以字符串中可能会混合多个"quack"。
 * <p/>
 * 2.大雁会依次完整发出”quack”，即字符串中’q’,‘u’,‘a’,‘℃’,‘k' 这5个字母按顺序完整存在才能计数为一只大雁。如果不完整或者没有按顺序则不予计数。
 * <p/>
 * 3.如果字符串不是由'q','u','a','c','k'字符组合而成，或者没有找到一只大雁，请返回-1。
 * <p/>
 * 输入描述:
 * <p/>
 * 一个字符串，包含大雁quack的叫声。1<=字符串长度<=1000，字符串中的字符只有'q','u','a','c','k'。
 * <p/>
 * 输出描述: 大雁的数量
 * <p/>
 * 示例1:
 * <p/>
 * 输入: quackquack
 * <p/>
 * 输出: 1
 * <p/>
 * 示例2:
 * <p/>
 * 输入: qaauucqckk
 * <p/>
 * 输出: -1
 * <p/>
 * 示例3:
 * <p/>
 * 输入: qquuaacckk
 * <p/>
 * 输出: 2
 * <p/>
 * <p/>
 * Copyright: Copyright (c) 2024-09-16 15:59
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class GooseSound {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入大雁叫声: ");
			String input = scanner.nextLine();
			System.out.println(minNumberOfGeese(input));
		}
	}

	// 定义大雁叫声的顺序/**/
	private static final String QUACK = "quack";

	public static int minNumberOfGeese(String sound) {
		// 如果字符串长度不是5的倍数，肯定无法形成完整的大雁叫声
		if (sound.length() % 5 != 0) {
			return -1;
		}

		// 用于追踪每个大雁发出叫声的不同阶段, q u a c k 的索引位置
		int[] stages = new int[5];

		// 标记一只大雁的开始与结束
		int currentGeese = 0;
		// 记录最大同时发声的大雁数量
		int maxGeese = 0;

		// 遍历输入字符串的每个字符
		for (char c : sound.toCharArray()) {
			switch (c) {
				case 'q': {
					// 开始一个新的"quack"的叫声，进入第一个阶段
					if (stages[0] == currentGeese) {
						currentGeese++;
						maxGeese = Math.max(maxGeese, currentGeese);
					}
					stages[0]++;
					break;
				}
				case 'u': {
					// "u"只能接在"q"后面
					if (stages[0] > stages[1]) {
						stages[1]++;
					} else {
						return -1;// 如果顺序错误，返回-1
					}
					break;
				}
				case 'a': {
					// "a"只能接在"u"后面
					if (stages[2] < stages[1]) {
						stages[2]++;
					} else {
						return -1;
					}
					break;
				}
				case 'c': {
					// "c"只能接在"a"后面
					if (stages[3] < stages[2]) {
						stages[3]++;
					} else {
						return -1;

					}
					break;
				}
				case 'k': {
					// "k"只能接在"c"后面，表示一个大雁完成了叫声
					if (stages[4] < stages[3]) {
						stages[4]++;
						currentGeese--;
					} else {
						return -1;
					}
				}
			}
		}

		// 检查所有阶段是否完成匹配，如果没有匹配完整返回-1
		if (currentGeese != 0) {
			return -1;
		}
		// 返回最大同时发声的大雁数量
		return maxGeese;
	}
}
