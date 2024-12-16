package com.loserico.search.support;

import com.loserico.common.lang.resource.PropertyReader;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
	
	private static final String COMMA = ",";
	
	/**
	 * HOSTS里面最终存的是 http://192.168.100.101:9200 这种形式得URL
	 */
	public static final List<String> HOSTS = new ArrayList<>();
	
	static {
		PropertyReader reader = new PropertyReader("elastic");
		String hosts = reader.getString("elastic.rest.hosts");
		if (isBlank(hosts)) {
			HOSTS.add("localhost:9200");
		} 
		
		//192.168.100.101:9200,192.168.100.102:9200这种形式
		if (hosts.contains(COMMA)) {
			String[] host_arr = StringUtils.split(hosts, COMMA);
			for (int i = 0; i < host_arr.length; i++) {
				String host = host_arr[i].trim();
				//只指定了IP, 没有指定端口号的情况
				if (!host.contains(COLON)) {
					host = host + COLON + "9200"; //每页包含端口号就把默认的9200 端口拼接上去
				}
				HOSTS.add(host);
			}
		} 
		//192.168.100.101:9200 只有单个IP的形式
		else {
			if (!hosts.contains(COLON)) {
				hosts = hosts + COLON + "9200"; //每页包含端口号就把默认的9200 端口拼接上去
			}
			HOSTS.add(hosts);
		}
		
		List<String> tempHosts = new ArrayList<>();
		tempHosts.addAll(HOSTS);
		for (int i = 0; i < tempHosts.size(); i++) {
			String host = tempHosts.get(i);
			if (!host.startsWith("http")) {
				host = "http://" + host;
				HOSTS.set(i, host);
			}
		}
	}
}
