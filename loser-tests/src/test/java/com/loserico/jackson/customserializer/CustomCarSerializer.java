package com.loserico.jackson.customserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * <p>
 * Copyright: (C), 2020-09-17 10:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CustomCarSerializer extends StdSerializer<Car> {
	
	public CustomCarSerializer() {
		this(null);
	}
	
	public CustomCarSerializer(Class clazz) {
		super(clazz);
	}
	
	@Override
	public void serialize(Car car, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("car_brand", car.getType());
		gen.writeEndObject();
	}
}
