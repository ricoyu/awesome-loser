package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

/**
 * 序列化LocalTime 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalTimeSerializer extends com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer {

	private static final long serialVersionUID = -1781796527684857415L;
	private boolean useTimestamp = false;
	private DateTimeFormatter formatter = null;
	public static final LocalTimeSerializer INSTANCE = new LocalTimeSerializer();

	protected LocalTimeSerializer() {
		this(null);
	}

	public LocalTimeSerializer(boolean useTimestamp, DateTimeFormatter formatter) {
		this.useTimestamp = useTimestamp;
		this.formatter = formatter;
	}

	public LocalTimeSerializer(DateTimeFormatter formatter) {
		this.formatter = formatter;
	}

	@Override
	public void serialize(LocalTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
		if (useTimestamp) {
			g.writeStartArray();
			g.writeNumber(value.getHour());
			g.writeNumber(value.getMinute());
			if (value.getSecond() > 0 || value.getNano() > 0) {
				g.writeNumber(value.getSecond());
				if (value.getNano() > 0) {
					if (provider.isEnabled(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
						g.writeNumber(value.getNano());
					} else {
						g.writeNumber(value.get(ChronoField.MILLI_OF_SECOND));
					}
				}
			}
			g.writeEndArray();
		} else {
			DateTimeFormatter dtf = formatter;
			if (dtf == null) {
				dtf = _defaultFormatter();
			}
			g.writeString(value.format(dtf));
		}
	}

	@Override
	protected DateTimeFormatter _defaultFormatter() {
		return DateTimeFormatter.ISO_LOCAL_TIME;
	}
}