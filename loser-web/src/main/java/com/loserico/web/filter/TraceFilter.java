package com.loserico.web.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * <p>
 * Copyright: (C), 2020/1/2 19:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Component
@WebFilter("/*")
public class TraceFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	                     FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 从请求头中获取traceId
		String traceId = ((HttpServletRequest) request).getHeader("TRACE_ID");
		// 不存在就生成一个
		if (null == traceId || "".equalsIgnoreCase(traceId.trim())) {
			traceId = UUID.randomUUID().toString().replaceAll("-", "");
		}
		MDC.put("traceId", traceId);
		chain.doFilter(request, response);
	}
}
