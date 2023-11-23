package com.loserico.codec;

import java.math.BigInteger;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 各种进制之间转换工具
 * <p>
 * Copyright: Copyright (c) 2019-05-12 17:13
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RedixUtils {
	
	private static final char[] HEX_CODE = "0123456789abcdef".toCharArray();
	private static final String HEX_STR = "0123456789ABCDEF";
	private static final String HEX_PREFIX = "0x";
	
	/**
	 * byte[]转16进制字符串
	 *
	 * @param data
	 * @return String
	 */
	public static String bytes2Hex(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(HEX_CODE[(b >> 4) & 0xF]);
			r.append(HEX_CODE[(b & 0xF)]);
		}
		return r.toString();
	}
	
	/**
	 * 16进制字符串转十进制long
	 *
	 * @param s
	 * @return long
	 */
	public static long hex2Decimal(String s) {
		s = preCheck(s);
		
		long value = 0L;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = HEX_STR.indexOf(c);
			value = 16 * value + d;
		}
		
		return value;
	}
	
	/**
	 * int 转 byte[]
	 *
	 * @param value
	 * @return byte[]
	 */
	public static byte[] int2ByteArray(int value) {
		return new byte[]{
				/*
				 * Java 里int占8个字节, 无符号右移24位, 最高8位就移动到低8位上, 前面高位都补0, 
				 * 强转byte会把前面高位的0都去掉, 只保留低8位, 这样就得到了最高8位的值 
				 */
				(byte) (value >>> 24),
				//强转byte就是只保留最低8位, 最高24位都被抹掉了
				(byte) (value >>> 16),
				(byte) (value >>> 8),
				//最低8位不需要移动, 直接强转就行
				(byte) value};
	}
	
	/**
	 * long 转 byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] long2ByteArray(long value) {
		return new byte[]{
				(byte) (value >>> 56),
				(byte) (value >>> 48),
				(byte) (value >>> 40),
				(byte) (value >>> 32),
				(byte) (value >>> 24),
				(byte) (value >>> 16),
				(byte) (value >>> 8),
				(byte) value};
	}
	
	/**
	 * 二进制字符串转int
	 * @param binaryStr
	 * @return int
	 */
	public static int binary2Int(String binaryStr) {
		return Integer.parseInt(binaryStr, 2);
	}
	
	/**
	 * 十六进制转二进制字符串
	 * @param hex
	 * @return String
	 */
	public static String hex2BinaryStr(String hex) {
		hex = preCheck(hex);
		return new BigInteger(hex, 16).toString(2);
	}
	
	/**
	 * int型转二进制字符串
	 *
	 * @param num
	 * @return String
	 */
	public static String int2BinaryStr(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 31; i >= 0; i--) {
			sb.append((num & (1 << i)) == 0 ? "0" : "1");
		}
		return sb.toString();
	}
	
	/**
	 * long转二进制字符串
	 *
	 * @param l
	 * @return String
	 */
	public static String long2BinaryStr(long l) {
		return Long.toBinaryString(l);
	}
	
	/**
	 * char转二进制字符串
	 * @param c
	 * @return String
	 */
	public static String charToBinaryStr(char c) {
		StringBuilder sb = new StringBuilder();
		for (int i = 13; i >= 0; i--) {
			// 使用位移和按位与操作来检查每一位
			sb.append((c >> i) & 1);
		}
		return sb.toString();
	}
	
	/**
	 * 用字符串的形式表示一个byte的二进制形式
	 * @param b
	 * @return String
	 */
	public static String byte2BinaryStr(byte b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 7; i >= 0; i--) {
			// 使用位移和按位与操作来检查每一位
			sb.append((b >> i) & 1);
		}
		return sb.toString();
	}
	
	/**
	 * 用字符串的形式表示byte数组的二进制形式
	 * @param bytes
	 * @return String
	 */
	public static String bytes2BinaryStr(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			for (int j = 7; j >= 0; j--) {
				// 使用位移和按位与操作来检查每一位
				sb.append((b >> j) & 1);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 打印byte的二进制形式
	 *
	 * @param b 一个字节
	 */
	public static void print(byte b) {
		for (int i = 7; i >= 0; i--) {
			// 使用位移和按位与操作来检查每一位
			System.out.print((b >> i) & 1);
		}
		System.out.println();  // 换行
	}
	
	/**
	 * 打印byte数组的二进制形式
	 *
	 * @param bytes 字节数组
	 */
	public static void print(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			for (int i = 7; i >= 0; i--) {
				// 使用位移和按位与操作来检查每一位
				sb.append((b >> i) & 1);
			}
		}
		System.out.println(sb.toString());
	}
	
	/**
	 * int型转16进制, 不带0x前缀
	 *
	 * @param i
	 * @return String
	 */
	public static String int2Hex(Integer i) {
		if (i == null) {
			return null;
		}
		
		return Integer.toHexString(i);
	}
	
	/**
	 * 16进制字符串转整型
	 *
	 * @param hex
	 * @return Integer
	 */
	public static Integer hex2Int(String hex) {
		if (isBlank(hex)) {
			return null;
		}
		
		if (hex.startsWith(HEX_PREFIX)) {
			hex = hex.substring(2);
		}
		return Integer.parseInt(hex, 16);
	}

	
	/**
	 * 判断val是不是2的n次幂, 如 2, 4, 8, 16...
	 *
	 * <pre> {@code
	 * 16  0001 0000
	 * -16 1111 0000
	 * }</pre>
	 * 位与后还是0001 0000, 所以2的n次幂就有这个特性 <p/>
	 *
	 * @param val
	 * @return boolean
	 */
	public static boolean isPowerOfTwo(int val) {
		return (val & -val) == val;
	}
	
	/**
	 * 对hex预处理, null检查, 空字符串检查, 两边trim, 去掉开头的0x, 返回处理后的字符串
	 *
	 * @param hex
	 * @return String
	 */
	private static String preCheck(String hex) {
		if (isBlank(hex)) {
			throw new IllegalArgumentException("Can not be blank!");
		}
		
		hex = hex.trim().toUpperCase();
		if (hex.indexOf("0X") != -1) {
			hex = hex.substring(2);
		}
		return hex;
	}
}
