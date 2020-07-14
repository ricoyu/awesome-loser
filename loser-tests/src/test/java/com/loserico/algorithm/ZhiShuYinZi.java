package com.loserico.algorithm;

import java.util.Scanner;

public class ZhiShuYinZi {
    
    /**
     * 质数因子
     * 
     * 功能:输入一个正整数，按照从小到大的顺序输出它的所有质因子（如180的质因子为2 2 3 3 5 ）
     * 最后一个数后面也要有空格
     *
     * @param args
     */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		while (scanner.hasNext()) {
			long input = scanner.nextLong();
			System.out.println(findPrimes(input));
		}
		scanner.close();
	}
	
	
	private static String findPrimes(long num) {
		
		StringBuilder builder = new StringBuilder(128);
		long i = 2;
		while (i <= num) {
			// 每次的i一定是质数时才会满足
			// 因为如果是一个合数，那那它一定是由更小的质数相乘得来的，
			// 而在i前的质数已经全部被使用过了，不能再整除num了
			while (num % i == 0) {
				builder.append(i).append(' ');
				num /= i;
			}
			i++;
		}
		
		return builder.toString();
	}
	
}