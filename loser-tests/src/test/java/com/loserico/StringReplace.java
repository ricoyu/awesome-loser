package com.loserico;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2019/12/24 9:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StringReplace {
	
	@Test
	public void testStringReplace() {
		String s = IOUtils.readClassPathFileAsString("nursery.json");
		//System.out.println(s);
		s = s.replace("\"", "\\\"");
		System.out.println(s);
	}
	
	@Test
	public void testStringReplace2() {
		String s = "rocketmq.namesrv";
		System.out.println(s.replace("rocketmq.", ""));
	}
}
