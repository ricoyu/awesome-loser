package com.loserico.jvm;

/**
 * 运行此代码, cpu会飙高
 * <p>
 * Copyright: (C), 2020-8-2 0002 10:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Math {
	
	public static final int initData = 666;
	
	public static void main(String[] args) {
		Math math = new Math();
		while (true) {
			math.compute();
		}
	}
	
	public int compute() {
		int a = 1;
		int b = 2;
		int c = (a + b) * 10;
		return c;
	}
	
}
