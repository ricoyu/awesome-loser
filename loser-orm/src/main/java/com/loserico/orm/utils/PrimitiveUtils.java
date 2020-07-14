package com.loserico.orm.utils;
import java.math.BigDecimal;
import java.math.BigInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class PrimitiveUtils {
	
	public static boolean isPrimitive(Object value) {
		if (value == null) {
			return false;
		}
		
		Class<?> clazz = value.getClass();
		if (Byte.class.equals(clazz) || Byte.TYPE.equals(clazz)) {
			return true;
		}
		if (Short.class.equals(clazz) || Short.TYPE.equals(clazz)) {
			return true;
		}
		if (Integer.class.equals(clazz) || Integer.TYPE.equals(clazz)) {
			return true;
		}
		if (Long.class.equals(clazz) || Long.TYPE.equals(clazz)) {
			return true;
		}
		if (Float.class.equals(clazz) || Float.TYPE.equals(clazz)) {
			return true;
		}
		if (Double.class.equals(clazz) || Double.TYPE.equals(clazz)) {
			return true;
		}
		if (Boolean.class.equals(clazz) || Boolean.TYPE.equals(clazz)) {
			return true;
		}
		if (Character.class.equals(clazz) || Short.TYPE.equals(clazz)) {
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T toPrimitive(byte[] data, Class<T> clazz) {
		if (Byte.class.equals(clazz) || Byte.TYPE.equals(clazz)) {
			return (T) Byte.valueOf(toString(data));
		}
		if (Integer.class.equals(clazz) || Integer.TYPE.equals(clazz)) {
			return (T) Integer.valueOf(toString(data));
		}
		if (Long.class.equals(clazz) || Long.TYPE.equals(clazz)) {
			return (T) Long.valueOf(toString(data));
		}
		if (Double.class.equals(clazz)) {
			return (T) Double.valueOf(toString(data));
		}
		if (Float.class.equals(clazz) || Float.TYPE.equals(clazz)) {
			return (T) Float.valueOf(toString(data));
		}
		if (Boolean.class.equals(clazz) || Boolean.TYPE.equals(clazz)) {
			return (T) Boolean.valueOf(toString(data));
		}
		if (Short.class.equals(clazz) || Short.TYPE.equals(clazz)) {
			return (T) Short.valueOf(toString(data));
		}
		if (Character.class.equals(clazz) || Short.TYPE.equals(clazz)) {
			return (T) Character.valueOf(toString(data).charAt(0));
		}
		return null;
	}
	

	/**
	 * 如果value是null，那么返回0
	 * 否则返回对应的int value
	 * @param value
	 * @return
	 */
	public static int toInt(Object value) {
		if (value == null) {
			return 0;
		}
		if (value instanceof Integer) {
			return ((Integer) value).intValue();
		}
		if (value instanceof Long) {
			return ((Long) value).intValue();
		}
		if (value instanceof BigInteger) {
			return ((BigInteger) value).intValue();
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).intValue();
		}
		if (value instanceof Byte) {
			return ((Byte)value).intValue();
		}
		return 0;
	}
	

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
			return ((Byte)value).toString();
		}
		return null;
	}
	
	public static boolean isByteArray(Object obj) {
		return obj instanceof byte[];
	}
	
	private static String toString(byte[] data) {
		return new String(data, UTF_8);
	}
}
