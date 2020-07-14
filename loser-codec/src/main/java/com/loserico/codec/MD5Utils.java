package com.loserico.codec;

import org.apache.commons.codec.digest.DigestUtils;

public final class MD5Utils {

	/**
	 * 使用UTF-8编码进行MD5加密
	 * @param source
	 * @return
	 */
	public static String md5Hex(String source) {
		return DigestUtils.md5Hex(source);
	}
}
