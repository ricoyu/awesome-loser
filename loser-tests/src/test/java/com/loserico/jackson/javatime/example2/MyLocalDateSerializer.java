package com.loserico.jackson.javatime.example2;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serializer for Java 8 temporal {@link LocalDate}s.
 *
 * @author Nick Williams
 * @since 2.2
 */
public class MyLocalDateSerializer extends StdSerializer<LocalDate> {
	private static final long serialVersionUID = 1L;

	public MyLocalDateSerializer() {
		super(LocalDate.class);
	}

	@Override
	public void serialize(LocalDate date, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeString(date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()+"");
	}
}