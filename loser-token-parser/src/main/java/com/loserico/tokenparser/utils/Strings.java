package com.loserico.tokenparser.utils;

/**
 * <p>
 * Copyright: (C), 2020-09-16 13:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class Strings {
	
	public static String toString(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Integer) {
			return ((Integer) value).toString();
		}
		if (value instanceof Long) {
			return ((Long) value).toString();
		}
		if (value instanceof Double) {
			return ((Double) value).toString();
		}
		if (value instanceof Float) {
			return ((Float) value).toString();
		}
		if (value instanceof Boolean) {
			return ((Boolean) value).toString();
		}
		if (value instanceof Short) {
			return ((Short) value).toString();
		}
		if (value instanceof Character) {
			return ((Character) value).toString();
		}
		if (value instanceof Byte) {
			return ((Byte) value).toString();
		}
		return null;
	}
}
