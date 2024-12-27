package com.loserico.algorithm.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 罗马数字转整数
 * <p/>
 * 罗马数字包含以下七种字符: I， V， X， L，C，D 和 M。 <p/>
 * 字符          数值
 * I             1          <br/>
 * V             5          <br/>
 * X             10         <br/>
 * L             50         <br/>
 * C             100        <br/>
 * D             500        <br/>
 * M             1000       <br/>
 * <p/>
 * 例如， 罗马数字 2 写做 II ，即为两个并列的 1 。12 写做 XII ，即为 X + II 。 27 写做  XXVII, 即为 XX + V + II 。
 * <p/>
 * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。这个特殊的规则只适用于以下六种情况：
 * <p/>
 * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。          <br/>
 * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。      <br/>
 * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。  <br/>
 * <p/>
 * 给定一个罗马数字，将其转换成整数。
 * <p/>
 * 示例 1:
 * <p/>
 * 输入: s = "III" <br/>
 * 输出: 3
 * <p/>
 * 示例 2:
 * <p/>
 * 输入: s = "IV" <br/>
 * 输出: 4
 * <p/>
 * 示例 3:
 * <p/>
 * 输入: s = "IX" <br/>
 * 输出: 9
 * <p/>
 * 示例 4:
 * <p/>
 * 输入: s = "LVIII" <br/>
 * 输出: 58 <br/>
 * 解释: L = 50, V= 5, III = 3.
 * <p/>
 * 示例 5:
 * <p/>
 * 输入: s = "MCMXCIV" <br/>
 * 输出: 1994 <br/>
 * 解释: M = 1000, CM = 900, XC = 90, IV = 4.
 *
 * <ul>观察规则：
 *     <li/>罗马数字中较小的数字通常在较大的数字右侧，直接相加。
 *     <li/>如果较小的数字在较大的数字左侧，需要用减法，例如 IV=4。
 * </ul>
 * <ul>算法设计：
 *     <li/>遍历字符串的每个字符，根据字符的值判断当前数字是加法还是减法。
 *     <li/>如果当前字符代表的数字小于后一个字符的数字，执行减法。
 *     <li/>否则，执行加法。
 * </ul>
 * <ul>特殊规则处理：
 *     <li/>用一个Map存储罗马字符到整数的映射关系。
 *     <li/>遍历时根据当前位置的字符与下一字符的关系判断加法或减法。
 * </ul>
 * <p/>
 * Copyright: Copyright (c) 2024-12-23 9:17
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RomanToIntegerConverter {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for(int i = 0; i < 5; i++) {
			System.out.print("请输入数字对: ");
			String input = scanner.nextLine().trim();
			System.out.println(romanToInt(input));
		}
	}

	public static int romanToInt(String s) {
		// 罗马字符与对应整数值的映射表
		Map<Character, Integer> romanMap = new HashMap<>();
		romanMap.put('I', 1);
		romanMap.put('V', 5);
		romanMap.put('X', 10);
		romanMap.put('L', 50);
		romanMap.put('C', 100);
		romanMap.put('D', 500);
		romanMap.put('M', 1000);

		// 初始化结果变量
		int result = 0;
		// 遍历字符串
		for(int i = 0; i < s.length(); i++) {
			// 获取当前字符对应的整数值
			int current = romanMap.get(s.charAt(i));

			// 判断是否存在下一个字符
			if (i <s.length()-1) {
				// 获取下一个字符对应的整数值
				int next = romanMap.get(s.charAt(i+1));
				// 如果当前值小于下一个值，说明需要执行减法
				if (current < next) {
					// 执行减法，减去当前值
					result -= current;
				} else {
					// 执行加法，加上当前值
					result += current;
				}
			} else {
				result += current;
			}
		}
		return result;
	}
}
