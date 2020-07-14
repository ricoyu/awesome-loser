package com.loserico.common.lang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2019/11/7 9:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class DateUtilsTest {
	
	@Test
	public void testStartUpTimes() throws ClassNotFoundException {
		long begin = System.nanoTime();
		//Class.forName("com.loserico.common.lang.utils.DateUtils");
		long end = System.nanoTime();
		System.out.println("初始化DateUtils需要: " + (end - begin) / 1000000 + "毫秒");
		log.info("初始化DateUtils需要: " + (end - begin) / 1000000 + "毫秒");
	}
}
