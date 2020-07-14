package com.loserico.algorithm;

import java.util.Scanner;

public class ShuZiDianDao {
	
	/**
	 * 数字颠倒
	 * <p>
	 * 输入一个整数，将这个整数以字符串的形式逆序输出
	 * 程序不考虑负数的情况，若数字含有0，则逆序形式也含有0，如输入为100，则输出为001
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int a = input.nextInt();
		String ss = String.valueOf(a);
		System.out.println(getReverse(ss));
	}
	
	public static String getReverse(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		return sb.reverse().toString();
	}
}