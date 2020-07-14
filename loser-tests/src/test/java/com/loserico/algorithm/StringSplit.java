package com.loserico.algorithm;

import java.util.Scanner;

public class StringSplit {
	
	/**
	 * 字符串分隔
	 * <p>
	 * •连续输入字符串，请按长度为8拆分每个字符串后输出到新的字符串数组；
	 * •长度不是8整数倍的字符串请在后面补数字0，空字符串不处理。
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (sc.hasNext()) {
			String s1 = sc.nextLine();
			String s2 = sc.nextLine();
			
			//第一行
			int flag1 = 0;
			for (int i = 0; i < s1.length(); i++) {
				if (flag1 != 7) {
					System.out.print(s1.charAt(i));
					flag1++;
				} else {//flag1==7  输出的字符已经有8个
					System.out.println(s1.charAt(i));
					flag1 = 0;//更新flag1
				}
			}
			
			//跳出循环后，如果flag1==0那么说明原来字符的个数是8的倍数
			//跳出循环后，如果flag1！=0那么说明需要补0
			if (flag1 != 0) {
				for (int i = 0; i < 8 - flag1; i++) {
					System.out.print("0");
				}
				System.out.println();
			}
			
			
			//第二行
			int flag2 = 0;
			for (int i = 0; i < s2.length(); i++) {
				if (flag2 != 7) {
					System.out.print(s2.charAt(i));
					flag2++;
				} else {
					System.out.println(s2.charAt(i));
					flag2 = 0;
				}
			}
			
			if (flag2 != 0) {
				for (int i = 0; i < 8 - flag2; i++) {
					System.out.print("0");
				}
				System.out.println();
			}
			
			
		}
		
	}
	
}