package com.loserico.common.lang;

import com.loserico.common.lang.utils.LogbackUtils;
import org.junit.Test;
import org.slf4j.Logger;

import java.nio.charset.Charset;

import static com.loserico.common.lang.utils.LogbackUtils.Protocol.UDP;
import static java.util.Arrays.asList;

/**
 * <p>
 * Copyright: (C), 2021-09-30 11:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LogbackUtilsTest {
	
	@Test
	public void testSendTcpSyslog() {
		Logger logger = LogbackUtils.addRsyslog(LogbackUtilsTest.class, asList("10.10.26.14:31514"), UDP, Charset.forName("UTF-8"));
		logger.info("hello");
	}
}
