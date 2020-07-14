package org.loser.cache;

import com.loserico.cache.JedisUtils;
import com.loserico.cache.concurrent.Lock;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020/4/1 9:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisLockTest {
	
	@Test
	public void testBlockingLock() {
		Lock lock = JedisUtils.blockingLock("lock001");
		lock.lock();
		System.out.println("...do some work");
		lock.unlock();
	}
}
