package com.loserico.networking;

import com.loserico.networking.utils.DomainUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-09-13 14:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DomainUtilsTest {
	
	@Test
	public void test() {
		//String domain = "xcc.stage.710162.server.dnssec.sangforsec.com";
		//String domain = "";
		//String domain = "www.baidu.com";
		String domain = "baidu.com";
		String firstLevelDomain = DomainUtils.getDomain(domain, 1);
		String secondLevelDomain = DomainUtils.getDomain(domain, 2);
		assertEquals("baidu.com", firstLevelDomain);
		assertEquals("baidu.com", secondLevelDomain);
	}
	
}
