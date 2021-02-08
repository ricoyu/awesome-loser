package com.loserico.codec;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2019/10/20 15:34
 * <p>
 * <p>
 * Company Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CRC16HashUitlsTest {
	
	@Test
	public void testCrc16Hash() {
		String key = "this s random key";
		int hash = HashUtils.crc16Hash(key);
		System.out.println(hash);
	}
}
