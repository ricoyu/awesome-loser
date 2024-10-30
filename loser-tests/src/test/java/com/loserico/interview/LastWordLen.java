package com.loserico.interview;

import java.util.Scanner;

/**
 * 计算字符串最后一个单词的长度，单词以空格隔开，字符串长度小于5000。（注：字符串末尾不以空格为结尾）
 * <p>
 * Copyright: Copyright (c) 2024-09-05 11:26
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LastWordLen {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String str = scanner.nextLine();
			if (str.length() > 5000) {
				System.out.println("字符串长度不能超过5000");
				System.exit(1);
			}
			String[] strs = str.split(" ");
			if (strs.length==0) {
				System.out.println(-1);
			}
			System.out.println(strs[strs.length - 1].length());
		}
	}
}
