package com.loserico.jackson.customdeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.loserico.jackson.customserializer.Car;

import java.io.IOException;

/**
 * <p>
 * Copyright: (C), 2020-09-17 10:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CustomCarDeserializer extends StdDeserializer<Car> {
	
	public CustomCarDeserializer() {
		this(null);
	}
	
	public CustomCarDeserializer(Class<?> clazz) {
		super(clazz);
	}
	
	@Override
	public Car deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Car car = new Car();
		
		ObjectCodec codec = parser.getCodec();
		JsonNode node = codec.readTree(parser);
		
		String color = node.get("color").asText();
		car.setColor(color);
		return car;
	}
}
