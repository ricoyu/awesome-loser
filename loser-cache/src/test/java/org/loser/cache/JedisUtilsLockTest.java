package org.loser.cache;

import com.loserico.cache.JedisUtils;
import com.loserico.cache.concurrent.Lock;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2024-01-25 11:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsLockTest {
	
	@Test
	public void testNonBlockingLock() {
		Lock lock = JedisUtils.nonBlockingLock("rcs:tcp:persist:lock");
		lock.lock();
		if (lock.locked()) {
			System.out.println("获取锁成功");
		} else {
			System.out.println("获取锁失败");
		}
	}
}
