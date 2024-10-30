package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2022-07-14 15:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsBitmapTest {
	
	@Test
	public void testSetbit() {
		long count = JedisUtils.Bitmap.bitCount("login_20220714");
		System.out.println(count);
		JedisUtils.del("login_20220714");
		boolean result = JedisUtils.Bitmap.setbit("login_20220714", 101, 10);
		System.out.println(result);
		result = JedisUtils.Bitmap.setbit("login_20220714", 101, 8);
		System.out.println(result);
	}
	
	@Test
	public void testBitPosition() {
		Boolean k1 = JedisUtils.Bitmap.getbit("k1", 8);
		assertTrue(k1);
		long bitCount = JedisUtils.Bitmap.bitCount("k1", 1, 1);
		assertEquals(1, bitCount);
	}
}
