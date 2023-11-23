package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-16 15:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CountBits_338 {
	
	public int[] countBits(int n) {
		int[] bits = new int[n+1];
		for (int i = 1; i <= n; i++) {
			bits[i] = bits[i&(i-1)] +1;
		}
		return bits;
	}
}
