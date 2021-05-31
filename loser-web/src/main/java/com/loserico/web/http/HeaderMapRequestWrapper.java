package com.loserico.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过Filter添加/修改 Header
 * https://stackoverflow.com/questions/2811769/adding-an-http-header-to-the-request-in-a-servlet-filter
 * http://sandeepmore.com/blog/2010/06/12/modifying-http-headers-using-java/
 * http://bijubnair.blogspot.de/2008/12/adding-header-information-to-existing.html
 * <p>
 * Copyright: Copyright (c) 2021-05-26 11:21
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
	
	public HeaderMapRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	private Map<String, String> headerMap = new HashMap<String, String>();
	
	/**
	 * 添加Header
	 *
	 * @param name
	 * @param value
	 */
	public void addHeader(String name, String value) {
		headerMap.put(name, value);
	}
	
	@Override
	public String getHeader(String name) {
		String headerValue = super.getHeader(name);
		if (headerMap.containsKey(name)) {
			headerValue = headerMap.get(name);
		}
		return headerValue;
	}
	
	/**
	 * 返回所有Header的名字
	 *
	 * @return
	 */
	@Override
	public Enumeration<String> getHeaderNames() {
		List<String> names = Collections.list(super.getHeaderNames());
		for (String name : headerMap.keySet()) {
			names.add(name);
		}
		return Collections.enumeration(names);
	}
	
	@Override
	public Enumeration<String> getHeaders(String name) {
		List<String> values = Collections.list(super.getHeaders(name));
		if (headerMap.containsKey(name)) {
			values.add(headerMap.get(name));
		}
		return Collections.enumeration(values);
	}
	
}
