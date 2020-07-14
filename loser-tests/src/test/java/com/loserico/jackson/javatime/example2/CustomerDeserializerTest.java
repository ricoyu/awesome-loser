package com.loserico.jackson.javatime.example2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.loserico.json.jackson.serializer.LocalDateSerializer;
import com.loserico.json.jackson.serializer.LocalDateTimeSerializer;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * https://stackoverflow.com/questions/44422616/jackson-deserialize-epoch-to-localdate
 * <p>
 * Copyright: Copyright (c) 2017-11-01 22:08
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class CustomerDeserializerTest {

	@Test
	public void testGetTime() {
		System.out.println(new Date().getTime());
	}
	@Test
	public void testName() throws IOException {
		// formatter that accepts an epoch millis value
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				// epoch seconds
				.appendValue(ChronoField.INSTANT_SECONDS, 1, 19, SignStyle.NEVER)
				// milliseconds
				.appendValue(ChronoField.MILLI_OF_SECOND, 3)
				// create formatter, using UTC as timezone
				.toFormatter().withZone(ZoneOffset.UTC);

		ObjectMapper objectMapper = new ObjectMapper();
		JavaTimeModule module = new JavaTimeModule();
		// add the LocalDateDeserializer with the custom formatter
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));
		module.addSerializer(LocalDate.class, new LocalDateSerializer());
		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
		objectMapper.registerModule(module);
		
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
				.withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		
		FbProfile fbProfile = new FbProfile();
		fbProfile.setBirthday(LocalDate.of(1982, 11, 9));
		fbProfile.setId(1L);
		
		String json = objectMapper.writeValueAsString(fbProfile);
		System.out.println("输出: " + json);
		FbProfile fbProfile2 = objectMapper.readValue(json, FbProfile.class);
		
		System.out.println(fbProfile2.getBirthday());
		
		FbProfile fbProfile3 = objectMapper.readValue("{\"id\":1,\"birthday\":\"1509585416991\"}", FbProfile.class);
//		FbProfile fbProfile3 = mapper.readValue("{\"id\":1,\"birthday\":\"1509585243042\"}", FbProfile.class);
		System.out.println(fbProfile3.getBirthday());
	}
}
