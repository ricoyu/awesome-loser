package com.loserico.zookeeper.utils;

/**
 * 字符串操作帮助类
 * <p>
 * Copyright: Copyright (c) 2019-04-08 16:12
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public final class StringUtils {

	public static boolean isBlank(String str) {
		if (str == null) {
			return true;
		}

		return "".equals(str.trim());
	}

	public static boolean isNotBlank(String str) {
		return str != null && !"".equals(str.trim());
	}
}
