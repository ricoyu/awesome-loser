package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Copyright: (C), 2022-01-26 14:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsHashTest {
	
	@Test
	public void testDelete() throws InterruptedException {
		JedisUtils.HASH.hset("idempotent-token", "token001", "somevalue", 6);
		String token = JedisUtils.HASH.hget("idempotent-token", "token001");
		assertEquals("somevalue", token);
		
		TimeUnit.SECONDS.sleep(5);

		Long deletedCount = JedisUtils.HASH.hdel("idempotent-token", "token001");
		System.out.println(deletedCount);
		assertTrue(deletedCount == 0);
	}
}
