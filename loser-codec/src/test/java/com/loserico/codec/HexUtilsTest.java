package com.loserico.codec;

import org.junit.Test;

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
}
