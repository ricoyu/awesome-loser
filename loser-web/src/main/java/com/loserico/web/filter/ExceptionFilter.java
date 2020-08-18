package com.loserico.web.filter;

import com.loserico.common.lang.context.ThreadContext;
import com.loserico.common.lang.vo.Results;
import com.loserico.web.utils.RestUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static com.loserico.common.lang.vo.CommonErrorType.INTERNAL_SERVER_ERROR;

/**
 * 在过滤器链上发生未处理的异常时, RestExceptionAdvice是处理不到的, 所以通过这个Filter来统一处理
 * <p>
 * Copyright: (C), 2020-08-18 13:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ExceptionFilter implements Filter {
	
	public static final String ROUTE_CAUSE = "routeCause";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Throwable e) {
			ThreadContext.put(ROUTE_CAUSE, e);
			RestUtils.writeJson(response, HttpStatus.INTERNAL_SERVER_ERROR, Results.status(INTERNAL_SERVER_ERROR).build());
		}
	}
}
