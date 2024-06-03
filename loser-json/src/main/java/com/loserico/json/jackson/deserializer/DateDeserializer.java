package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;
import com.loserico.common.lang.utils.DateUtils;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Deserializer for Java 8 temporal {@link LocalDate}s.
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:21
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class DateDeserializer extends JSR310DateTimeDeserializerBase<Date> {
	private static final long serialVersionUID = 1L;

	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

	public static final DateDeserializer INSTANCE = new DateDeserializer();

	public DateDeserializer() {
		this(DEFAULT_FORMATTER);
	}

	public DateDeserializer(DateTimeFormatter dtf) {
		super(Date.class, dtf);
	}

	public DateDeserializer(DateDeserializer deserializer, Boolean leniency) {
		super(deserializer, leniency);
	}

	protected DateDeserializer(DateDeserializer base, JsonFormat.Shape shape) {
		super(base, shape);
	}

	@Override
	protected JSR310DateTimeDeserializerBase<Date> withDateFormat(DateTimeFormatter dtf) {
		return new DateDeserializer(dtf);
	}
	
	@Override
	protected JSR310DateTimeDeserializerBase<Date> withLeniency(Boolean leniency) {
		return new DateDeserializer(this, leniency);
	}
	
	@Override
	protected JSR310DateTimeDeserializerBase<Date> withShape(JsonFormat.Shape shape) {
		return new DateDeserializer(this, shape);
	}
	
	
	@Override
	public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		if (parser.hasToken(JsonToken.VALUE_STRING)) {
			String dateStr = parser.getText().trim();
			if (dateStr.length() == 0) {
				return null;
			}
			// as per [datatype-jsr310#37], only check for optional (and, incorrect...) time
			// marker 'T'
			// if we are using default formatter
			DateTimeFormatter format = _formatter;
			try {
				return DateUtils.parse(dateStr);
			} catch (DateTimeException e) {
				throw new UnsupportedOperationException("Cannot update object of type "
						+ LocalDate.class.getName() + " (by deserializer of type " + getClass().getName() + ")");
			}
		}
		if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
			long milis = parser.getLongValue();
			return new Date(milis);
		}
		if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
			return (Date) parser.getEmbeddedObject();
		}

		throw context.wrongTokenException(parser, JsonToken.VALUE_STRING, "Expected array or string.");
	}
}