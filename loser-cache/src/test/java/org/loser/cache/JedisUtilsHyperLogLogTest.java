package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2022-07-16 9:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsHyperLogLogTest {
	
	@Test
	public void test() {
		JedisUtils.del("www.sexyuncle.com:{uv}");
		JedisUtils.del("www.sexyuncle.com:{uv}");
		JedisUtils.del("www.loserico.com:{uv}");
		JedisUtils.del("total:{uv}");
		long result = JedisUtils.HyperLogLog.pfadd("www.sexyuncle.com:{uv}", "101", "102", "103");
		assertEquals(1L, result);
		System.out.println("result: "+result);
		System.out.println(JedisUtils.HyperLogLog.pfcount("www.sexyuncle.com:{uv}"));
		result = JedisUtils.HyperLogLog.pfadd("www.loserico.com:{uv}", "103", "104", "105");
		assertEquals(1, result);
		System.out.println(result);
		long count = JedisUtils.HyperLogLog.pfcount("www.loserico.com:{uv}");
		assertEquals(count, 3L);
		System.out.println(count);
		String mergeResult = JedisUtils.HyperLogLog.pfmerge("total:{uv}", "www.sexyuncle.com:{uv}", "www.loserico.com:{uv}");
		System.out.println(mergeResult);
		Long pfcount = JedisUtils.HyperLogLog.pfcount("total:{uv}");
		System.out.println("total:" + pfcount);
	}
}
