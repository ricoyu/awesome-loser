package com.loserico.mongo.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.loserico.common.lang.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * <p>
 * Copyright: (C), 2020-10-23 17:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class GenericAnnotationSerializer<T> extends StdSerializer<T> {
	
	public GenericAnnotationSerializer() {
		this(null);
	}
	
	public GenericAnnotationSerializer(Class<T> clazz) {
		super(clazz);
	}
	
	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (value == null) {
			return;
		}
		
		gen.writeStartObject();
		Field[] fields = ReflectionUtils.getFields(value.getClass());
	}
}
