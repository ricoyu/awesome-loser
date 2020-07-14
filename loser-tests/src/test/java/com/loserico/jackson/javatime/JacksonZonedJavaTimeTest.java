package com.loserico.jackson.javatime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class JacksonZonedJavaTimeTest {

	@Test
	public void testName() throws JsonProcessingException {
		LocalDateTime localDateTime = LocalDateTime.of(2019, 3, 13, 0, 0);

		ZonedDateTime java8ZonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Shanghai"));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		System.out.println(objectMapper.writeValueAsString(java8ZonedDateTime));
	}
}
