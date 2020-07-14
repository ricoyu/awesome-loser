package com.loserico.common.spring.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * <p>
 * Copyright: (C), 2020/5/1 11:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class Requests {
	
	/**
	 * 表单提交, 默认POST请求, Content-Type为application/x-www-form-urlencoded
	 *
	 * @param url
	 * @return FormRequestBuilder
	 */
	public static FormRequestBuilder form(String url) {
		FormRequestBuilder requestBuilder = new FormRequestBuilder();
		requestBuilder.setUrl(url);
		requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		return requestBuilder;
	}
	
	/**
	 * REST请求, request body 发送 json数据
	 * @param url
	 * @return JsonRequestBuilder
	 */
	public static JsonRequestBuilder post(String url) {
		JsonRequestBuilder requestBuilder = new JsonRequestBuilder();
		requestBuilder.setUrl(url);
		requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return requestBuilder;
	}
	
	/**
	 * REST请求
	 * @param url
	 * @return JsonRequestBuilder
	 */
	public static JsonRequestBuilder get(String url) {
		JsonRequestBuilder requestBuilder = new JsonRequestBuilder();
		requestBuilder.setHttpMethod(HttpMethod.GET);
		requestBuilder.setUrl(url);
		requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return requestBuilder;
	}
}
