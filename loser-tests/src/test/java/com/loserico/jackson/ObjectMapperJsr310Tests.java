package com.loserico.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by izeye on 15. 10. 13..
 */
public class ObjectMapperJsr310Tests {

	@Test
	public void testDate() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		System.out.println(mapper.writeValueAsString(new Date()));
	}

	@Test
	public void testLocalDateTimeWithDateFormat() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		mapper.registerModule(new JavaTimeModule());
		System.out.println(mapper.writeValueAsString(LocalDateTime.now()));
	}

	@Test
	public void testLocalDateTimeWithCustomSerializer() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		mapper.registerModule(javaTimeModule);
		System.out.println(mapper.writeValueAsString(LocalDateTime.now()));
	}

}