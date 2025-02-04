package com.loserico.security.endpoint;

import com.loserico.common.lang.vo.Result;
import com.loserico.common.lang.vo.Results;
import com.loserico.web.utils.RestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.loserico.common.lang.errors.ErrorTypes.TOKEN_EXPIRED;

/**
 * 处理未认证访问的端点 
 * <p>
 * Copyright: Copyright (c) 2021-03-29 17:04
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger log = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
	
	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		
		Result result = Results.status(TOKEN_EXPIRED).build();
		RestUtils.writeJson(response, result);
	}

}
