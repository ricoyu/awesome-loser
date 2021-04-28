package com.loserico.common.spring.utils;

import com.loserico.common.spring.http.Requests;
import lombok.Data;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-04-25 11:44
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RequestsTest {
	
	@Test
	public void testFormSubmit() {
		Requests.form("http://localhost:8080/users")
				.param("username", "ricoyu")
				.request();
	}
	
	@Test
	public void testPost() {
		String requestBody = "{\n" +
				"  \"id\": 31,\n" +
				"  \"ruleName\": \"redis规则h\",\n" +
				"  \"protocol\": \"tcp\",\n" +
				"  \"switcher\": true,\n" +
				"  \"riskLevel\": \"high\",\n" +
				"  \"remark\": \"备注2\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"id\": 64,\n" +
				"      \"field\": \"app_proto\",\n" +
				"      \"value\": \"redis\",\n" +
				"      \"operator\": \"ct\",\n" +
				"      \"logical\": \"\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";
		CustomRule customRule = Requests.post("http://localhost:8081/custom-rule")
				.body(requestBody)
				.addHeader("authToken", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9")
				.responseType(CustomRule.class)
				.request();
	}
	
	@Test
	public void testGet() {
		String url = "http://192.168.100.101:9200/rico/_mapping";
		String response = Requests.get(url).request();
	}
	
	@Data
	public static class CustomRule {
		private Long id;
		private Long ruleSeq;
		private String ruleName;
		private String protocol;
		private Boolean switcher;
		private String riskLevel;
		private String remark;
	}
}
