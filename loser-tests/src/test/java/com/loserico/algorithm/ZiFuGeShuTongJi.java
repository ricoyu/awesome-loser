package com.loserico.algorithm;

import java.util.ArrayList;
import java.util.Scanner;

public class ZiFuGeShuTongJi {
    
    /**
     * 字符个数统计
     * 
     * 编写一个函数，计算字符串中含有的不同字符的个数。字符在ACSII码范围内(0~127)，换行表示结束符，不算在字符里。不在范围内的不作统计。
     * @param args
     */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String s = sc.nextLine();
			ArrayList<String> alist = new ArrayList<String>();
			
			for (int i = s.length() - 1; i >= 0; i--) {
				if (!alist.contains(s.charAt(i) + "")) {
					alist.add(s.charAt(i) + "");
				}
			}
			
			System.out.println(alist.size());
		}
	}
}