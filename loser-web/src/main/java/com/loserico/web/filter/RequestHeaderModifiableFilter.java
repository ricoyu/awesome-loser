package com.loserico.web.filter;

import com.loserico.web.http.HeaderMapRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * 在Filter里面添加修改Header
 * <p>
 * Copyright: (C), 2021-05-26 11:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class RequestHeaderModifiableFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
		if (matches(req)) {
			addHeader(requestWrapper);
		}
		chain.doFilter(requestWrapper, response);
	}
	
	/**
	 * 留给子类
	 *
	 * @param request
	 * @return boolean
	 */
	public abstract boolean matches(HttpServletRequest request);
	
	/**
	 * 添加或者修改Header
	 *
	 * @param request
	 */
	public abstract void addHeader(HeaderMapRequestWrapper request);
}
