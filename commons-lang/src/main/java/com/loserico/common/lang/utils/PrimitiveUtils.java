package com.loserico.common.lang.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 原子操作帮助类
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:47
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
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
	 *
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
			return ((Byte) value).intValue();
		}
		return 0;
	}
	
	public static int toInt(byte[] data) {
		if (data == null || data.length == 0) {
			return 0;
		}
		
		return Integer.valueOf(toString(data));
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
			return ((Byte) value).toString();
		}
		if (value instanceof byte[]) {
			return new String((byte[]) value, UTF_8);
		}
		return null;
	}
	
	public static boolean isByteArray(Object obj) {
		return obj instanceof byte[];
	}
	
	private static String toString(byte[] data) {
		return new String(data, UTF_8);
	}
	
	/**
	 * 打印整数的二进制形式
	 * <p>
	 * 一个整数占4字节, 32位
	 * 1左移31位就是把1从第1位上的移到了32位上, 变成 10000000 00000000 00000000 00000000
	 * 所以一个正数(符号位0)与其位与, 得到的二进制形式就是
	 * 00000000 00000000 00000000 00000000, 即整数0, 此时输出符号位"0"
	 * 负数的话得到
	 * 10000000 00000000 00000000 00000000, 即Integer.MIN_VALUE, 不等于0, 此时输出符号位 "1"
	 * <p>
	 * 接下来1左移30位, 那么二进制形式为 01000000 00000000 00000000 00000000
	 * 任意数与其位与, 如果这个数的31位上是0, 位与得到的结果就是00000000 00000000 00000000 00000000, 即整数0, 此时输出31位 "0"
	 * 如果这个数的31为上是1, 位与得到的结果就是01000000 00000000 00000000 00000000, 不等于0, 所以此时输出31位二进制 "1"
	 * <p>
	 * 依次类推
	 *
	 * @param num
	 */
	public static void printBinary(int num) {
		for (int i = 31; i >= 0; i--) {
			System.out.print((num & (1 << i)) == 0 ? "0" : "1");
		}
		System.out.println();
	}
}
