package com.loserico.cache.utils;

import com.loserico.common.lang.utils.PrimitiveUtils;
import com.loserico.json.jackson.JacksonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: Copyright (c) 2019/10/17 15:26
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class UnMarshaller {

	@SuppressWarnings("unchecked")
	public static <T> T toObject(byte[] data, Class<T> clazz) {
		if (data == null || data.length == 0) {
			return null;
		}
		if (clazz.equals(String.class)) {
			return (T) new String(data, UTF_8);
		}
		T result = PrimitiveUtils.toPrimitive(data, clazz);
		if (result != null) {
			return result;
		}
		return JacksonUtils.toObject(toString(data), clazz);
	}

	public static <T> List<T> toList(byte[] value, Class<T> clazz) {
		if (value == null || value.length == 0) {
			return new ArrayList<>();
		}
		String json = toString(value);
		return JacksonUtils.toList(json, clazz);
	}

	public static String toString(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}
		return new String(data, UTF_8);
	}

	public static Long toLong(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}
		return new Long(new String(data, UTF_8));
	}

	public static int toSeconds(int time, TimeUnit timeUnit) {
		Long timeoutSeconds = timeUnit.toSeconds(time);
		return timeoutSeconds.intValue();
	}
}
