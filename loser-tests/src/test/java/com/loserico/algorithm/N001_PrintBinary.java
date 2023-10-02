package com.loserico.algorithm;

import com.loserico.common.lang.utils.PrimitiveUtils;

/**
 * 打印整数的二进制形式
 * <p>
 * Copyright: (C), 2023-09-07 8:27
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class N001_PrintBinary {
	
	public static void main(String[] args) {
		int i = 12312313;
		int j = 81278637;
		print(i);
		print(j);
		System.out.println("==================");
		PrimitiveUtils.printBinary(i | j);
	}
	
	public static void print(int num) {
		for (int i = 31; i >= 0; i--) {
			/*
			 * 一个整数占4字节, 32位
			 * 1左移31位就是把1从第1位上的移到了32位上, 变成 10000000 00000000 00000000 00000000
			 * 所以一个正数(符号位0)与其位与, 得到的二进制形式就是
			 * 00000000 00000000 00000000 00000000, 即整数0, 此时输出符号位"0"
			 * 负数的话得到
			 * 10000000 00000000 00000000 00000000, 即Integer.MIN_VALUE, 不等于0, 此时输出符号位 "1"
			 * 
			 * 接下来1左移30位, 那么二进制形式为 01000000 00000000 00000000 00000000
			 * 任意数与其位与, 如果这个数的31位上是0, 位与得到的结果就是00000000 00000000 00000000 00000000, 即整数0, 此时输出31位 "0"
			 * 如果这个数的31为上是1, 位与得到的结果就是01000000 00000000 00000000 00000000, 不等于0, 所以此时输出31位二进制 "1"
			 * 
			 * 依次类推
			 */
			System.out.print((num & (1 << i)) == 0 ? "0" : "1");
		}
		System.out.println();
	}
}
