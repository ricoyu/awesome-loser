package com.loserico.codec;

import org.junit.Test;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2023-11-22 17:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StringEncodeTest {
	
	@Test
	public void testStrToBytes() {
		String s = "123,001,10,3,2,{MOV020P120W12},03";
		byte[] bytes = s.getBytes(US_ASCII);//US-ASCII
		byte[] bytes1 = s.getBytes(UTF_8);
		RedixUtils.print(bytes);
		RedixUtils.print(bytes1);
		String s1 = RedixUtils.bytes2BinaryStr(bytes);
		String s2 = RedixUtils.bytes2BinaryStr(bytes1);
		System.out.println(s1.equals(s2));
		String original = new String(bytes, US_ASCII);
		String original2 = new String(bytes1, US_ASCII);
		assertEquals(s, original);
		assertEquals(s, original2);
		
		String bits = "001100010011001000110011001011000011000000110000001100010010110000110001001100000010110000110011001011000011001000101100011110110100110101001111010101100011000000110010001100000101000000110001001100100011000001010111001100010011001001111101001011000011000000110011";
		int byteCount = bits.toCharArray().length / 8;
		System.out.println(byteCount);
	}
}
