package com.loserico.algorithm;//输入数据后转换成二进制数，统计二进制数中1的个数，输出即可。

import java.util.Scanner;

public class IntNeiCun1GeShu {
	
	/**
	 * 求int型正整数在内存中存储时1的个数
	 * <p>
	 * 题目描述
	 * 输入一个int型的正整数，计算出该int型数据在内存中存储时1的个数。
	 * <p>
	 * 输入描述:
	 * 输入一个整数（int类型）
	 * <p>
	 * 输出描述:
	 * 这个数转换成2进制后，输出1的个数
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			int n = sc.nextInt();
			String ans = Integer.toBinaryString(n);
			int sum = 0;
			for (int i = 0; i < ans.length(); i++) {
				if (ans.charAt(i) == 49) {
					sum++;
				}
			}
			System.out.println(sum);
		}
	}
	
}