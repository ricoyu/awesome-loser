package com.loserico.algorithm;

import java.util.*;

public class ZiFuChuanLianJieZuiChangLuJingChaZhao {
	
	/**
	 * 字串的连接最长路径查找
	 * 给定n个字符串，请对n个字符串按照字典序排列。
	 * <p>
	 * 输入描述:
	 * 输入第一行为一个正整数n(1≤n≤1000),下面n行为n个字符串(字符串长度≤100),字符串中只含有大小写字母。
	 * 输出描述:
	 * 数据输出n行，输出结果为按照字典序排列的字符串。
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		String[] st = new String[n];
		for (int i = 0; i < n; i++) {
			st[i] = sc.next();
		}
		Arrays.sort(st);
		for (int i = 0; i < n; i++) {
			System.out.println(st[i]);
		}
	}
}
