package com.loserico.security.endpoint;

import com.loserico.common.lang.vo.Result;
import com.loserico.common.lang.vo.Results;
import com.loserico.web.utils.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.loserico.common.lang.errors.ErrorTypes.*;

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
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		
		Result result = Results.status(TOKEN_EXPIRED).build();
		RestUtils.writeJson(response, result);
	}
}
