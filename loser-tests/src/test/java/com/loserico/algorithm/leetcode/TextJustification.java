package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 文本左右对齐
 * <p/>
 * 给定一个单词数组 words 和一个长度 maxWidth ，重新排版单词，使其成为每行恰好有 maxWidth 个字符，且左右两端对齐的文本。
 * <p/>
 * 你应该使用 “贪心算法” 来放置给定的单词；也就是说，尽可能多地往每行中放置单词。必要时可用空格 ' ' 填充，使得每行恰好有 maxWidth 个字符。
 * <p/>
 * 要求尽可能均匀分配单词间的空格数量。如果某一行单词间的空格不能均匀分配，则左侧放置的空格数要多于右侧的空格数。
 * <p/>
 * 文本的最后一行应为左对齐，且单词之间不插入额外的空格。
 * <p/>
 * 注意:
 * <p/>
 * 单词是指由非空格字符组成的字符序列。 <br/>
 * 每个单词的长度大于 0，小于等于 maxWidth。 <br/>
 * 输入单词数组 words 至少包含一个单词。 <br/>
 * <p/>
 * 示例 1:
 * <p/>
 * 输入: words = ["This", "is", "an", "example", "of", "text", "justification."], maxWidth = 16 <br/>
 * 输出: <br/>
 * [ <br/>
 * "This    is    an", <br/>
 * "example  of text", <br/>
 * "justification.  " <br/>
 * ]
 * <p/>
 * 示例 2:
 * <p/>
 * 输入:words = ["What","must","be","acknowledgment","shall","be"], maxWidth = 16 <br/>
 * 输出: <br/>
 * [ <br/>
 * "What   must   be", <br/>
 * "acknowledgment  ", <br/>
 * "shall be        " <br/>
 * ] <br/>
 * 解释: 注意最后一行的格式应为 "shall be    " 而不是 "shall     be", <br/>
 * 因为最后一行应为左对齐，而不是左右两端对齐。 <br/>
 * 第二行同样为左对齐，这是因为这行只包含一个单词。
 * <p>
 * <p/>
 * 示例 3:
 * <p/>
 * 输入:words = ["Science","is","what","we","understand","well","enough","to","explain","to","a","computer.","Art",
 * "is","everything","else","we","do"]，maxWidth = 20 <br/>
 * 输出: <br/>
 * [ <br/>
 * "Science  is  what we", <br/>
 * "understand      well", <br/>
 * "enough to explain to", <br/>
 * "a  computer.  Art is", <br/>
 * "everything  else  we", <br/>
 * "do                  " <br/>
 * ]
 * <p/>
 * 解题思路
 * <ul>贪心算法：
 *     <li/>将尽可能多的单词放入每一行，但每行的字符总长度不能超过 maxWidth。
 *     <li/>计算每行单词之间的空格分配。
 *     <li/>如果单词间无法均匀分配空格，左侧的空格数量多于右侧。
 * </ul>
 * <ul>两种情况的处理：
 *     <li/>非最后一行：单词间的空格尽可能均匀分配。
 *     <li/>最后一行：左对齐，单词之间一个空格，末尾补齐空格。
 * </ul>
 * <ul>实现步骤：
 *     <li/>遍历 words，根据单词总长度和空格长度判断单词是否能加入当前行。
 *     <li/>如果不能加入，则开始处理当前行的排版，将其结果加入最终输出。
 *     <li/>特殊处理最后一行，使其左对齐。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2025-01-04 12:12
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class TextJustification {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 3; i++) {
			System.out.print("请输入单词数组: ");
			String input = scanner.nextLine().trim();
			String[] words = Arrays.toStringArray(input);
			System.out.print("请输入最大宽度: ");
			int maxWidth = scanner.nextInt();
			scanner.nextLine();
			List<String> strings = fullJustify(words, maxWidth);
			strings.forEach(System.out::println);
		}
	}

	public static List<String> fullJustify(String[] words, int maxWidth) {
		List<String> result = new ArrayList<>();
		int index = 0; // 当前单词索引

		while (index < words.length) {
			int totalChars = words[index].length(); // 当前行的字符总数
			int last = index + 1;// 指向下一个单词的索引

			// 计算当前行最多可以容纳的单词
			while (last < words.length) {
				if (totalChars + 1 + words[last].length() > maxWidth) {
					break; // +1 表示至少一个空格
				}
				totalChars += 1 + words[last].length(); // 加上空格和单词长度
				last++;
			}

			// 当前行的单词范围是 words[index] 到 words[last-1]
			int numOfWords = last - index;// 当前行单词数量
			int numOfSpaces = maxWidth - totalChars + (numOfWords - 1);// 总空格数

			// 构建当前行字符串
			StringBuilder builder = new StringBuilder();

			// 如果是最后一行或者该行只有一个单词，则左对齐
			if (last == words.length || numOfWords == 1) {
				for (int i = index; i < last; i++) {
					builder.append(words[i]);
					if (i < last - 1) {
						builder.append(" ");// 单词之间一个空格
					}
				}
				// 补齐剩余空格
				while (builder.length() < maxWidth) {
					builder.append(" ");
				}
			} else {
				// 非最后一行，均匀分配空格
				int spacesPerSlot = numOfSpaces / (numOfWords - 1); // 每个间隔的空格数
				int extraSpaces = numOfSpaces % (numOfWords - 1); // 多余的空格数

				for (int i = index; i < last; i++) {
					builder.append(words[i]);
					if (i < last - 1) {// 最后一个单词后不加空格
						for (int j = 0; j < spacesPerSlot; j++) {
							builder.append(" ");
						}
						// 如果还有多余空格，分配到左侧
						if (extraSpaces > 0) {
							builder.append(" ");
							extraSpaces--;
						}
					}
				}
			}
			result.add(builder.toString());
			index = last; // 更新索引，处理下一行
		}

		return result;
	}
}
