package com.loserico.codec;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

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
		System.out.println(RedixUtils.hex2Int("22"));
	}
	
	@Test
	public void testBinary2Int() {
		System.out.println(RedixUtils.binaryStr2Int("01101010"));
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
		RedixUtils.print((byte) -1);
		String binary = RedixUtils.int2BinaryStr(-1);
		System.out.println(binary);
	}
	
	@Test
	public void testInt2OneByte() {
		int i = 100;
		byte b = (byte) i;
		RedixUtils.print(b); //01100100
		int j = (int) b;
		System.out.println(j); //100
		
		System.out.println("---------------");
		int k = 300;
		System.out.println(RedixUtils.int2BinaryStr(k));//00000000000000000000000100101100
		byte b2 = (byte) k; //只取最后一个字节 00101100
		RedixUtils.print(b2); //00101100
		int l = (int) b2; //00101100 对应的int是44
		System.out.println(l); //44
	}
	
	@Test
	public void testPrintNagative() {
		byte b = (byte) -1;
		RedixUtils.print(b);
		int i = (int) b;
		System.out.println(i);
	}
	
	@Test
	public void testHexStr() {
		byte[] bytes = RedixUtils.int2Bytes(16);
		RedixUtils.print(bytes[bytes.length - 1]);
		String s = String.valueOf(bytes[bytes.length - 1]);
		String hex = Integer.toHexString(Integer.parseInt(s));
		System.out.println(hex);
	}
	
	@Test
	public void testChar() {
		char c = 'a';
		int digit = Character.digit(c, 16);
		System.out.println(digit);
	}
	
	@Test
	public void testHex2ByteArray() {
		String hexStr =
				"01 00 63 6f 6d 2e 6c 6f 73 65 72 69 63 6f 2e 62 6f 6f 74 2e 6e 65 74 74 79 2e 61 64 76 2e 76 6f2e 4d 79 4d 65 73 73 61 67 e5 01 00 01 01 63 6f6d 2e 6c 6f 73 65 72 69 63 6f 2e 62 6f 6f 74 2e6e 65 74 74 79 2e 61 64 76 2e 76 6f 2e 4d 79 4865 61 64 65 f2 01 01 02 6a 61 76 61 2e 75 74 696c 2e 48 61 73 68 4d 61 f0 01 01 fd fb 87 c1 0a00 00 00 05";
		byte[] bytes = RedixUtils.hex2Bytes(hexStr);
		String s = new String(bytes, UTF_8);
		RedixUtils.print(bytes);
		System.out.println(s);
		String hex2 = RedixUtils.bytes2Hex(bytes);
		assertEquals(hex2, hex2);
	}
	
	@Test
	public void testHex2ByteArr() {
		String hex = "6f6d";
		int len = hex.length();
		
		byte[] data = new byte[len / 2];
		
		for (int i = 0; i < len; i += 2) {
			char c1 = hex.charAt(i);
			int digit = Character.digit(c1, 16);
			System.out.println(digit+" 的二进制形式: "+RedixUtils.int2BinaryStr(digit));
			char c2 = hex.charAt(i + 1);
			int digit1 = Character.digit(c2, 16);
			System.out.println(digit1+" 的二进制形式: "+RedixUtils.int2BinaryStr(digit1));
			int tmp = digit << 4;
			System.out.println(digit +" 左移四位后: " + tmp +", 对应的二进制形式: " + RedixUtils.int2BinaryStr(tmp));
			int sum = tmp + digit1;
			System.out.println(sum);
			System.out.println(digit +" + "+digit1+" 对应的二进制形式: " + RedixUtils.int2BinaryStr(sum));
		}
	}
	
	@Test
	public void testCharBytes() {
		String s = IOUtils.readClassPathFileAsString("task.json");
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			//String value = String.valueOf(chars[i]);
			//byte[] bytes = value.getBytes();
			byte[] bytes = RedixUtils.char2Bytes(chars[i]);
			assertEquals(2, bytes.length);
		}
	}
	
	@Test
	public void testcharByteArray() {
		char c = '{';
		byte[] bytes = RedixUtils.char2Bytes(c);
		RedixUtils.print(bytes);
		System.out.println(Character.BYTES);
	}
	
	@Test
	public void testParseInt() {
		String s = "01010101111";
		int i = Integer.parseInt(s, 2);
		int j = RedixUtils.binaryStr2Int(s);
		System.out.println(i);
		assertTrue(i == j);
	}
	
	@Test
	public void testBytes2Int() {
		String bytes = "11011011";
		byte[] data = RedixUtils.binaryStr2Bytes(bytes);
		int i = (int)data[0];  //-37
		int j = (int)data[0]&0xff; //219
		System.out.println(i);
		System.out.println(j);
		
		int k = RedixUtils.bytes2Int(data);
		System.out.println(k);
		assertEquals(j, k);
		
		String s = "11111111";
		data = RedixUtils.binaryStr2Bytes(s);
		System.out.println((int)data[0]);
		System.out.println((int)(data[0]&0xff));
	}
	
	@Test
	public void testFloat2Bytes() {
		float f = 1.02f;
		byte[] bytes = RedixUtils.float2Bytes(f);
		RedixUtils.print(bytes);
		System.out.println(RedixUtils.bytes2Float(bytes));
	}
}
