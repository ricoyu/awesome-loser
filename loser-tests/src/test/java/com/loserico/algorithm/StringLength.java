package com.loserico.algorithm;

import java.util.Scanner;

/**
 * <p>
 * Copyright: (C), 2020/4/22 20:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StringLength {
	
	/**
	 * 字符串最后一个单词的长度
	 * <p>
	 * 计算字符串最后一个单词的长度，单词以空格隔开。
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String line;
		while (scan.hasNextLine()) {
			line = scan.nextLine().trim();
			String[] array = line.split(" ");
			int lastNumber = array[array.length - 1].length();
			System.out.println(lastNumber);
		}
	}

}
