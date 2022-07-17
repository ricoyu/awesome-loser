package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;

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
public class JedisUtilsHYperLogLogTest {
	
	@Test
	public void test() {
		Long result = JedisUtils.HyperLogLog.pfadd("www.sexyuncle.com:{uv}", "101", "102", "103");
		System.out.println("result: "+result);
		System.out.println(JedisUtils.HyperLogLog.pfcount("www.sexyuncle.com:{uv}"));
		result = JedisUtils.HyperLogLog.pfadd("www.loserico.com:{uv}", "103", "104", "105");
		System.out.println(result);
		System.out.println(JedisUtils.HyperLogLog.pfcount("www.loserico.com:{uv}"));
		String mergeResult = JedisUtils.HyperLogLog.pfmerge("total:{uv}", "www.sexyuncle.com:{uv}", "www.loserico.com:{uv}");
		System.out.println(mergeResult);
		Long pfcount = JedisUtils.HyperLogLog.pfcount("total:{uv}");
		System.out.println("total:" + pfcount);
	}
}
