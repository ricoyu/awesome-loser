package com.loserico.web.filter;

import com.loserico.web.http.XssAndSqlHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 注册到该bean到Spring容器中即可
 * <p>
 * Copyright: (C), 2020-7-22 0022 10:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class XssFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("XssFilter inited");
	}
	
	@Override
	public void destroy() {
		log.info("XssFilter destory");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		XssAndSqlHttpServletRequestWrapper wrapper = new XssAndSqlHttpServletRequestWrapper(httpServletRequest);
		chain.doFilter(wrapper, response);
	}
}
