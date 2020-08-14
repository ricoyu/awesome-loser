package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * <p>
 * Copyright: (C), 2020-7-22 0022 10:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {
	
	@Override
	public Class<String> handledType() {
		return String.class;
	}
	
	@Override
	public void serialize(String value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
		if (isNotEmpty(value)) {
			String encodedValue = StringEscapeUtils.escapeHtml4(value);
			generator.writeString(encodedValue);
		}
	}
}
