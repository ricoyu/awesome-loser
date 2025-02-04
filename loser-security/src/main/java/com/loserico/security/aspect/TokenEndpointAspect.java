package com.loserico.security.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 请求/oauth/**出错但是不打印error问题解决
 * <p>
 * Copyright: (C), 2022-10-18 15:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Aspect
@Component
public class TokenEndpointAspect {
	private static final Logger log = LoggerFactory.getLogger(TokenEndpointAspect.class);
	
	@Pointcut("execution(public * org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.handleException(*))")
	public void handleException(){}
	
	@Before("handleException()")
	public void logBeforeErrorhanding(JoinPoint joinPoint) {
		Throwable e = (Throwable) joinPoint.getArgs()[0];
		log.error("", e);
	}
}
