package com.loserico.algorithm;

/**
 * <p>
 * Copyright: (C), 2023-05-29 上午8:48
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
public class Int2Binary {
	
	public static void main(String[] args) {
		print(-4);
		print(~4+1);
		System.out.println(~-4 +1);
		
	}
	
	public static void print(int num) {
		for (int i = 31; i >= 0; i--) {
			System.out.print((num & (1 << i)) == 0 ? "0" : "1");
		}
		System.out.println();
	}
	
	public static int nagitive(int num) {
		int i = (~num + 1);
		System.out.println(i);
		return i;
	}
}
