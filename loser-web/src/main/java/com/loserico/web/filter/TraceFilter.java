package com.loserico.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

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
	
	private static final int MAX_TRACE_ID = 1000000;
	private static volatile int APP_ID = 100 * MAX_TRACE_ID;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 从请求头中获取traceId
		String traceId = ((HttpServletRequest) request).getHeader("TRACE_ID");
		// 不存在就生成一个
		if (null == traceId || "".equalsIgnoreCase(traceId.trim())) {
			traceId = String.valueOf(APP_ID + Math.abs(new Random().nextInt(MAX_TRACE_ID)));
		}
		MDC.put("traceId", traceId);
		/*
		 * 然后在logback-spring.xml里面配置一下输出traceId， 这样log.info("xxx")的输出中就会带traceId了
		 * <property name="CONSOLE_LOG_PATTERN" value="${CONSOLE_LOG_PATTERN:-%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }) --- [%t] [%X{traceId}] {magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} L%-4L %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
         * <property name="FILE_LOG_PATTERN" value="${FILE_LOG_PATTERN:-%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] [%X{traceId}] %-40.40logger{39} L%-4L : %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
		 */
		chain.doFilter(request, response);
	}
}
