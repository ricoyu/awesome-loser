package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
	private static final long serialVersionUID = 1L;
	
	private ZoneOffset zoneOffset = ZoneOffset.ofHours(8);

	public LocalDateTimeSerializer() {
		super(LocalDateTime.class);
	}
	
	public LocalDateTimeSerializer(ZoneOffset zoneOffset) {
		super(LocalDateTime.class);
	}

	@Override
	public void serialize(LocalDateTime date, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeNumber(date.atZone(zoneOffset).toInstant().toEpochMilli());
	}
}