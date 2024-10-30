package com.loserico.algorithm.exam_egroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 敏感字段加密
 * <p/>
 * 问题描述:
 * <p/>
 * 给定一个由多个命令字组成的命令字符串S 和一个索引K，要求对指定索引的敏感字段进行加密处理。命令字符串具有以下特征：
 * <p/>
 * 1. 字符串长度不超过127 字节，仅包含大小写字母、数字、下划线和偶数个双引号。
 * <br/>
 * 2. 命令字之间以一个或多个下划线 "_" 分隔。
 * <br/>
 * 3. 两个双引号 """" 可用来标识包含下划线的命令字或空命令字（仅包含两个双引号的命令字）。双引号不会在命令字内部出现。
 * <p/>
 * 要求对索引K 的敏感字段进行加密，将其替换为 "******"（6个星号），并删除命令字前后多余的下划线。如果无法找到指定索引的命令字，则输出字符串 "ERROR"。
 * <p/>
 * 输入格式:
 * <p/>
 * 输入包含两行： 第一行为一个整数K，表示要加密的命令字索引（从0 开始）。  <br/>
 * 第二行为命令字符串S。
 * <p/>
 * 输出格式:
 * <br/>
 * 输出一行字符串，为处理后的命令字符串。如果无法找到指定索引的命令字，输出 "ERROR"。
 * <p/>
 * 输入样例1：<br/>
 * 1 <br/>
 * password__a12345678_timeout_100
 * <p/>
 * 输出样例1：<br/>
 * password_******_timeout_100
 * <p/>
 * 输入样例2：<br/>
 * 2 <br/>
 * aaa_password_"a12_45678"_timeout__100_""_
 * <p/>
 * 输出样例2：<br/>
 * aaa_password_******_timeout_100_""
 * <p/>
 * 对于样例2，索引2 的命令字 "a12_45678"（包含在双引号内）被替换为 "******"，并删除了多余的下划线。注意最后的空命令字 """" 被保留。
 * <p>
 * <p/>
 * Copyright: Copyright (c) 2024-09-29 9:47
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SensitiveFieldEncryption {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 2; i++) {
			System.out.print("请输入索引k: ");
			int k = scanner.nextInt();
			System.out.print("请输入命令字符串: ");
			scanner.nextLine();
			String s = scanner.nextLine();

			String result = processCommandString(k, s);
			System.out.println(result);
		}
	}

	public static String processCommandString(int k, String s) {
		List<String> commandWords = new ArrayList<>();
		StringBuilder currentCommand = new StringBuilder();
		boolean inQuote = false;

		// 遍历字符串S，提取命令字
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '"') {
				inQuote = !inQuote;
			} else if (c == '_' && !inQuote) {
				// 处理下划线分隔符
				if (currentCommand.length() > 0) {
					commandWords.add(currentCommand.toString());
					currentCommand.setLength(0);
				}
			} else {
				currentCommand.append(c);
			}
		}

		if (currentCommand.length() > 0) {
			commandWords.add(currentCommand.toString());
		}

		// 检查索引K是否有效
		if (k < 0 || k >= commandWords.size()) {
			return "ERROR";
		}

		// 替换指定索引的命令字为"******"
		commandWords.set(k, "******");

		// 拼接命令字，使用下划线连接
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < commandWords.size(); i++) {
			if (i > 0) {
				sb.append("_");
			}
			sb.append(commandWords.get(i));
		}

		return sb.toString();
	}
}
