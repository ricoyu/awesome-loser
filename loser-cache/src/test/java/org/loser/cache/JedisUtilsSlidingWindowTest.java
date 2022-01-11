package org.loser.cache;

import com.loserico.cache.JedisUtils;
import com.loserico.common.lang.utils.StringUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Copyright: (C), 2022-01-09 14:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsSlidingWindowTest {
	
	@SneakyThrows
	@Test
	public void testSlidingWindow() {
		long windowSize = 1000; // 毫秒
		long limitCount = 1; //windowSize毫秒内可以放行多少个请求
		
		for (int i = 0; i < 10; i++) {
			String member = StringUtils.uniqueKey(12);
			boolean isPassed =
					JedisUtils.AFFLUENT.slidingWindows("purchase",
							member, 
							System.currentTimeMillis(),
							windowSize,
							limitCount);
			System.out.println(member +" : "+isPassed);
			/*if (i == 5) {
				assertFalse(isPassed);
			} else {
				assertTrue(isPassed);
			}*/
		}
		Thread.sleep(500L);
		String member = StringUtils.uniqueKey(12);
		boolean isPassed =
				JedisUtils.AFFLUENT.slidingWindows("purchase",
						member, 
						System.currentTimeMillis(),
						windowSize,
						limitCount);
		System.out.println(member +" : "+isPassed);
		assertFalse(isPassed);
		
		Thread.sleep(500L);
		member = StringUtils.uniqueKey(12);
		isPassed =
				JedisUtils.AFFLUENT.slidingWindows("purchase",
						member, 
						System.currentTimeMillis(),
						windowSize,
						limitCount);
		System.out.println(member +" : "+isPassed);
		assertTrue(isPassed);
	}
}
