package com.loserico.networking;

import com.loserico.networking.utils.HttpUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020/4/22 16:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HttpUtilsTest {
	
	@Test
	public void testEncodeDecode() {
		String encoded = HttpUtils.urlEncode("三少爷");
		System.out.println("encoded:" + encoded);
		String decoded = HttpUtils.urlDecode(encoded);
		System.out.println("decoded:" + decoded);
		
	}
}
