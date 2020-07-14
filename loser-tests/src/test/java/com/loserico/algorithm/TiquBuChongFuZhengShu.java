package com.loserico.algorithm;

import java.util.Scanner;

public class TiquBuChongFuZhengShu {
	
	/**
	 * 提取不重复的整数
	 * 输入一个int型整数，按照从右向左的阅读顺序，返回一个不含重复数字的新的整数。
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String string = scanner.nextLine();
		int[] a = new int[10];
		for (int i = string.length() - 1; i >= 0; i--) {
			if (a[string.charAt(i) - '1'] == 0) {
				System.out.print(string.charAt(i));
				a[string.charAt(i) - '1'] = 1;
			}
		}
	}
}