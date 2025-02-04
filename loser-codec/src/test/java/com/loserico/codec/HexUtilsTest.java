package com.loserico.codec;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static java.util.Arrays.asList;

/**
 * <p>
 * Copyright: (C), 2021-02-01 17:56
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HexUtilsTest {
	
	
	@Test
	public void test() {
		System.out.println(HexUtils.hex2Binary("0015"));
	}
	
	@Test
	public void testHexToBinary() {
		String ipv6 = "2001:0db8:3c4d:0015";
		asList(ipv6.split(":"))
				.forEach((hex) -> {
					String binaryString = HexUtils.hex2Binary(hex);
					System.out.println(binaryString);
				});
	}
	
	@Test
	public void testBinary2Ten() {
		System.out.println(HexUtils.hexToInteger("11111111"));
		System.out.println(HexUtils.hexToInteger("1111"));
	}

	@Test
	public void hex2Binary_NullInput_ReturnsNull() {
		assertNull(HexUtils.hex2Binary(null));
	}

	@Test
	public void hex2Binary_EmptyString_ReturnsNull() {
		assertNull(HexUtils.hex2Binary(""));
		when(HexUtils.hex2Binary("   "));
	}

	@Test
	public void hex2Binary_WhitespaceString_ReturnsNull() {
		assertNull(HexUtils.hex2Binary("   "));
	}

	@Test
	public void hex2Binary_ValidHexWithPrefix_ReturnsBinaryString() {
		assertEquals("10101", HexUtils.hex2Binary("0x15"));
	}

	@Test
	public void hex2Binary_ValidHexWithoutPrefix_ReturnsBinaryString() {
		assertEquals("10101", HexUtils.hex2Binary("15"));
	}

	@Test
	public void hex2Binary_ValidHexWithUppercase_ReturnsBinaryString() {
		assertEquals("10101", HexUtils.hex2Binary("0X15"));
	}

	@Test
	public void hex2Binary_ValidHexWithLowercase_ReturnsBinaryString() {
		assertEquals("10101", HexUtils.hex2Binary("0x15"));
	}

	@Test
	public void hex2Binary_InvalidHex_ThrowsNumberFormatException() {
		assertThrows(NumberFormatException.class, () -> {
			HexUtils.hex2Binary("1G");
		});
	}
}
