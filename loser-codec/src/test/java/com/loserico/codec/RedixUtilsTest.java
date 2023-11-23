package com.loserico.codec;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-8-2 0002 11:07
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RedixUtilsTest {
	
	@Test
	public void testInt2Hex() {
		System.out.println(RedixUtils.int2Hex(14));
	}
	
	@Test
	public void testHex2Int() {
		System.out.println(RedixUtils.hex2Int("0x00000E"));
	}
	
	@Test
	public void testBinary2Int() {
		System.out.println(RedixUtils.binary2Int("01101010"));
	}
	
	@Test
	public void testHexToBinary() {
		System.out.println(RedixUtils.hex2BinaryStr("0x00000C"));
	}
	
	@Test
	public void testInt2Bonary() {
		System.out.println(RedixUtils.int2BinaryStr(36));
	}
	
	@Test
	public void testNativeInt() {
		RedixUtils.print((byte)-1);
		String binary = RedixUtils.int2BinaryStr(-1);
		System.out.println(binary);
	}
	
	@Test
	public void testInt2OneByte() {
		int i = 100;
		byte b = (byte)i;
		RedixUtils.print(b); //01100100
		int j = (int)b;
		System.out.println(j); //100
		
		System.out.println("---------------");
		int k = 300;
		System.out.println(RedixUtils.int2BinaryStr(k));//00000000000000000000000100101100
		byte b2 = (byte)k; //只取最后一个字节 00101100
		RedixUtils.print(b2); //00101100
		int l = (int)b2; //00101100 对应的int是44
		System.out.println(l); //44
	}
	
	@Test
	public void testPrintNagative() {
		byte b = (byte) -1;
		RedixUtils.print(b);
		int i = (int)b;
		System.out.println(i);
	}
	
}
