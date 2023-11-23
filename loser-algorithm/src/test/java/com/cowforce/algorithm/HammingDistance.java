package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-16 16:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HammingDistance {
	public int hammingDistance(int x, int y) {
		int distance = 0;
		/**
		 * xor &= (xor - 1)用以清除最低位的1
		 * 比如，假设xor = 21，二进制表示为  0001 0101,
		 * 则 21&20 = 0001 0101 & 0001 0100 = 0001 0100 = 20
		 * 20&19 = 0001 0100 & 0001 0011 = 0001 0000= 16
		 * 16&15 = 0001 0000 & 0000 1111 = 0
		 */
		for (int xor = x ^ y; xor != 0; xor = (xor & (xor - 1))) {
			distance++;
		}
		return distance;
	}
}
