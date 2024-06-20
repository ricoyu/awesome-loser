package com.loserico.web.filter;

import com.loserico.web.http.RepeatedReadHttpServletRequestWarpper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * <p>
 * Copyright: (C), 2020-09-08 14:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HttpServletRequestRepeatedReadFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			requestWrapper = new RepeatedReadHttpServletRequestWarpper((HttpServletRequest) request);
		}
		
		//获取请求中的流, 将取出来的字符串, 再次转换成流, 然后把它放入到新request对象中
		if (requestWrapper == null) {
			chain.doFilter(request, response);
		} else {
			chain.doFilter(requestWrapper, response);
		}
	}
}
