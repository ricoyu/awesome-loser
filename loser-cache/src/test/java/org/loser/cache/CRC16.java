package org.loser.cache;

import org.junit.Test;
import redis.clients.util.JedisClusterCRC16;

/**
 * <p>
 * Copyright: (C), 2019/10/20 10:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @version 1.0
 * @author: Rico Yu ricoyu520@gmail.com
 */
public class CRC16 {
	
	@Test
	public void testCrc16() {
		String name = "name1";
		System.out.println(JedisClusterCRC16.getCRC16(name) & (16384-1));
		System.out.println(JedisClusterCRC16.getCRC16(name) % 16384);
	}
}
