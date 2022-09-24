package com.loserico.base64;

import com.loserico.common.lang.utils.StringUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * <p>
 * Copyright: (C), 2022-09-18 17:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class URLEncoderTest {
	
	@Test
	public void testURLEncoder() {
		try {
			System.out.println(URLEncoder.encode("俞雪华", StringUtils.DEFAULT_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
