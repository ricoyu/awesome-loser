package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-06-23 14:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisCasTest {
	
	@Test
	public void testCas() {
		JedisUtils.del("caskey");
		boolean result = JedisUtils.casNumber("caskey", 10L, -1); // key不存在的情况
		assertTrue(result);
		result = JedisUtils.casNumber("caskey", 11L, -1); // 小于原始值才set, 但传的新值大于原始值, 所以应该set不成功
		assertFalse(result);
		long value = JedisUtils.get("caskey", Integer.class);
		assertThat(value).isEqualTo(10);
		
		result = JedisUtils.casNumber("caskey", 9L, -1); // 小于原始值才set, set成功
		assertTrue(result);
		
		result = JedisUtils.casNumber("caskey", 11L, 1); // 大于原始值才set, 应该set成功
		assertTrue(result);
		value = JedisUtils.get("caskey", Long.class);
		assertThat(value).isEqualTo(11);
	}
}
