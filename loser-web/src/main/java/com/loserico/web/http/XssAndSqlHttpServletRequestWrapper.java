package com.loserico.web.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * getParameter方法是直接通过request获得querystring类型的入参调用的方法
 * 如果是通过springMVC注解类型来获得参数的话, 走的是getParameterValues的方法
 *
 * <p>
 * Copyright: (C), 2020-7-22 0022 9:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class XssAndSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {
	
	private HttpServletRequest request;
	
	public XssAndSqlHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (isNotEmpty(name)) {
			return StringEscapeUtils.escapeHtml4(value);
		}
		
		return value;
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null || values.length == 0) {
			return values;
		}
		
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (isNotEmpty(value)) {
				values[i] = StringEscapeUtils.escapeHtml4(value);
			}
		}
		return values;
	}
	
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (isNotEmpty(value)) {
			return StringEscapeUtils.escapeHtml4(value);
		}
		
		return value;
	}
	
	@Override
	public Enumeration<String> getHeaders(String name) {
		Enumeration<String> values = super.getHeaders(name);
		List<String> headerValues = new ArrayList<>();
		while (values.hasMoreElements()) {
			String value = values.nextElement();
			if (isNotEmpty(value)) {
				headerValues.add(StringEscapeUtils.escapeHtml4(value));
			} else {
				headerValues.add(value);
			}
		}
		return Collections.enumeration(headerValues);
	}
}
