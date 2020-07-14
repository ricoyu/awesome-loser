package com.loserico.security.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Copyright: (C), 2020/5/23 15:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class OAuthUtils {
	/**
	 * Authentication请求头的前缀部分, bearer+一个空格
	 */
	public static final String AUTH_HEADER_PREFIX = "Bearer ";
	
	private static final String AUTH_HEADER_PREFIX_LOWER = "bearer ";
	
	/**
	 * Authorization请求头的值类似如下格式:
	 * Bearer fb91227e-da4b-4ba4-a5f9-ecd820093479
	 * 获取"Bearer "后面的部分
	 *
	 * @param authorizationHeader
	 * @return String
	 */
	public static String bearToken(String authorizationHeader) {
		if (isBlank(authorizationHeader)) {
			return null;
		}
		
		String token = StringUtils.substringAfter(authorizationHeader, AUTH_HEADER_PREFIX);
		if (isBlank(token)) {
			token = StringUtils.substringAfter(authorizationHeader, AUTH_HEADER_PREFIX_LOWER); 
		}
		
		if (!isBlank(token)) {
			return token.trim();
		}
		
		return null;
	}
	
	/**
	 * 判断字符串是null或者空字符串"", 或者仅包含空格
	 * @param s
	 * @return boolean
	 */
	private static boolean isBlank(String s) {
		return null == s || s.trim().equals("");
	}
}
