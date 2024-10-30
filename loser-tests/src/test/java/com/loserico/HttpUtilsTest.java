package com.loserico;

import com.loserico.cache.JedisUtils;
import com.loserico.codec.RsaUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.networking.utils.HttpUtils;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2021-05-18 15:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HttpUtilsTest {
	
	@Test
	public void testAuth() {
		String responseJson = HttpUtils.get("http://localhost:8083/pic-code").request();
		String codeId = JsonPathUtils.readNode(responseJson, "$.data.codeId");
		String code = JedisUtils.get("verifycode:" + codeId.toLowerCase());
		
		String password = RsaUtils.publicEncrypt("Admin#123");
		
		String loginResponse = HttpUtils.form("http://localhost:8083/login")
				.username("admin")
				.password(password)
				.formData("codeId", codeId)
				.formData("verifyCode", code)
				.request();
		String token = JsonPathUtils.readNode(loginResponse, "$.data");
		
		
		Object result = HttpUtils.get("http://localhost:8083/menu?debug=true")
				.bearerAuth(token)
				.request();
		System.out.println(result);
	}

	@Test
	public void testEsSettings() {
		String url = "http://192.168.100.101:9200/_cluster/settings?include_defaults=true&flat_settings=true";
		Object response = HttpUtils.get(url)
				.basicAuth("elastic", "qianyu14")
				.connectionTimeout(120, TimeUnit.SECONDS)
				.request();
		System.out.println(response);
	}
}
