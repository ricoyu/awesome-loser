package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2023-02-09 11:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsSetTest {

	@Test
	public void testSetAdd() {
		long addedCount = JedisUtils.SET.sadd("msg-ids", "101", "102", "103");
		assertTrue(addedCount == 3);
		addedCount = JedisUtils.SET.sadd("msg-ids", "101", "102", "103");
		assertTrue(addedCount == 0);
	}
}
