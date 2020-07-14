package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class LocalDateSerializer extends com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer {
	private static final long serialVersionUID = 1L;

	private ZoneOffset zoneOffset = ZoneOffset.ofHours(+8);
	private boolean useTimestamp = false;

	public LocalDateSerializer() {
	}

	/**
	 * 是否序列化成EPOCH 毫秒数，当前时区为东八区。默认序列化成yyy-MM-dd
	 * @param useTimestamp
	 */
	public LocalDateSerializer(boolean useTimestamp) {
		this.useTimestamp = useTimestamp;
	}
	
	/**
	 * 是否序列化成EPOCH 毫秒数，同时指定当前时区。默认序列化成yyy-MM-dd
	 * @param useTimestamp
	 */
	public LocalDateSerializer(boolean useTimestamp, ZoneOffset zoneOffset) {
		this.useTimestamp = useTimestamp;
		this.zoneOffset = zoneOffset;
	}

	@Override
	public void serialize(LocalDate date, JsonGenerator g, SerializerProvider provider) throws IOException {
		if (useTimestamp) {
			g.writeString(date.atStartOfDay(zoneOffset).toInstant().toEpochMilli() + "");
		} else {
			g.writeString((_formatter == null) ? date.toString() : date.format(_formatter));
		}
	}

}