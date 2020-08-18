package com.loserico.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.jackson.deserializer.SimpleGrantedAuthority;
import com.loserico.jackson.deserializer.SimpleGrantedAuthorityDeserializer;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-08-14 13:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SimpleGrantedAuthorityTest {
	
	@Test
	public void testDeserializer() throws JsonProcessingException {
		String json = IOUtils.readClassPathFileAsString("authorities.json");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new SimpleModule().addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer()));
		SimpleGrantedAuthority simpleGrantedAuthority = objectMapper.readValue(json, SimpleGrantedAuthority.class);
		System.out.println(simpleGrantedAuthority.getAuthority());
		
		System.out.println(objectMapper.writeValueAsString(simpleGrantedAuthority));
	}
}
