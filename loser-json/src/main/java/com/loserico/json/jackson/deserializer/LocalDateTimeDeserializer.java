package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * 将日期字符串反序列化成LocalDateTime对象
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:49
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalDateTimeDeserializer extends JSR310DateTimeDeserializerBase<LocalDateTime> {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(LocalDateTimeDeserializer.class);
	
	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	
	public static final LocalDateTimeDeserializer INSTANCE = new LocalDateTimeDeserializer();
	
	private static final DateTimeFormatter formatter1 = ofPattern("yyyy-MM-dd").withZone(ZoneOffset.ofHours(8));
	private static final DateTimeFormatter formatter2 = ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.ofHours(8));
	private static final DateTimeFormatter formatter3 = ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.ofHours(8));
	private static final DateTimeFormatter formatter4 = ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneOffset.ofHours(8));
	
	private static final DateTimeFormatter UTC_EPOC_HMILIS_FORMATER = new DateTimeFormatterBuilder()
			.appendValue(ChronoField.INSTANT_SECONDS, 1, 19, SignStyle.NEVER)
			.appendValue(ChronoField.MILLI_OF_SECOND, 3)
			.toFormatter().withZone(ZoneOffset.ofHours(8));
	
	public LocalDateTimeDeserializer() {
		this(UTC_EPOC_HMILIS_FORMATER);
	}
	
	public LocalDateTimeDeserializer(DateTimeFormatter formatter) {
		super(LocalDateTime.class, formatter);
	}
	
	@Override
	protected LocalDateTimeDeserializer withShape(JsonFormat.Shape shape) {
		return this;
	}
	
	@Override
	protected JSR310DateTimeDeserializerBase<LocalDateTime> withDateFormat(DateTimeFormatter formatter) {
		return new LocalDateTimeDeserializer(formatter);
	}
	
	@Override
	protected JSR310DateTimeDeserializerBase<LocalDateTime> withLeniency(Boolean leniency) {
		return new LocalDateTimeDeserializer(this, leniency);
	}
	
	protected LocalDateTimeDeserializer(LocalDateTimeDeserializer base, Boolean leniency) {
		super(base, leniency);
	}
	
	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		//数字类型的毫秒数形式：1520007540000
		if (parser.hasTokenId(JsonTokenId.ID_NUMBER_INT)) {
			return LocalDateTime.parse(parser.getValueAsString(), _formatter);
		}
		
		if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
			String string = parser.getText().trim();
			if (string.length() == 0) {
				return null;
			}
			
			try {
				if (string.contains("-") || string.contains(" ")) {
					return parse(string);
				}
				return LocalDateTime.parse(string, _formatter);
			} catch (DateTimeException e) {
				throw new UnsupportedOperationException("Cannot update object of type "
						+ LocalDateTime.class.getName() + " (by deserializer of type " + getClass().getName() + ")");
			}
		}
		
		
		if (parser.isExpectedStartArrayToken()) {
			if (parser.nextToken() == JsonToken.END_ARRAY) {
				return null;
			}
			int year = parser.getIntValue();
			int month = parser.nextIntValue(-1);
			int day = parser.nextIntValue(-1);
			int hour = parser.nextIntValue(-1);
			int minute = parser.nextIntValue(-1);
			
			if (parser.nextToken() != JsonToken.END_ARRAY) {
				int second = parser.getIntValue();
				
				if (parser.nextToken() != JsonToken.END_ARRAY) {
					int partialSecond = parser.getIntValue();
					if (partialSecond < 1_000 &&
							!context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
						partialSecond *= 1_000_000;
					}
					if (parser.nextToken() != JsonToken.END_ARRAY) {
						throw context.wrongTokenException(parser, LocalDateTime.class, JsonToken.END_ARRAY, "Expected array to end.");
					}
					return LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
				}
				return LocalDateTime.of(year, month, day, hour, minute, second);
			}
			return LocalDateTime.of(year, month, day, hour, minute);
		}
		if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
			return (LocalDateTime) parser.getEmbeddedObject();
		}
		
		throw context.wrongTokenException(parser, LocalDateTime.class, JsonToken.VALUE_STRING, "Expected array or string.");
	}
	
	private LocalDateTime parse(String string) {
		int length = string.length();
		switch (length) {
			case 10:
				long milis = LocalDate.parse(string, formatter1).atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
				return LocalDateTime.ofInstant(Instant.ofEpochMilli(milis), ZoneOffset.ofHours(8));
			case 16:
				return LocalDateTime.parse(string, formatter2);
			case 19:
				return LocalDateTime.parse(string, formatter3);
			case 23:
				return LocalDateTime.parse(string, formatter4);
			default:
				break;
		}
		
		return LocalDateTime.parse(string, DEFAULT_FORMATTER);
	}
}
