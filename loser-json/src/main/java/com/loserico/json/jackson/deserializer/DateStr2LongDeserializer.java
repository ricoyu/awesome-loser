package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.loserico.common.lang.utils.DateUtils;

import java.io.IOException;
import java.util.Date;

/**
 * 日期字符串反序列化成Long类型, 默认时区是+8
 * <p>
 * Copyright: (C), 2021-05-17 14:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DateStr2LongDeserializer extends JsonDeserializer<Long> {
	
	@Override
	public boolean isCachable() {
		return true;
	}
	
	@Override
	public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonToken curr = p.getCurrentToken();
		
		// Usually should just get string value:
		if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME) {
			final String source = p.getText();
			Date date = DateUtils.parse(source);
			if (date == null) {
				return null;
			}
			
			return date.getTime();
		}
		// But let's consider int acceptable as well (if within ordinal range)
		if (curr == JsonToken.VALUE_NUMBER_INT) {
			return p.getLongValue();
		}
		return null;
	}
}
