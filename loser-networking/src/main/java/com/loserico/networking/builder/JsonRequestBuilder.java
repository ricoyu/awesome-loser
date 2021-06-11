package com.loserico.networking.builder;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2020/5/5 19:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JsonRequestBuilder extends AbstractRequestBuilder {
	
	/**
	 * 提交的json数据
	 */
	private String jsonBody;
	
	/**
	 * Basic Authentication
	 * 设置请求头: Authorization, 值为: "Basic XXXX" 形式
	 *
	 * @param username
	 * @param password
	 */
	public JsonRequestBuilder basicAuth(String username, String password) {
		super.basicAuth(username, password);
		return this;
	}
	
	/**
	 * 设置请求头: Authorization:Bearer XXXX
	 *
	 * @param token
	 * @return
	 */
	@Override
	public JsonRequestBuilder bearerAuth(String token) {
		super.bearerAuth(token);
		return this;
	}
	
	/**
	 * 设置提交的json数据, 适用POST请求
	 *
	 * @param json
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder body(String json) {
		this.jsonBody = json;
		return this;
	}
	
	/**
	 * 设置返回结果类型
	 *
	 * @param responseType
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder responseType(Class responseType) {
		this.responseType = responseType;
		return this;
	}
	
	@Override
	public JsonRequestBuilder addHeader(String headerName, Object headerValue) {
		super.addHeader(headerName, headerValue);
		return this;
	}
	
	@Override
	public JsonRequestBuilder addParam(String paramName, Object paramValue) {
		super.addParam(paramName, paramValue);
		return this;
	}
	
	@Override
	public JsonRequestBuilder addCookie(String name, String value) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return this;
	}
	
	@Override
	public JsonRequestBuilder addCookie(String name, String value, String domain, String path) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		return this;
	}
	
	@Override
	public JsonRequestBuilder onError(Consumer<Exception> errorCallback) {
		super.onError(errorCallback);
		return this;
	}
	
	@Override
	protected void addBody(HttpEntityEnclosingRequestBase request) {
		if (jsonBody != null) {
			request.setEntity(new StringEntity(jsonBody, UTF_8));
		}
	}
}
