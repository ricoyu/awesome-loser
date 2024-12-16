package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.RegexUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 单词拆分
 * <p>
 * 给你一个字符串 s 和一个字符串列表 wordDict 作为字典。如果可以利用字典中出现的一个或多个单词拼接出 s 则返回 true。
 * <p>
 * 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
 * <p>
 * 示例 1：
 * <p>
 * 输入: s = "leetcode", wordDict = ["leet", "code"]
 * 输出: true
 * 解释: 返回 true 因为 "leetcode" 可以由 "leet" 和 "code" 拼接成。
 * <p>
 * 示例 2：
 * <p>
 * 输入: s = "applepenapple", wordDict = ["apple", "pen"]
 * 输出: true
 * 解释: 返回 true 因为 "applepenapple" 可以由 "apple" "pen" "apple" 拼接成。
 * 注意，你可以重复使用字典中的单词。
 * <p>
 * 示例 3：
 * <p>
 * 输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
 * 输出: false
 *
 * <ul>解题思路:
 *     <li/>定义问题：我们需要判断是否存在一种方式将字符串 s 通过字典中的单词组合起来。为了实现这一点，我们可以使用动态规划的思想。
 *     <li/>定义状态：用一个布尔数组 dp 来表示 dp[i] 是否可以通过字典中的单词拼接得到 s 的前 i 个字符。
 *          也就是说，如果 dp[i] 是 true，表示前 i 个字符可以由字典中的单词拼接而成。
 *     <li/>状态转移：
 *          对于字符串的每一个位置 i，我们检查从 0 到 i 的每一个子串 s[j:i] 是否在字典 wordDict 中。如果 s[j:i] 在字典中并且 dp[j] 为 true，则 dp[i] 设为 true。
 *     <li/>初始化：初始状态 dp[0] 设为 true，表示空字符串可以由字典中的单词拼接得到。
 *     <li/>最终结果：dp[s.length()] 的值就是我们所求的结果。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-10-28 15:37
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class WordBreak {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Pattern pattern = Pattern.compile("\"(.+)\s");
		List<String> wordDict = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			System.out.print("请输入字符串s: ");
			String s = scanner.nextLine();
			System.out.print("请输入字典: ");
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			for (int j = 0; j < parts.length; j++) {
				String word = RegexUtils.trimQuotes(parts[j].trim());
				wordDict.add(word);
			}
			System.out.println(wordBreak(s, wordDict));
		}
	}

	public static boolean wordBreak(String s, List<String> wordDict) {
		// 将 wordDict 转换为 HashSet 以便于 O(1) 时间复杂度进行查找
		Set<String> wordSet = new HashSet<>(wordDict);

		//创建一个 dp 数组，长度为 s.length() + 1
		//dp[i] 表示 s 的前 i 个字符是否可以由字典中的单词拼接而成
		boolean[] dp = new boolean[s.length() + 1];

		// 初始化，空字符串可以被拼接，故 dp[0] 为 true
		dp[0] = true;

		// 遍历字符串的每个字符
		for (int i = 1; i <= s.length(); i++) {
			// 对于每个 i，检查 j 从 0 到 i 的每个子串 s[j:i]
			for (int j = 0; j < i; j++) {
				// 如果 dp[j] 为 true，且 s[j:i] 在字典中
				if (dp[j] && wordSet.contains(s.substring(j, i))) {
					// 则将 dp[i] 设为 true 并跳出循环
					dp[i] = true;
					break;
				}
			}
		}

		return dp[s.length()];
	}
}
