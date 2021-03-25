package com.loserico.security.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;

/**
 * <p>
 * Copyright: (C), 2021-02-22 14:13
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class XSSUtils {
	
	/**
	 * 
	 * @param value
	 * @return String
	 */
	public static String stripXSS(String value) {
		if (value == null) {
			return null;
		}
		value = ESAPI.encoder()
				.canonicalize(value)
				.replaceAll("\0", "");
		return Jsoup.clean(value, Whitelist.none());
	}
}
