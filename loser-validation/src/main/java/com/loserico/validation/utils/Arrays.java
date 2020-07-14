package com.loserico.validation.utils;

import java.util.Objects;

import static java.util.Arrays.stream;

/**
 * 数组操作帮助类
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:42
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class Arrays {

	public static final <T> T[] asArray(T... args) {
		return args;
	}

	/**
	 * 过滤Array中的null元素并排序
	 *
	 * @param args
	 * @return T[]
	 */
	public static final <T> T[] nonNull(T... args) {
		if (args == null) {
			return null;
		}
		return (T[]) stream(args)
				.filter(Objects::nonNull)
				.sorted()
				.distinct()
				.toArray();

	}
}