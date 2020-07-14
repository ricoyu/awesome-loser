package com.loserico.algorithm;//简单的四舍五入

import java.util.Scanner;

public class JinsiZhi {
    
    /**
     * 取近似值
     * 
     * 写出一个程序，接受一个正浮点数值，输出该数值的近似整数值。如果小数点后数值大于等于5,向上取整；小于5，则向下取整。
     * @param args
     */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			double a = sc.nextDouble();
			
			int b = (int) a;//向下取整
			if (a - b >= 0.5) {
				System.out.println(b + 1);
			} else {
				System.out.println(b);
			}
		}
	}
}