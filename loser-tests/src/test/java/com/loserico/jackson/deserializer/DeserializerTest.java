package com.loserico.jackson.deserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.loserico.json.jackson.JacksonUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * Copyright: (C), 2020/5/10 15:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DeserializerTest {
	
	public static void main(String[] args) {
		//System.out.println(new Date().getTime());
		String s = "{\"name\": \"sanshaoye\", \"time\": 1589096695}";
		User user = JacksonUtils.toObject(s, User.class);
		System.out.println(user.getTime());
	}
	
	public static class User {
		private String name;
		
		private LocalDateTime time;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		@JsonDeserialize(using = SecondsToLocalDateTimeDeserializer.class)
		public LocalDateTime getTime() {
			return time;
		}
		
		public void setTime(LocalDateTime time) {
			this.time = time;
		}
	}
}
