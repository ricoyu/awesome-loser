package com.loserico.cache.utils;

import com.loserico.common.lang.utils.PrimitiveUtils;
import com.loserico.json.jackson.JacksonUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

/**
 * <p>
 * Copyright: Copyright (c) 2019/10/17 15:24
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ByteUtils {

	public static byte[] toBytes(Object obj) {
		if (obj == null) {
			return new byte[0];
		}
		// 先检查一下是不是原子类型, 是的话直接toString
		String primitive = PrimitiveUtils.toString(obj);
		if (primitive != null) {
			return primitive.getBytes(UTF_8);
		} else if (String.class.isInstance(obj)) {
			return ((String) obj).getBytes(UTF_8);
		} else if (Collection.class.isInstance(obj)) {
			return JacksonUtils.toBytes(obj);
		} else {// key不可序列化
			return JacksonUtils.toBytes(obj);
		}
	}

	public static byte[] toBytes(String value) {
		if (value == null) {
			return new byte[0];
		}
		return value.getBytes(UTF_8);
	}

	public static byte[] toBytes(long value) {
		return String.valueOf(value).getBytes(UTF_8);
	}

	public static byte[] toBytes(int value) {
		return String.valueOf(value).getBytes(UTF_8);
	}

	public static byte[] toBytes(double value) {
		return String.valueOf(value).getBytes(UTF_8);
	}

	public static byte[] toBytes(BigDecimal value) {
		if (value == null) {
			return new byte[0];
		}
		return String.valueOf(value).getBytes(UTF_8);
	}

	public static byte[] toBytes(long time, TimeUnit timeUnit) {
		long seconds = timeUnit.toSeconds(time);
		return toBytes(seconds);
	}

	public static byte[][] toBytes(List<?> values) {
		List<byte[]> bytes = values.stream()
				.map((value) -> toBytes(value))
				.collect(Collectors.toList());
		byte[][] bytesArrays = new byte[values.size()][];
		for (int i = 0; i < bytesArrays.length; i++) {
			bytesArrays[i] = bytes.get(i);
		}

		return bytesArrays;
	}

	public static byte[][] toBytes(Object... values) {
		List<byte[]> bytes = asList(values).stream()
				.map((value) -> toBytes(value))
				.collect(Collectors.toList());
		byte[][] bytesArrays = new byte[values.length][];
		for (int i = 0; i < bytesArrays.length; i++) {
			bytesArrays[i] = bytes.get(i);
		}

		return bytesArrays;
	}
}
