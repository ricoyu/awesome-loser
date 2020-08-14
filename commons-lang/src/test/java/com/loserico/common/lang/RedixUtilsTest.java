package com.loserico.common.lang;

import com.loserico.common.lang.utils.RedixUtils;
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
		System.out.println(RedixUtils.int2Hex(549));
	}
	
	@Test
	public void testHex2Int() {
		System.out.println(RedixUtils.hex2Int("180"));
	}
}
