package com.loserico.encrypt;

import java.security.MessageDigest;

public class MD5Utils {

	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
			'b', 'c', 'd', 'e', 'f' };
	
	
	/**
	 * 将字节数组算出16进制MD5串
	 * @param origin 字节数组
	 * @return 16进制MD5字符串
	 */
	public static String encode16(byte[] origin) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(origin));
		} catch (Exception e) {
			//logger.error(e, e);
		}
		return resultString;
	}

	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));//若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
		}
		return resultSb.toString();
	}


	/**
	 * 转换字节为10进制字符串
	 * @param b 字节
	 * @return 10进制字符串
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return "" + hexDigits[d1] + hexDigits[d2];
	}
}
