package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * 题目描述
 * <p/>
 * 主管期望你来实现英文输入法单词联想功能。需求如下：依据用户输入的单词前缀，从已输入的英文语句中联想出用户想输入的单词，按字典序输出联想到的单词序列，如果联想不到，请输出用户输入的单词前缀。
 * 注意：英文单词联想时，区分大小写，缩略形式如”don’t”，判定为两个单词，”don”和”t”，输出的单词序列，不能有重复单词，且只能是英文单词，不能有标点符号
 * <p/>
 * 输入描述:
 * <ol>输入为两行。
 *     <li/>首行输入一段由英文单词word和标点符号组成的语句str
 *     <li/>接下来一行为一个英文单词前缀pre
 * </ol>
 * <pre> {@code
 * 0 < word.length() <= 20
 * 0 < str.length <= 10000
 * 0 < pre <= 20
 * }</pre>
 * <p>
 * 输出描述: 输出符合要求的单词序列或单词前缀，存在多个时，单词之间以单个空格分割
 * <p/>
 * 用例1:
 * <ul>输入:
 *     <li/>I love you
 *     <li/>He
 * </ul>
 * 输出: He
 * <p/>
 * 说明：从用户已输入英文语句”I love you”中提炼出“I”、“love”、“you”三个单词，接下来用户输入“He”，从已输入信息中无法联想到任何符合要求的单词，因此输出用户输入的单词前缀。
 * <p/>
 * 用例2:
 * <ul>输入:
 *     <li/>The furthest distance in the world, Is not between life and death, But when I stand in front of you, Yet you don't know that I love you.
 *     <li/>f
 * </ul>
 * 输出: front furthest
 * <p/>
 * 说明：从用户已输入英文语句”The furthestdistance in the world, Is not between life and death, But when I stand in frontof you,
 * Yet you dont know that I love you.”中提炼出的单词，符合“f”作为前缀的，有“furthest”和“front”，按字典序排序并在单词间添加空格后输出，结果为“front furthest”。
 * <p>
 * Copyright: Copyright (c) 2024-09-09 18:58
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class EnglishWordPrediction {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入单词序列: ");
		var str = scanner.nextLine();
		System.out.print("请输入单词前缀: ");
		var pre = scanner.nextLine();
		scanner.close();

		// 提取单词, 并存入 TreeSet 自动去重并排序
		Set<String> words = extractWords(str);
		// 进行单词联想
		String result = predictWords(words, pre);
		System.out.println(result);
	}

	/**
	 * 此方法通过正则表达式分割字符串，以提取所有单词。对于包含缩略符号的情况，它将字符串进一步分割。
	 * @param str
	 * @return
	 */
	public static Set<String> extractWords(String str) {
		Set<String> words = new TreeSet<>();
		// 正则表达式匹配单词，处理缩略形式如 don’t
		String[] tokens = str.split("[\\s,.!?;:()]+");
		for (String token : tokens) {
			// 处理缩略形式
			if (token.contains("'")) {
				String[] parts = token.split("'");
				for (String part : parts) {
					if (!part.isEmpty()) {
						words.add(part);
					}
				}
			} else {
				words.add(token);
			}
		}
		return words;
	}

	/**
	 * 此方法检查每个单词是否以指定的前缀开始，并构建结果字符串
	 * @param words
	 * @param prefix
	 * @return
	 */
	public static String predictWords(Set<String> words, String prefix) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (String word : words) {
			if (word.contains(prefix)) {
				if (!first) {
					sb.append(" ");
				}
				sb.append(word);
				first = false;
			}
		}

		return !sb.isEmpty() ? sb.toString() : prefix;
	}
}
