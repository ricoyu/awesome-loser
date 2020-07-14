package com.loserico.jackson.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试序列化/反序列化没有默认构造函数的POJO
 * <p>
 * Copyright: (C), 2020/4/29 18:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DeserializeWithNoDefaultConstructorTest {
	
	private OAuth2Authentication oAuth2Authentication;
	
	@Before
	public void setup() {
		oAuth2Authentication = new OAuth2Authentication("三少爷", 38);
	}
	
	@SneakyThrows
	@Test
	public void testSerializeWithoutMixin() {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(oAuth2Authentication);
		System.out.println(json);
		OAuth2Authentication auth = objectMapper.readValue(json, OAuth2Authentication.class);
		System.out.println(auth.getName());
	}
	
	@SneakyThrows
	@Test
	public void testSerializeWithMixin() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.addMixIn(OAuth2Authentication.class, OAuth2AuthenticationMinIn.class);
		String json = objectMapper.writeValueAsString(oAuth2Authentication);
		System.out.println(json);
		OAuth2Authentication auth = objectMapper.readValue(json, OAuth2Authentication.class);
		System.out.println(auth.getName());
	}
}
