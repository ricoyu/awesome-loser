package com.loserico.algorithm;

import java.util.Scanner;

public class CharCount {
	
	/**
	 * 计算字符个数
	 * 写出一个程序，接受一个由字母和数字组成的字符串，和一个字符，然后输出输入字符串中含有该字符的个数。不区分大小写。
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String str1 = in.nextLine();
		int n = 0;
		String str2 = in.nextLine();
		
		String s1 = str1.toLowerCase();
		String s2 = str2.toLowerCase();
		for (int i = 0; i < s1.length(); i++) {
			if (s2.equals(s1.substring(i, i + 1))) {
				n = n + 1;
			}
		}
		
		System.out.println(n);
		
	}
}
