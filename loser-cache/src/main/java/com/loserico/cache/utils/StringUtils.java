package com.loserico.cache.utils;

/**
 * <p>
 * Copyright: (C), 2020/4/1 11:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class StringUtils {
	
	/**
	 * 校验参数不能为null或者空字符串
	 * @param s
	 */
	public static void requireNonEmpty(String s) {
		if (s == null || "".equals(s.trim())) {
			throw new IllegalArgumentException("参数不能为空");
		}
	}
}
