package com.loserico.bitop;

import com.loserico.codec.HexUtils;

/**
 * <p>
 * Copyright: (C), 2019/11/25 20:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Hex2BinaryTest {
	
	public static void main(String[] args) {
		String binStr = HexUtils.hex2Binary("0x7fffffff");
		System.out.println(binStr);
	}
}
