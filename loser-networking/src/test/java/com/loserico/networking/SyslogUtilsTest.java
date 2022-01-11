package com.loserico.networking;

import com.loserico.networking.constants.Proto;
import com.loserico.networking.utils.SyslogUtils;
import org.graylog2.syslog4j.SyslogIF;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-12-15 16:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SyslogUtilsTest {
	
	@Test
	public void test() {
		SyslogIF logger = SyslogUtils.of("10.10.17.22", 11514)
				.proto(Proto.UDP)
				.charset("UTF-8")
				.maxMessageLength(50000)
				.build();
		
		logger.info("hello");
		System.out.println("..............");
	}
}
