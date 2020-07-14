package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.loserico.json.jackson.serializer.annotation.Precision;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FloatContextualSerializer extends JsonSerializer<Float> implements ContextualSerializer {

	private int precision = 0;

	public FloatContextualSerializer(int precision) {
		this.precision = precision;
	}

	public FloatContextualSerializer() {

	}

	@Override
	public void serialize(Float value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (precision == 0) {
			gen.writeNumber(value.floatValue());
		} else {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(precision, RoundingMode.HALF_UP);
			gen.writeNumber(bd.floatValue());
		}

	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		Precision precision = property.getAnnotation(Precision.class);
		if (precision != null) {
			return new FloatContextualSerializer(precision.precision());
		}
		return this;
	}
}