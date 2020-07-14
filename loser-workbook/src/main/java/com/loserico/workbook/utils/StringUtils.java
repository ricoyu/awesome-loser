package com.loserico.workbook.utils;

/**
 * <p>
 * Copyright: Copyright (c) 2019/10/15 11:08
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StringUtils {

	/**
	 * 移除开头结尾的所有空格、单引号、双引号
	 *
	 * @param source
	 * @return String
	 */
	public static String trimQuote(String source) {
		if (source == null) {
			return null;
		}
		source = source.trim();

		boolean found = false;
		if (source.startsWith("\"") || source.startsWith("'")) {
			source = source.substring(1, source.length());
			found = true;
		}
		if (source.endsWith("\"") || source.endsWith("'")) {
			source = source.substring(0, source.length() - 1);
			found = true;
		}
		if (found) {
			return trimQuote(source);
		}
		return source;
	}
}
