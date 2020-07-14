package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2019/10/25 17:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsTests {
	
	@Test
	public void testWarmUp() {
		try {
			Class.forName("com.loserico.cache.JedisUtils");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSet() {
		JedisUtils.set("k1", "aaa");
		Assert.assertEquals("aaa", JedisUtils.get("k1"));
		System.out.println(JedisUtils.get("k1"));
	}
	
	@Test
	public void testSubscribe() {
	}
	
}
