package com.loserico.security.filter;

import com.loserico.security.http.XSSRequestWrapper;
import com.loserico.security.utils.XSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
public class XSSFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("XSSFilter inited");
	}
	
	@Override
	public void destroy() {
		log.info("XSSFilter destory");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		XSSRequestWrapper wrappedRequest = new XSSRequestWrapper((HttpServletRequest) request);
		
		String body = IOUtils.toString(wrappedRequest.getReader());
		if (isNotBlank(body)) {
			body = XSSUtils.stripXSS(body);
			wrappedRequest.resetInputStream(body.getBytes());
		}
		
		chain.doFilter(wrappedRequest, response);
	}
}
