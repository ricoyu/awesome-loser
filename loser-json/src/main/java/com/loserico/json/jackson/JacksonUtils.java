package com.loserico.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.loserico.json.ObjectMapperDecorator;
import com.loserico.json.exception.JacksonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Jackson工具类
 * <p>
 * Copyright: Copyright (c) 2017-10-30 13:13
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class JacksonUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);
	
	private static ObjectMapper objectMapper = null;
	
	static {
		ObjectMapperDecorator decorator = new ObjectMapperDecorator();
		objectMapper = ObjectMapperFactory.createOrFromBeanFactory();
		decorator.decorate(objectMapper);
	}
	
	/**
	 * 将json字符串转成指定对象
	 *
	 * @param json
	 * @param clazz
	 * @return T
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		if (isBlank(json)) {
			return null;
		}
		
		if (clazz.isAssignableFrom(String.class)) {
			return (T) json;
		}
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JacksonException(e);
		}
	}
	
	public static <T> T toObject(byte[] src, Class<T> clazz) {
		try {
			return objectMapper.readValue(src, clazz);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JacksonException(e);
		}
	}
	
	/**
	 * Map转POJO
	 *
	 * @param map
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T mapToPojo(Map map, Class<T> clazz) {
		return objectMapper.convertValue(map, clazz);
	}
	
	/**
	 * JSON字符串转MAP
	 *
	 * @param json
	 * @return
	 */
	public static <T> Map<String, T> toMap(String json) {
		if (isBlank(json)) {
			return emptyMap();
		}
		Map<String, T> map = new HashMap<String, T>();
		try {
			return objectMapper.readValue(json, new TypeReference<Map<String, T>>() {
			});
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JacksonException(e);
		}
	}
	
	/**
	 * POJO转Map
	 *
	 * @param pojo
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String, T> pojoToMap(Object pojo) {
		return objectMapper.convertValue(pojo, new TypeReference<Map<String, T>>() {
		});
	}
	
	/**
	 * JSON字符串转MAP
	 *
	 * @param json
	 * @return
	 */
	public static <K, V> Map<K, V> toGenericMap(String json) {
		if (isBlank(json)) {
			return emptyMap();
		}
		Map<K, V> map = new HashMap<>();
		try {
			return objectMapper.readValue(json, new TypeReference<Map<K, V>>() {
			});
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JacksonException(e);
		}
	}
	
	public static <T> List<T> toList(String jsonArray, Class<T> clazz) {
		if (isBlank(jsonArray)) {
			return emptyList();
		}
		CollectionType javaType = objectMapper.getTypeFactory()
				.constructCollectionType(List.class, clazz);
		try {
			return objectMapper.readValue(jsonArray, javaType);
		} catch (IOException e) {
			logger.error("Parse json array \n{} \n to List of type {} failed", jsonArray, clazz, e);
			throw new JacksonException(e);
		}
	}
	
	/**
	 * 将对象转成json串
	 *
	 * @param object
	 * @param <T>
	 * @return
	 */
	public static <T> String toJson(T object) {
		if (object == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			throw new JacksonException(e);
		}
	}
	
	public static byte[] toBytes(Object object) {
		if (object == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			throw new JacksonException(e);
		}
	}
	
	public static <T> String toPrettyJson(T object) {
		if (object == null) {
			return null;
		}
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			throw new JacksonException(e);
		}
	}
	
	public static void writeValue(Writer writer, Object value) {
		try {
			objectMapper.writeValue(writer, value);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new JacksonException(e);
		}
	}
	
	public static ObjectMapper objectMapper() {
		return objectMapper;
	}
	
	public static void addMixIn(Class target, Class mixinSource) {
		objectMapper.addMixIn(target, mixinSource);
	}
}
