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
 * Copyright: (C), 2020/5/10 15:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SecondsToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
	
	@Override
	public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		long seconds = jsonParser.getLongValue();
		return DateUtils.secondsToLocalDateTime(seconds);
	}
}