package com.loserico.log;

import com.loserico.common.lang.utils.LogbackUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;

import static java.util.Arrays.asList;

/**
 * <p>
 * Copyright: (C), 2020-12-22 9:58
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class RsyslogTest {
	
	@Test
	public void testRsyslog() {
		Logger logger = LogbackUtils.addRsyslog(RsyslogTest.class, asList("172.16.0.66:514"));
		logger.info(">>>>>>>>>>>>>>>>>>");
		log.info("<<<<<<<<<<<<<<<<<<<<<<<");
	}
}
