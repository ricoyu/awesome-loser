package com.loserico.common.lang.utils;

import java.math.BigInteger;

/**
 * 各种进制之间转换工具
 * <p>
 * Copyright: Copyright (c) 2019-05-12 17:13
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class RedixUtils {

	private static final char[] HEX_CODE = "0123456789abcdef".toCharArray();
	private static final String HEX_STR = "0123456789ABCDEF";

	/**
	 * byte[]转16进制字符串
	 * 
	 * @param data
	 * @return String
	 */
	public static String bytesToHex(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(HEX_CODE[(b >> 4) & 0xF]);
			r.append(HEX_CODE[(b & 0xF)]);
		}
		return r.toString();
	}

	/**
	 * 16进制字符串转二进制字符串形式
	 * 
	 * @param s
	 * @return String
	 */
	public static long hexToDecimal(String s) {
		s = preCheck(s);
		
		long value = 0L;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = HEX_STR.indexOf(c);
			value = 16 * value + d;
		}

		return value;
	}
	
	public static String hexToBinary(String hex) {
		hex = preCheck(hex);
		return new BigInteger(hex, 16).toString(2);
	}
	
	/**
	 * int型转二进制字符串
	 * @param i
	 * @return String
	 */
	public static String intToBinary(int i) {
		return Integer.toBinaryString(i);
	}
	
	/**
	 * long转二进制字符串
	 * @param l
	 * @return String
	 */
	public static String longToBinary(long l) {
		return Long.toBinaryString(l);
	}
	
	/**
	 * 对hex预处理, null检查, 空字符串检查, 两边trim, 去掉开头的0x
	 * @param hex
	 * @return String
	 */
	private static String preCheck(String hex) {
		if (hex == null || hex.trim().equals("")) {
			throw new NullPointerException();
		}
		
		hex = hex.trim().toUpperCase();
		if (hex.indexOf("0X") != -1) {
			hex = hex.substring(2);
		}
		return hex;
	}
}
