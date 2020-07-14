package com.loserico.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.loserico.json.jackson.JacksonUtils;
import lombok.Data;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * <p>
 * Copyright: (C), 2020/4/29 16:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NoDefaultConstructorTest {
	
	@Test
	public void test() {
		OAuth2Authentication sanshaoye = new OAuth2Authentication(LocalDateTime.now(), "sanshaoye");
		String s = JacksonUtils.toJson(sanshaoye);
		OAuth2Authentication auth2Authentication = JacksonUtils.toObject(s, OAuth2Authentication.class);
		System.out.println(auth2Authentication.getLocalDateTime());
	}
	
	@Data
	static class OAuth2Authentication {
		
		private static final long serialVersionUID = -4809832298438307309L;
		
		private final LocalDateTime localDateTime;
		
		private final String name;
		
		@JsonCreator
		public OAuth2Authentication(LocalDateTime localDateTime, String name) {
			this.localDateTime = localDateTime;
			this.name = name;
		}
		
	}
}
