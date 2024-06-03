package com.loserico.codec;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.common.lang.utils.StringUtils;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
		System.out.println(RedixUtils.hex2Int("00 7b"));
	}
	
	@Test
	public void testBinary2Int() {
		System.out.println(RedixUtils.binaryStr2Int("01101010"));
	}
	
	@Test
	public void testHexToBinary() {
		System.out.println(RedixUtils.hex2BinaryStr("0x0F"));
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
		byte[] bytes = RedixUtils.binaryStr2Bytes(RedixUtils.int2BinaryStr(k));
		RedixUtils.print(bytes);
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
			System.out.println(digit + " 的二进制形式: " + RedixUtils.int2BinaryStr(digit));
			char c2 = hex.charAt(i + 1);
			int digit1 = Character.digit(c2, 16);
			System.out.println(digit1 + " 的二进制形式: " + RedixUtils.int2BinaryStr(digit1));
			int tmp = digit << 4;
			System.out.println(digit + " 左移四位后: " + tmp + ", 对应的二进制形式: " + RedixUtils.int2BinaryStr(tmp));
			int sum = tmp + digit1;
			System.out.println(sum);
			System.out.println(digit + " + " + digit1 + " 对应的二进制形式: " + RedixUtils.int2BinaryStr(sum));
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
		int i = (int) data[0];  //-37
		int j = (int) data[0] & 0xff; //219
		System.out.println(i);
		System.out.println(j);
		
		int k = RedixUtils.bytes2Int(data);
		System.out.println(k);
		assertEquals(j, k);
		
		String s = "11111111";
		data = RedixUtils.binaryStr2Bytes(s);
		System.out.println((int) data[0]);
		System.out.println((int) (data[0] & 0xff));
	}
	
	@Test
	public void testFloat2Bytes() {
		float f = 1.02f;
		byte[] bytes = RedixUtils.float2Bytes(f);
		RedixUtils.print(bytes);
		System.out.println(RedixUtils.bytes2Float(bytes));
	}
	
	@Test
	public void testFloat2Bytes2() {
		float f = 1.112f;
		byte[] bytes = RedixUtils.float2Bytes(f);
		RedixUtils.print(bytes);
	}
	
	@Test
	public void testCharInt() {
		int i = 123;
		byte[] bytes = RedixUtils.int2Bytes(i);
		String s = new String(bytes);
		System.out.println(s);
	}
	
	@Test
	public void testInt2Byte() {
		byte[] bytes = RedixUtils.int2Bytes(123);
		RedixUtils.print(bytes);
	}
	
	@Test
	public void testStr2asciiBytes() {
		String s = "<STX>";
		byte[] bytes = s.getBytes(US_ASCII);
		RedixUtils.print(bytes);
	}
	
	@Test
	public void testbyteStr2int() {
		int i = RedixUtils.binaryStr2Int("01111011");
		System.out.println(i);
	}
	
	@Test
	public void test3bytesInt() {
		int i = RedixUtils.binaryStr2Int("11111111 11111111 11111111");
		System.out.println(i);
	}
	
	@Test
	public void testHex2AscII() {
		String hex = "01 00 63 6f 6d 2e 6c 6f 73 65 72 69 63 6f 2e 62";
		String ascii = RedixUtils.hex2Ascii(hex);
		System.out.println(ascii);
		
		System.out.println(RedixUtils.hex2Ascii("0x03"));
	}
	
	@Test
	public void testAscii2bytes() {
		char a = 'a';
		byte[] bytes = RedixUtils.char2Bytes(a);
		String s = RedixUtils.bytes2Hex(bytes);
		System.out.println(s);
		String ascii = RedixUtils.hex2Ascii(s);
		System.out.println(ascii);
	}
	
	@Test
	public void testHex2Bytes() {
		String hex = "57 65 6c 63 6f 6d 65 20 74 6f 20 4e 65 74 74 7921";
		byte[] bytes = RedixUtils.hex2Bytes(hex);
		RedixUtils.print(bytes);
		String hex2 = RedixUtils.binaryStr2Hex(RedixUtils.hex2BinaryStr(hex));
		System.out.println(hex);
		hex = StringUtils.trimAll(hex);
		assertEquals(hex, hex2);
	}
	
	@Test
	public void testPrintHex2BinaryStr() {
		String binaryStr = RedixUtils.hex2BinaryStr("0xCAFEBABE");
		System.out.println(binaryStr);
	}

	@Test
	public void testHex2Asciii() {
		System.out.println(RedixUtils.ascii2Hex("2"));
	}

	@Test
	public void testhex2Asci() {
		String ascii = RedixUtils.hex2Ascii("37");
		System.out.println(ascii);
	}
	@Test
	public void testascii2Bytes() {
		byte[] bytes = RedixUtils.ascii2Bytes("$02");
		RedixUtils.print(bytes);
	}

	@Test
	public void testHex2Ascii() {
		String hex = "0231323b4d433032333b313b54414d523b6576743d4d53473b6f69643d463b7174793d313b6f72643d324d4f563330443035304330353b03";
		System.out.println(RedixUtils.hex2Ascii(hex));;
	}

	@Test
	public void testAscii2Hex() {
		String s = "M";
		System.out.println(RedixUtils.ascii2Hex(s));
	}

	@Test
	public void testHex2Bits() {
		String hex = RedixUtils.ascii2Hex("1");
		String binary = RedixUtils.hex2BinaryStr(hex);
		System.out.println(hex);
		System.out.println(binary);
	}

	@Test
	public void testHex2Float() {
		float floatValue = RedixUtils.hex2Float("00001111", 2);
		assertEquals(floatValue, -12.9, 0.01);
	}

	@Test
	public void testFloat2Hex() {
		String hex = RedixUtils.float2Hex(-12.9f);
		assertEquals(hex, "C14E6666".toLowerCase());
	}

	@Test
	public void test() {
		String s = "001";
		String s1 = RedixUtils.ascii2Hex(s); //303031
		System.out.println(s1); //303031
		System.out.println(RedixUtils.hex2Ascii(s1)); //001
	}

	@Test
	public void testDecimal2Hex() {
		String hex = RedixUtils.int2Hex(6);
		System.out.println(hex);

		System.out.println(RedixUtils.hex2Int("06"));
	}

	@Test
	public void testHex2Binary() {
		String binaryStr = RedixUtils.hex2BinaryStr("100D");
		binaryStr.substring(binaryStr.length()-1);
	}
}
