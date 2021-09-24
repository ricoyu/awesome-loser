package com.loserico.codec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Base64 加密/解密
 * <p>
 * Copyright: Copyright (c) 2019-02-14 16:24
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public final class Base64Utils {
	
	public static String encode(String source) {
		if (source == null) {
			return null;
		}
		return Base64.encodeBase64String(source.getBytes(UTF_8));
	}
	
	public static String encode(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return Base64.encodeBase64String(bytes);
	}
	
	/**
	 * 对encoded进行base64解码, 返回UTF-8编码的字符串, 解码失败返回defaultValue
	 * @param encoded
	 * @param defaultValue
	 * @return String
	 */
	public static String decode(String encoded, String defaultValue) {
		try {
			return decode(encoded);
		} catch (Throwable e) {
			log.error("Base64解码失败, {}", encoded, e);
		}
		return defaultValue;
	}
	
	/**
	 * 对encoded进行base64解码, 返回UTF-8编码的字符串
	 * @param encoded
	 * @return String
	 */
	public static String decode(String encoded) {
		if (encoded == null) {
			return null;
		}
		byte[] bytes = Base64.decodeBase64(encoded);
		return new String(bytes, UTF_8);
	}
	
	/**
	 * 对encoded进行base64解码, 返回charset编码的字符串, 解码失败返回defaultValue
	 * @param encoded
	 * @param charset
	 * @param defaultValue
	 * @return String
	 */
	public static String decode(String encoded, Charset charset, String defaultValue) {
		try {
			return decode(encoded, charset);
		} catch (Throwable e) {
			log.error("Base64解码失败, {}", encoded, e);
		}
		
		return defaultValue;
	}
	
	/**
	 * 对encoded进行base64解码, 返回charset编码的字符串
	 * @param encoded
	 * @param charset
	 * @return String
	 */
	public static String decode(String encoded, Charset charset) {
		if (encoded == null) {
			return null;
		}
		byte[] bytes = Base64.decodeBase64(encoded);
		return new String(bytes, charset);
	}
	
	/**
	 * Base64 有三个字符+、/和=，在 URL 里面有特殊含义，所以要被替换掉：=被省略、+替换成-，/替换成_ 。这就是 Base64URL 算法。
	 *
	 * @param source
	 * @return
	 */
	public static String urlEncode(String source) {
		if (source == null) {
			return null;
		}
		return java.util.Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(source.getBytes(UTF_8));
	}
	
	public static String urlDecode(String encoded) {
		if (encoded == null) {
			return null;
		}
		byte[] bytes = java.util.Base64.getUrlDecoder()
				.decode(encoded.getBytes(UTF_8));
		return new String(bytes, UTF_8);
	}
	
}
