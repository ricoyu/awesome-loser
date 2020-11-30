package com.loserico.codec;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.util.Objects;

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
public final class Base64Utils {
	
	public static String encode(String source) {
		Objects.requireNonNull(source, "source cannot be null");
		return Base64.encodeBase64String(source.getBytes(UTF_8));
	}
	
	public static String decode(String encoded) {
		Objects.requireNonNull(encoded, "encoded cannot be null");
		byte[] bytes = Base64.decodeBase64(encoded);
		return new String(bytes, UTF_8);
	}
	
	public static String decode(String encoded, Charset charset) {
		Objects.requireNonNull(encoded, "encoded cannot be null");
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
		Objects.requireNonNull(source, "source cannot be null");
		return java.util.Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(source.getBytes(UTF_8));
	}
	
	public static String urlDecode(String encoded) {
		Objects.requireNonNull(encoded, "encoded cannot be null");
		byte[] bytes = java.util.Base64.getUrlDecoder()
				.decode(encoded.getBytes(UTF_8));
		return new String(bytes, UTF_8);
	}
	
}
