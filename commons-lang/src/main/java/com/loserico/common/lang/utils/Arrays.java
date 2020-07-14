package com.loserico.common.lang.utils;

import java.util.Objects;

import static java.util.Arrays.stream;

public final class Arrays {

	@SuppressWarnings("unchecked")
	public static final <T> T[] asArray(T... args) {
		return args;
	}

	/**
	 * 过滤Array中的null元素并排序
	 * @param args
	 * @return T[]
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T[] nonNull(T... args) {
		if(args == null) {
			return null;
		}
		return (T[]) stream(args)
				.filter(Objects::nonNull)
				.sorted()
				.distinct()
				.toArray();

	}
}
