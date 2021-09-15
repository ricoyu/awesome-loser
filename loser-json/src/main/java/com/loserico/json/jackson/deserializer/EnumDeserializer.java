package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.loserico.common.lang.utils.EnumUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class EnumDeserializer extends JsonDeserializer<Enum> {

	private final Class<? extends Enum> enumType;
	private Set<String> enumProperties = new HashSet<>();
	private JsonDeserializer<?> deserializer;

	public EnumDeserializer(Class<? extends Enum> enumType) {
		this.enumType = enumType;
	}

	public EnumDeserializer(Class<? extends Enum> enumType, Set<String> enumProperties) {
		this.enumType = enumType;
		this.enumProperties = enumProperties;
	}

	public EnumDeserializer(Class<? extends Enum> enumType, Set<String> enumProperties, JsonDeserializer<?> deserializer) {
		this.enumType = enumType;
		this.enumProperties = enumProperties;
		this.deserializer = deserializer;
	}

	/**
	 * Because of costs associated with constructing Enum resolvers, let's cache
	 * instances by default.
	 */
	@Override
	public boolean isCachable() {
		return true;
	}

	@Override
	public Enum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		Enum result = toEnum(p);
		if (result != null) {
			return result;
		}
		
		return (Enum) deserializer.deserialize(p, ctxt);
	}
	
	private Enum toEnum(JsonParser p) throws IOException {
		Enum result;
		JsonToken curr = p.getCurrentToken();
		
		// Usually should just get string value:
		if (curr == JsonToken.VALUE_STRING || curr == JsonToken.FIELD_NAME) {
			final String source = p.getText();
			result = EnumUtils.lookupEnum(enumType, source);
			if (result != null) {
				return result;
			}
			for (String property : enumProperties) {
				result = EnumUtils.lookupEnum(enumType, source, property);
				if (result != null) {
					return result;
				}
			}
		}
		// But let's consider int acceptable as well (if within ordinal range)
		if (curr == JsonToken.VALUE_NUMBER_INT) {
			int index = p.getIntValue();
			return EnumUtils.lookupEnum(enumType, index);
		}
		return null;
	}
}
