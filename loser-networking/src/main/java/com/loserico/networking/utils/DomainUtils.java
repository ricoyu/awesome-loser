package com.loserico.networking.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 域名处理
 * <p>
 * Copyright: (C), 2021-09-13 14:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DomainUtils {
	
	// 一级域名提取
	private static final String REGEX_TOP_1 = "(\\w*\\.?){1}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";
	
	// 二级域名提取
	private static final String REGEX_TOP_2 = "(\\w*\\.?){2}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";
	
	// 三级域名提取
	private static final String REGEX_TOP_3 = "(\\w*\\.?){3}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";
	
	private static final Pattern PATTEN_TOP1 = Pattern.compile(REGEX_TOP_1);
	private static final Pattern PATTEN_TOP2 = Pattern.compile(REGEX_TOP_2);
	private static final Pattern PATTEN_TOP3 = Pattern.compile(REGEX_TOP_3);
	
	/**
	 * 获取一级, 二级, 三级域名
	 * @param url
	 * @param level
	 * @return String
	 */
	public static String getDomain(String url, int level) {
		if (isBlank(url)) {
			return null;
		}
		Matcher matcher = null;
		
		switch (level) {
			case 1:
				matcher = PATTEN_TOP1.matcher(url);
				break;
			case 2:
				matcher = PATTEN_TOP2.matcher(url);
				break;
			case 3:
				matcher = PATTEN_TOP3.matcher(url);
				break;
			default:
				return "";
		}
		if (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}
}
