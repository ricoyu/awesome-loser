package com.loserico.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class JianDanCuoWuJiLu {
	
	/**
     * 简单错误记录
     * 
     * 题目描述
     * 开发一个简单错误记录功能小模块，能够记录出错的代码所在的文件名称和行号。
     *
     * 处理：
     * 1、 记录最多8条错误记录，循环记录（或者说最后只输出最后出现的八条错误记录），对相同的错误记录（净文件名称和行号完全匹配）只记录一条，错误计数增加；
     * 2、 超过16个字符的文件名称，只记录文件的最后有效16个字符；
     * 3、 输入的文件可能带路径，记录文件名称不能带路径。
     *
     * 输入描述:
     * 一行或多行字符串。每行包括带路径文件名称，行号，以空格隔开。
     *
     * 输出描述:
     * 将所有的记录统计并将结果输出，格式：文件名 代码行数 数目，一个空格隔开，如：
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		Map<String, Integer> map = new LinkedHashMap<>(16);
		
		String message;
		// 键盘输入信息
		while ((message = bufferedReader.readLine()) != null) {
			String[] messages = message.split(" ");
			String errorPath = messages[0];
			String line = messages[1];
			
			// 文件名
			errorPath = errorPath.substring(errorPath.lastIndexOf("\\") + 1);
			// 文件名大于 16 时，截取。
			if (errorPath.length() > 16) {
				errorPath = errorPath.substring(errorPath.length() - 16);
			}
			
			// 存储数据到 map 中
			String key = errorPath + " " + line;
			map.put(key, map.getOrDefault(key, 0) + 1);
		}
		
		// 打印最后 8 条数据
		int count = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			count++;
			if (count > (map.size() - 8)) {
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		}
	}
}


