package com.loserico.algorithm.exam_egroup;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 斗地主之顺子 <p/>
 * 题目描述： <p/>
 * <p/>
 * 在斗地主扑克牌游戏中， 扑克牌由小到大的顺序为：3,4,5,6,7,8,9,10,J,Q,K,A,2，
 * <br/>
 * 玩家可以出的扑克牌阵型有：单张、对子、顺子、飞机、炸弹等。
 * <br/>
 * 其中顺子的出牌规则为：由 至少 5 张由小到大连续递增 的扑克牌组成，且 不能包含 2 。
 * <p/>
 * 例如：{3,4,5,6,7}、{3,4,5,6,7,8,9,10,J,Q,K,A}都是有效的顺子；
 * <br/>
 * 而{J,Q,K,A,2}、 {2,3,4,5,6}、{3,4,5,6}、{3,4,5,6,8}等都不是顺子。
 * <p/>
 * 给定一个包含13张牌的数组，如果有满足出牌规则的顺子，请输出顺子。
 * <p/>
 * 如果存在多个顺子，请每行输出一个顺子，且需要按顺子的 第一张牌的大小（必须从小到大） 依次输出。
 * <p/>
 * 如果没有满足出牌规则的顺子，请 输出 No 。
 * <p/>
 * 输入描述: <br/>
 * 13张任意顺序的扑克牌，每张扑克牌数字用空格隔开，每张扑克牌的数字都是合法的，并且不包括大小王： <br/>
 * 29J 234K A 79A 56 <br/>
 * 不需要考虑输入为异常字符的情况 <p/>
 *
 * 输出描述: <br/>
 * 组成的顺子，每张扑克牌数字用空格隔开： 34567 <br/>
 * 34567
 * <br/>
 * 示例1：
 * <p/>
 * 输入 <br/>
 * 29J 234K A 79A 56
 * <br/>
 * 输出  <br/>
 * 34567
 * <p/>
 * 说明 <p/>
 * 13张牌中，可以组成的顺子只有1组：3 4 5 6 7
 *  <br/>
 * 示例2：
 *  <br/>
 * 输入 <br/>
 * 29J 1034K A 7Q A 56
 * <p/>
 * 输出 <br/>
 * 34567
 * <p/>
 *  <br/>
 * <p/>
 * 说明
 *  <br/>
 * 13张牌中，可以组成2组顺子，从小到大分别为：3 4 5 6 7 和 9 10 J Q K A
 * <p/>
 * 示例3：
 * <p/>
 * 输入 <br/>
 * 299934K A 10Q A 56
 * <p/>
 * 输出  <br/>
 * No
 * <p/>
 * 说明 <p/>
 * 13张牌中，无法组成顺子
 * <p/>
 * 注意： <p/>
 *
 * 特殊输入 <p/>
 * 3344556677889
 * <p/>
 * 对应输出 <p/>
 * 345678 <p/>
 * 3456789
 * <p/>
 * Copyright: Copyright (c) 2024-10-18 20:55
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class DouDiZhuE {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int[] cards = new int[13];
		for (int i = 0; i < 13; i++) {
			System.out.print("请输入第" + (i + 1) + "张牌：");
			String card = scanner.next();
			cards[i] = cardValue(card.toUpperCase());
		}
		scanner.close();
		findAndPrintSequences(cards);
	}

	public static void findAndPrintSequences(int[] cards) {
		Arrays.sort(cards);
		boolean found = false;

		for (int i = 0; i < cards.length; i++) {
			//至少需要5张连续的卡，且不能以2结束
			if (i+4<cards.length && cards[i+4] < 12) {
				//检查是否为顺子
				if (cards[i] + 1== cards[i+1] &&
						cards[i+1] + 1 == cards[i+2] &&
						cards[i+2] + 1 == cards[i+3] &&
						cards[i+3] + 1 == cards[i+4]) {
					// 打印顺子
					System.out.println("顺子：" + cardChar(cards[i])
							+ cardChar(cards[i+1])
							+ cardChar(cards[i+2])
							+ cardChar(cards[i+3])
							+ cardChar(cards[i+4]));
					System.out.println();
					found = true;
				}
			}
		}

		if (!found) {
			System.out.println("No");
		}
	}

	/**
	 * 将扑克牌映射为数字，方便排序和比较
	 * @param card
	 * @return
	 */
	public static int cardValue(String card) {
		switch (card) {
			case "3": return 0;
			case "4": return 1;
			case "5": return 2;
			case "6": return 3;
			case "7": return 4;
			case "8": return 5;
			case "9": return 6;
			case "10": return 7;
			case "J": return 8;
			case "Q": return 9;
			case "K": return 10;
			case "A": return 11;
			case "2": return 12;
			default: return -1; // 默认为不合法值

		}
	}

	/**
	 * 数字映射回字符表示
	 * @param value
	 * @return
	 */
	private static String cardChar(int value) {
		switch (value) {
			case 0: return "3";
			case 1: return "4";
			case 2: return "5";
			case 3: return "6";
			case 4: return "7";
			case 5: return "8";
			case 6: return "9";
			case 7: return "10";
			case 8: return "J";
			case 9: return "Q";
			case 10: return "K";
			case 11: return "A";
			case 12: return "2"; // 仍然提供2的字符映射，虽然不用于顺子
			default: return "?"; // 不合法值
		}
	}

}
