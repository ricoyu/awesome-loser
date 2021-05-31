package com.loserico.security.filter;

import com.loserico.codec.RsaUtils;
import com.loserico.common.lang.context.ThreadContext;
import com.loserico.common.spring.utils.ServletUtils;
import com.loserico.security.constants.LoserSecurityConstants;
import com.loserico.security.vo.LoginRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.loserico.json.jackson.JacksonUtils.toObject;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 先尝试表单提交认证、接着是request body认证
 * <p>
 * Copyright: Copyright (c) 2018-07-23 15:44
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
@Data
public class UsernamePasswordAuthenticationFilter extends org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter {
	
	private static final String ERROR_MESSAGE = "Something went wrong while parsing /login request body";
	
	private boolean rsaEncrypted;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		Authentication token = null;
		
		// 1 尝试表单提交方式获取用户名密码
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		if (isNotBlank(username) && isNotBlank(password)) {
			if (rsaEncrypted) {
				password = RsaUtils.privateDecrypt(password, data -> log.warn("RSA 私钥解密 {} 失败!", data));
			}
			token = new UsernamePasswordAuthenticationToken(username, password);
			ThreadContext.put(LoserSecurityConstants.SPRING_SECURITY_FORM_USERNAME_KEY, username);
		}
		
		//从request body 中获取用户名密码
		if (token != null) {
			return this.getAuthenticationManager().authenticate(token);
		}
		
		String requestBody = ServletUtils.readRequestBody(request);
		if (isBlank(requestBody)) {
			token = new UsernamePasswordAuthenticationToken("", "");
			return this.getAuthenticationManager().authenticate(token);
		}
		
		LoginRequest authRequest = toObject(requestBody, LoginRequest.class);
		if (isNotBlank(authRequest.getUsername()) && isNotBlank(authRequest.getPassword())) {
			if (rsaEncrypted && isNotBlank(authRequest.getPassword())) {
				password = RsaUtils.privateDecrypt(authRequest.getPassword(), data -> log.warn("RSA 私钥解密 {} 失败!", data));
				authRequest.setPassword(password);
			}
			token = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
			ThreadContext.put(LoserSecurityConstants.SPRING_SECURITY_FORM_USERNAME_KEY, username);
			return this.getAuthenticationManager().authenticate(token);
		}
		
		token = new UsernamePasswordAuthenticationToken("", "");
		return this.getAuthenticationManager().authenticate(token);
	}
	
}
