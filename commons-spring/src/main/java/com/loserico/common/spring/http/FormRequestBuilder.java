package com.loserico.common.spring.http;

import com.loserico.common.lang.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.function.Consumer;

/**
 * 模拟表单提交
 * <p>
 * Copyright: Copyright (c) 2020-05-13 19:48
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class FormRequestBuilder extends AbstractRequestBuilder implements OAuth2Support {
	
	/**
	 * Basic Authentication
	 * 设置请求头: Authorization, 值为: "Basic XXXX" 形式
	 *
	 * @param username
	 * @param password
	 */
	public FormRequestBuilder basicAuth(String username, String password) {
		super.basicAuth(username, password);
		return this;
	}
	
	@Override
	public FormRequestBuilder bearerAuth(String token) {
		super.bearerAuth(token);
		return this;
	}
	
	@Override
	public FormRequestBuilder addHeader(String headerName, String headerValue) {
		super.addHeader(headerName, headerValue);
		return this;
	}
	
	/**
	 * 设置请求参数
	 *
	 * @param paramName
	 * @param paramValue
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder param(String paramName, String paramValue) {
		Assert.notNull(paramName, "Param name cannot be null");
		params.add(paramName, paramValue);
		return this;
	}
	
	/**
	 * 设置username请求参数
	 *
	 * @param username
	 * @return
	 */
	public FormRequestBuilder username(String username) {
		return param("username", username);
	}
	
	/**
	 * 设置password请求参数
	 *
	 * @param password
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder password(String password) {
		return param("password", password);
	}
	
	/**
	 * 同时设置username和password
	 *
	 * @param username
	 * @param password
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder userpwd(String username, String password) {
		username(username);
		password(password);
		return this;
	}
	
	/**
	 * 设置OAuth2的grant_type参数
	 *
	 * @param grantType
	 * @return OAuth2Support
	 */
	@Override
	public FormRequestBuilder grantType(GrantType grantType) {
		Assert.notNull(grantType, "grantType cannot be null");
		params.add("grant_type", grantType.toString());
		return this;
	}
	
	/**
	 * 设置OAuth2的scope参数
	 *
	 * @param scope
	 * @return FormRequestBuilder
	 */
	@Override
	public FormRequestBuilder scope(Scope scope) {
		Assert.notNull(scope, "scope cannot be null");
		params.add("scope", scope.toString());
		return this;
	}
	
	/**
	 * 设置返回结果类型
	 *
	 * @param responseType
	 * @return FormRequestBuilder
	 */
	public FormRequestBuilder responseType(Class responseType) {
		this.responseType = responseType;
		return this;
	}
	
	@Override
	public FormRequestBuilder onError(Consumer<Exception> errorCallback) {
		super.onError(errorCallback);
		return this;
	}
	
	/**
	 * 发送HTTP请求, 返回结果
	 *
	 * @return T
	 */
	@Override
	public <T> T request() {
		return request(null);
	}
	
	@Override
	public <T> T request(RestTemplate restTemplate) {
		if (restTemplate == null) {
			restTemplate = ApplicationContextHolder.getBean(RestTemplate.class);
		}
		try {
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
			ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, entity, responseType);
			return responseEntity.getBody();
		} catch (Exception e) {
			log.error("", e);
			if (errorCallback != null) {
				errorCallback.accept(e);
			} else {
				throw e;
			}
		}
		
		return null;
	}
}
