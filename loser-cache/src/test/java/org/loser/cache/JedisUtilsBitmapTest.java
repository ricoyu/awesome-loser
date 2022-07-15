package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;

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
		boolean result = JedisUtils.Bitmap.setbit("login_20220714", 101, 99);
		System.out.println(result);
		result = JedisUtils.Bitmap.setbit("login_20220714", 101, 99);
		System.out.println(result);
		result = JedisUtils.Bitmap.setbit("login_20220714", 100, 99);
		System.out.println(result);
	}
}
