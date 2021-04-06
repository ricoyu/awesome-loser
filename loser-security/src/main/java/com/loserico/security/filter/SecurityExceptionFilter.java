package com.loserico.security.filter;

import com.loserico.common.lang.context.ThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 在过滤器链上发生未处理的异常时, RestExceptionAdvice是处理不到的, <p>
 * 所以通过这个Filter来统一捕获, 然后通过HandlerExceptionResolver代理给RestExceptionAdvice来处理<p>
 * 这个过滤器要放到SpringSecurity过滤器链的前面, 具体要多前就自己考虑吧 ;-)
 * <p>
 * Copyright: (C), 2020-08-18 13:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SecurityExceptionFilter extends OncePerRequestFilter {
	
	public static final String ROUTE_CAUSE = "routeCause";
	
	@Autowired
	private HandlerExceptionResolver handlerExceptionResolver;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.error("", e);
			ThreadContext.put(ROUTE_CAUSE, e);
			handlerExceptionResolver.resolveException(request, response, null, e);
		}
	}
}
