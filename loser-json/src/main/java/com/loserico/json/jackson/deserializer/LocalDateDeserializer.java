package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;
import com.loserico.common.lang.utils.DateUtils;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
public class LocalDateDeserializer extends JSR310DateTimeDeserializerBase<LocalDate> {
	private static final long serialVersionUID = 1L;

	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

	public static final LocalDateDeserializer INSTANCE = new LocalDateDeserializer();

	public LocalDateDeserializer() {
		this(DEFAULT_FORMATTER);
	}

	public LocalDateDeserializer(DateTimeFormatter dtf) {
		super(LocalDate.class, dtf);
	}
	
	public LocalDateDeserializer(LocalDateDeserializer deserializer, Boolean leniency) {
		super(deserializer, leniency);
	}
	
	protected LocalDateDeserializer(LocalDateDeserializer base, JsonFormat.Shape shape) {
		super(base, shape);
	}

	@Override
	protected JSR310DateTimeDeserializerBase<LocalDate> withDateFormat(DateTimeFormatter dtf) {
		return new LocalDateDeserializer(dtf);
	}
	
	@Override
	protected JSR310DateTimeDeserializerBase<LocalDate> withLeniency(Boolean leniency) {
		return new LocalDateDeserializer(this, leniency);
	}
	
	@Override
	protected JSR310DateTimeDeserializerBase<LocalDate> withShape(JsonFormat.Shape shape) {
		return new LocalDateDeserializer(this, shape);
	}
	
	
	@Override
	public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		if (parser.hasToken(JsonToken.VALUE_STRING)) {
			String string = parser.getText().trim();
			if (string.length() == 0) {
				return null;
			}
			// as per [datatype-jsr310#37], only check for optional (and, incorrect...) time
			// marker 'T'
			// if we are using default formatter
			DateTimeFormatter format = _formatter;
			try {
				if (format == DEFAULT_FORMATTER) {
					// JavaScript by default includes time in JSON serialized Dates (UTC/ISO instant
					// format).
					if (string.length() > 10 && string.charAt(10) == 'T') {
						if (string.endsWith("Z")) {
							return LocalDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC).toLocalDate();
						} else {
							return LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
						}
					}
				}
				return DateUtils.toLocalDate(string);
			} catch (DateTimeException e) {
				throw new UnsupportedOperationException("Cannot update object of type "
						+ LocalDate.class.getName() + " (by deserializer of type " + getClass().getName() + ")");
			}
		}
		if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
			long milis = parser.getLongValue();
			return Instant.ofEpochMilli(milis).atOffset(ZoneOffset.ofHours(8)).toLocalDate();
		}
		if (parser.isExpectedStartArrayToken()) {
			if (parser.nextToken() == JsonToken.END_ARRAY) {
				return null;
			}
			int year = parser.getIntValue();
			int month = parser.nextIntValue(-1);
			int day = parser.nextIntValue(-1);

			if (parser.nextToken() != JsonToken.END_ARRAY) {
				throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
			}
			return LocalDate.of(year, month, day);
		}
		if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
			return (LocalDate) parser.getEmbeddedObject();
		}

		throw context.wrongTokenException(parser, JsonToken.VALUE_STRING, "Expected array or string.");
	}
}