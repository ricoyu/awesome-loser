package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class NullKeySerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeFieldName("");
	}
}