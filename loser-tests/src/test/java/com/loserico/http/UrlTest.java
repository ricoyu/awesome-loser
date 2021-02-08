package com.loserico.http;

import lombok.SneakyThrows;
import org.junit.Test;

import java.net.URLEncoder;

/**
 * <p>
 * Copyright: (C), 2021-02-04 16:42
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UrlTest {
	
	@SneakyThrows
	@Test
	public void testUrlEncoder() {
		String encode = URLEncoder.encode("HTTP/1.1", "UTF-8");
		System.out.println(encode);
	}
}
