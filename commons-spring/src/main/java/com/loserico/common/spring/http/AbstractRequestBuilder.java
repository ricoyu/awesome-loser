package com.loserico.common.spring.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Consumer;

public abstract class AbstractRequestBuilder {
	
	protected Charset UTF8 = StandardCharsets.UTF_8;
	
	protected String url;
	
	protected HttpHeaders headers = new HttpHeaders();
	
	protected HttpMethod httpMethod = HttpMethod.POST;
	
	protected MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	
	protected Class responseType;
	
	/**
	 * HTTP请求报错时回调函数
	 */
	protected Consumer<Exception> errorCallback;
	
	public AbstractRequestBuilder addHeader(String headerName, String headerValue) {
		Assert.notNull(headerName, "Header name cannot be null");
		Assert.notNull(headerValue, "Header value cannot be null");
		headers.add(headerName, headerValue);
		return this;
	}
	
	/**
	 * Basic Authentication
	 * 设置请求头: Authorization, 值为: "Basic XXXX" 形式
	 *
	 * @param username
	 * @param password
	 */
	protected AbstractRequestBuilder basicAuth(String username, String password) {
		Assert.notNull(username, "Username must not be null");
		Assert.notNull(password, "Password must not be null");
		
		CharsetEncoder encoder = UTF8.newEncoder();
		if (!encoder.canEncode(username) || !encoder.canEncode(password)) {
			throw new IllegalArgumentException(
					"Username or password contains characters that cannot be encoded to " + UTF8.displayName());
		}
		
		String credentialsString = username + ":" + password;
		byte[] encodedBytes = Base64.getEncoder().encode(credentialsString.getBytes(UTF8));
		String encodedCredentials = new String(encodedBytes, UTF8);
		headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);
		
		return this;
	}
	
	/**
	 * Bearer Token Authentication
	 * 设置请求头: Authorization, 值为: "Bearer XXXX" 形式
	 *
	 * @param token
	 */
	protected AbstractRequestBuilder bearerAuth(String token) {
		Assert.notNull(token, "token must not be null");
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		return this;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	protected AbstractRequestBuilder onError(Consumer<Exception> errorCallback) {
		this.errorCallback = errorCallback;
		return this;
	}
}