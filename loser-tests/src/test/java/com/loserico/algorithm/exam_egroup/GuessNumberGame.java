package com.loserico.algorithm.exam_egroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 猜数字 <p/>
 * 问题描述 <p/>
 * 一个人设定一组四码的数字作为谜底，另一方猜。每猜一个数，出数者就要根据这个数字给出提示，提示以 XAYB 形式呈现，直到猜中位置。
 * <p/>
 * 其中 X 表示位置正确的数的个数（数字正确且位置正确），而 Y 表示数字正确而位置不对的数的个数。
 * <p/>
 * 例如，当谜底为 8123，而猜谜者猜 1052 时，出题者必须提示 0A2B。
 * <p/>
 * 例如，当谜底为 5637，而猜谜者猜 4931 时，出题者必须提示 1A0B。
 * <p/>
 * 当前已知 N 组猜谜者猜的数字与提示，如果答案确定，请输出答案，不确定则输出 NA。
 * <p/>
 * 输入格式
 * <p/>
 * 第一行输入一个正整数 𝑁 {@code  0 <N<100}。
 * <p/>
 * 接下来 N 行，每一行包含一个猜测的数字与提示结果。
 * <p/>
 * <pre> {@code
 * 6
 * 4815 1A1B
 * 5716 0A1B
 * 7842 0A1B
 * 4901 0A0B
 * 8585 3A0B
 * 8555 2A1B
 * }</pre>
 * 输出格式: <br/>
 * 输出最后的答案，答案不确定则输出 NA。
 * <p>
 * Copyright: Copyright (c) 2024-09-11 9:06
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class GuessNumberGame {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入猜测次数: ");
		int n = scanner.nextInt();
		String[] guesses = new String[n]; //每次的猜测
		String[] tips = new String[n]; //每次猜测的提示
		scanner.nextLine(); //清空scanner缓存
		for (int i = 0; i < n; i++) {
			System.out.print("请输入第" + (i + 1) + "组猜测的数字与提示: ");
			String input = scanner.nextLine();
			String[] parts = input.split(" ");
			guesses[i] = parts[0];
			tips[i] = parts[1];
		}
		//--------------填充猜测与提示完毕-------------

		List<String> possibleAnswers = new ArrayList<>();
		// 遍历所有可能的四位数
		for (int i = 1000; i <= 9999; i++) {
			String candidate = String.valueOf(i);
			if (isValid(guesses, tips, candidate)) {
				possibleAnswers.add(candidate);
			}
		}

		// 如果只有一个可能的答案
		if (possibleAnswers.size() == 1) {
			System.out.println(possibleAnswers.get(0));
		} else {
			System.out.println("NA");
		}
		scanner.close();
	}

	/**
	 * 判断给定的猜测与提示是否与候选答案匹配
	 *
	 * @param guesses   接受的一组输入中用户猜测的数字组合
	 * @param tips      接受的一组输入中对该猜测的提示
	 * @param candidate 候选答案
	 * @return boolean
	 */
	public static boolean isValid(String[] guesses, String[] tips, String candidate) {
		for (int i = 0; i < guesses.length; i++) {
			if (!matches(candidate, guesses[i], tips[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断给定的猜测与提示是否与候选答案匹配
	 * @param candidate 候选答案
	 * @param guess  用户猜测的数字
	 * @param tip    提示结果
	 * @return
	 */
	public static boolean matches(String candidate, String guess, String tip) {
		int a = 0; // 位置和数字都正确
		int b = 0; // 数字正确位置不正确

		Map<Character, Integer> answerMap = new HashMap<>();
		Map<Character, Integer> guessMap = new HashMap<>();

		//首先计算A的数量(数字正确且位置正确)
		//XAYB A前面的X代表数字正确且位置正确, B前面的Y代表数字正确位置不正确
		/*
		 * 遍历字符串candidate和guess的前4个字符。
		 * 如果当前字符相同，则增加计数器a。
		 * 如果不同，分别在两个映射（answerMap和guessMap）中记录各自字符出现次数。
		 */
		for (int i = 0; i < 4; i++) {
			char aChar = candidate.charAt(i);
			char gchar = guess.charAt(i);
			if (aChar == gchar) {
				a++;
			} else {
				answerMap.put(aChar, answerMap.getOrDefault(aChar, 0) + 1);
				guessMap.put(gchar, guessMap.getOrDefault(gchar, 0) + 1);
			}
		}

		// 计算B的数量
		for (char key : guessMap.keySet()) {
			if (answerMap.containsKey(key)) {
				b += Math.min(answerMap.get(key), guessMap.get(key));
			}
		}

		// 构造结果并与输入结果比较
		String matchResult = a + "A" + b + "B";
		return matchResult.equals(tip);
	}
}
