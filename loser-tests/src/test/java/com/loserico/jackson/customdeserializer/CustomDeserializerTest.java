package com.loserico.jackson.customdeserializer;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.loserico.jackson.customserializer.Car;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Copyright: (C), 2020-09-17 10:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CustomDeserializerTest {
	
	@SneakyThrows
	@Test
	public void testCustomDeserializer() {
		SimpleModule module = new SimpleModule("CustomCarDeserializer", new Version(1, 0, 0, null, null, null));
		module.addDeserializer(Car.class, new CustomCarDeserializer());
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);
		
		String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
		Car car = mapper.readValue(json, Car.class);
		assertTrue(car.getColor().equals("Black"));
	}
}
