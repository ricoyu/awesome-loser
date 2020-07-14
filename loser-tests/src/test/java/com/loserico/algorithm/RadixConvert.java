package com.loserico.algorithm;

import java.util.Scanner;

public class RadixConvert {
	
	/**
	 * 进制转换
	 * 写出一个程序，接受一个十六进制的数，输出该数值的十进制表示。（多组同时输入 ）
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String input = sc.next();
			String ans = input.substring(2);
			System.out.println(Integer.parseInt(ans, 16));
		}
	}
	
}