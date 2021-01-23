package com.loserico.json.jsonpath.mapper;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.mapper.MappingException;

import java.lang.reflect.Type;

/**
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:28
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class JacksonMappingProvider implements LoserMappingProvider {

	private final ObjectMapper objectMapper;

	public JacksonMappingProvider() {
		this(new ObjectMapper());
	}

	public JacksonMappingProvider(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}


	@Override
	public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
		if (source == null) {
			return null;
		}
		try {
			return objectMapper.convertValue(source, targetType);
		} catch (Exception e) {
			throw new MappingException(e);
		}

	}

	@Override
	public <T> T map(Object source, final TypeRef<T> targetType, Configuration configuration) {
		if (source == null) {
			return null;
		}
		JavaType type = objectMapper.getTypeFactory().constructType(targetType.getType());

		try {
			return (T) objectMapper.convertValue(source, type);
		} catch (Exception e) {
			throw new MappingException(e);
		}

	}

	@Override
	public <T> T map(Object source, Type targetType, Configuration configuration) {
		if (source == null) {
			return null;
		}
		JavaType type = objectMapper.getTypeFactory().constructType(targetType);

		try {
			return (T) objectMapper.convertValue(source, type);
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}
}
