package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.loserico.common.lang.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 * Copyright: Copyright (c) 2020-05-10 15:55
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MillisToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
	
	@Override
	public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		long millis = jsonParser.getLongValue();
		return DateUtils.toLocalDateTime(millis);
	}
}