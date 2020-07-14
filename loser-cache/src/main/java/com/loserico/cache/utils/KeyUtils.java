package com.loserico.cache.utils;

import com.loserico.cache.exception.InvalidKeyException;
import com.loserico.common.lang.utils.PrimitiveUtils;
import com.loserico.json.jackson.JacksonUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

import static java.lang.String.join;
import static java.text.MessageFormat.format;

/**
 * Redis Key 生成相关帮助类
 * <p>
 * Copyright: Copyright (c) 2018-09-13 10:40
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public final class KeyUtils {
	
	/**
	 * 用key模板生成最终的redis key
	 * keyArg如果是null，用空字符串代替
	 * keyArg必须是String、或其他基本类型，不支持对象类型
	 * @param template
	 * @param keyArgs
	 * @return String
	 * @on
	 */
	public static String formatKey(String template, Object... keyArgs) {
		Objects.requireNonNull(template, "template不能为null");

		if (keyArgs == null || keyArgs.length == 0) {
			return template;
		}

		/*
		 * 如果只有一个参数就不用Stream API去处理了
		 */
		if (keyArgs.length == 1) {
			Object keyArg = keyArgs[0];
			String key = toString(keyArg);
			return format(template, key);
		} else {
			Object[] args = Arrays.stream(keyArgs)
					.map((keyArg) -> {
						if (keyArg == null) {
							return "";
						}
						if (keyArg instanceof String) {
							return ((String) keyArg).trim();
						}

						return PrimitiveUtils.toString(keyArg);
					}).toArray(Object[]::new);

			return format(template, args);
		}

	}

	/**
	 * keyArgs只能包含字符串或者原子类型的对象
	 * 
	 * 用 : 连接key的各部分，返回最终的key
	 * 如果其中有null值，用空字符串替代，两头有空格则trim掉
	 * @param keyArgs
	 * @return
	 * @on
	 */
	public static String joinKey(Object... keyArgs) {
		Objects.requireNonNull(keyArgs, "keyArgs 不能为null");

		if (keyArgs.length == 0) {
			throw new IllegalArgumentException("必须提供一个参数");
		}

		if (keyArgs.length == 1) {
			return toString(keyArgs[0]);
		}

		String[] args = Arrays.stream(keyArgs)
				.map((keyArg) -> toString(keyArg))
				.toArray(String[]::new);

		return join(":", args);
	}

	/**
	 * 将对象转成字符串, 如果是null返回空字符串, trim掉两头空格
	 * 
	 * @param obj 要转成string的对象
	 * @return String
	 */
	private static String toString(Object obj) {
		if (obj == null) {
			return "";
		}

		if (obj instanceof String) {
			return ((String) obj).trim();
		}

		if (PrimitiveUtils.isPrimitive(obj)) {
			return PrimitiveUtils.toString(obj);
		}

		ArrayTypes arrayTypes = Types.arrayTypes(obj);
		if (arrayTypes != null) {
			StringJoiner stringJoiner = new StringJoiner(":");
			switch (arrayTypes) {
			case LONG_WRAPPER:
				Long[] arr1 = (Long[]) obj;
				for (int i = 0; i < arr1.length; i++) {
					stringJoiner.add(toString(arr1[i]));
				}
				break;
			case LONG:
				long[] arr2 = (long[]) obj;
				for (int i = 0; i < arr2.length; i++) {
					long value = arr2[i];
					stringJoiner.add(Long.toString(value));
				}
				break;
			case INTEGER_WRAPPER:
				Integer[] arr4 = (Integer[]) obj;
				for (int i = 0; i < arr4.length; i++) {
					Integer value = arr4[i];
					stringJoiner.add(toString(value));
				}
				break;
			case INTEGER:
				int[] arr3 = (int[]) obj;
				for (int i = 0; i < arr3.length; i++) {
					int value = arr3[i];
					stringJoiner.add(Integer.toString(value));
				}
				break;
			case STRING:
				String[] arr5 = (String[]) obj;
				for (int i = 0; i < arr5.length; i++) {
					stringJoiner.add(arr5[i]);
				}
				break;
			case DOUBLE_WRAPPER:
				Double[] arr7 = (Double[]) obj;
				for (int i = 0; i < arr7.length; i++) {
					Double value = arr7[i];
					stringJoiner.add(toString(value));
				}
				break;
			case DOUBLE:
				double[] arr6 = (double[]) obj;
				for (int i = 0; i < arr6.length; i++) {
					double value = arr6[i];
					stringJoiner.add(Double.toString(value));
				}
				break;
			case FLOAT_WRAPPER:
				Float[] arr9 = (Float[]) obj;
				for (int i = 0; i < arr9.length; i++) {
					float value = arr9[i];
					stringJoiner.add(toString(value));
				}
				break;
			case FLOAT:
				float[] arr8 = (float[]) obj;
				for (int i = 0; i < arr8.length; i++) {
					float value = arr8[i];
					stringJoiner.add(Float.toString(value));
				}
				break;

			default:
				break;
			}

			return stringJoiner.toString();
		}

		return JacksonUtils.toJson(obj);
	}
	
	/**
	 * key不能为null或者空字符串
	 * @param key
	 */
	public static void requireNonBlank(String key) {
		if (null == key || "".equals(key.trim())) {
			throw new InvalidKeyException("key不能为空");
		}
	}
}
