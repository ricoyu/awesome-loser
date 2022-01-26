package com.loserico.common.lang;

import com.loserico.common.lang.utils.SnowflakeId;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2022-01-21 16:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SnowflakeIdTest {
	
	@Test
	public void test() {
		SnowflakeId idWorker = new SnowflakeId(0, 31);
		for (int i = 0; i < 1000; i++) {
			long id = idWorker.nextId();
			System.out.println(id);
		}
	}
}
