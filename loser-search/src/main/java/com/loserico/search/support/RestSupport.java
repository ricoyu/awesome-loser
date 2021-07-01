package com.loserico.search.support;

import com.loserico.common.lang.resource.PropertyReader;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2021-06-30 11:41
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class RestSupport {
	
	private static final String COLON = ":";
	
	/**
	 * 组合成 http://192.168.100.101:9200 这种形式
	 */
	public static final String HOST;
	
	static {
		PropertyReader reader = new PropertyReader("elastic");
		String hosts = reader.getString("elastic.rest.hosts");
		if (isBlank(hosts)) {
			hosts = "localhost:9200";
		} else if (!hosts.contains(COLON)) {
			hosts = hosts + COLON + "9200"; //每页包含端口号就把默认的9200 端口拼接上去
		}
		
		if (!hosts.startsWith("http")) {
			hosts = "http://" + hosts;
		}
		HOST = hosts;
	}
}
