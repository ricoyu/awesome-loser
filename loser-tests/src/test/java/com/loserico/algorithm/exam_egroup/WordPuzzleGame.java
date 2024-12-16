package com.loserico.algorithm.exam_egroup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 题目描述
 * <p/>
 * 小王设计了一个简单的猜字谜游戏，游戏的谜面是一个错误的单词，比如nesw，玩家需要猜出谜底库中正确的单词。猜中的要求如下： <p/>
 * <ol>对于某个谜面和谜底单词，满足下面任一条件都表示猜中：
 *     <li/>变换顺序以后一样的，比如通过变换w和e的顺序，“nwes”跟“news”是可以完全对应的；
 *     <li/>字母去重以后是一样的，比如“woood”和“wood”是一样的，它们去重后都是“wod”
 * </ol>
 * <p>
 * 请你写一个程序帮忙在谜底库中找到正确的谜底。谜面是多个单词，都需要找到对应的谜底，如果找不到的话，返回”not found”
 * <ul>输入描述:
 *     <li/>谜面单词列表，以“,”分隔
 *     <li/>谜底库单词列表，以","分隔
 * </ul>
 *
 * <ul>输出描述:
 *     <li/>匹配到的正确单词列表，以","分隔
 *     <li/>如果找不到，返回"not found"
 * </ul>
 *
 * <ul>备注
 *     <li/>单词的数量N的范围：0 < N < 1000
 *     <li/>词汇表的数量M的范围：0 < M < 1000
 *     <li/>单词的长度P的范围：0 < P < 20
 *     <li/>输入的字符只有小写英文字母，没有其他字符
 * </ul>
 * <p>
 * 示例1:
 * <p/>
 * 输入: <br/>
 * <pre> {@code
 * conection
 * connection,today
 * }</pre>
 * <p>
 * 输出:
 * connection
 * <p>
 * 示例2
 * <p/>
 * 输入
 * <pre> {@code
 * bdni,wooood
 * bind,wrong,wood
 * }</pre>
 * <p>
 * 输出:
 * bind,wood
 * <p/>
 * Copyright: Copyright (c) 2024-09-12 19:37
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class WordPuzzleGame {

	/**
	 * @param puzzles  谜面
	 * @param wordBank 谜底
	 * @return
	 */
	public static String findCorrectWords(String puzzles, String wordBank) {
		// 分解输入的谜面和谜底库到数组中
		String[] puzzleArray = puzzles.trim().split(",");
		for (int i = 0; i < puzzleArray.length; i++) {
			puzzleArray[i] = puzzleArray[i].trim();
		}
		String[] wordBankArray = wordBank.trim().split(",");
		for (int i = 0; i < wordBankArray.length; i++) {
			wordBankArray[i] = wordBankArray[i].trim();
		}

		// 创建一个用于存放结果的集合，使用LinkedHashSet保持插入顺序
		Set<String> results = new LinkedHashSet<>();

		// 遍历每一个谜面单词
		for (String puzzle : puzzleArray) {
			String sortedPuzzle = sortCharacters(puzzle);
			String dedupPuzzle = deduplicateCharacters(puzzle);

			// 标记是否找到匹配
			boolean found = false;

			// 遍历每一个谜底单词
			for (String word : wordBankArray) {
				String sortedWord = sortCharacters(word);
				String dedupWord = deduplicateCharacters(word);
				// 如果谜底单词和谜面单词排序后一样，或者谜底单词和谜面单词去重后一样，则找到匹配
				if (sortedWord.equals(sortedPuzzle) || dedupWord.equals(dedupPuzzle)) {
					results.add(word);
					found = true;
					break;
				}
			}

			// 如果对于当前谜面单词未找到匹配，添加"not found"
			if (!found && results.isEmpty()) {
				return "not found";
			}
		}

		// 将结果集转换为字符串输出，如果没有找到匹配的单词，返回"not found"
		return results.isEmpty() ? "not found" : String.join(",", results);
	}

	/**
	 * 对字符串的字符进行排序
	 *
	 * @param word
	 * @return
	 */
	public static String sortCharacters(String word) {
		char[] chars = word.toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	}

	/**
	 * 去重字符串中的字符
	 *
	 * @param word
	 * @return
	 */
	public static String deduplicateCharacters(String word) {
		Set<Character> charSet = new HashSet<>();
		for (char c : word.toCharArray()) {
			charSet.add(c);
		}

		StringBuilder sb = new StringBuilder();
		for (char c : charSet) {
			sb.append(c);
		}

		return sb.toString();
	}
}
