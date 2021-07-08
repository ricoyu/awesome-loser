package com.loserico.networking.enums;

import com.loserico.networking.exception.InvalidHttpTypeException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;

/**
 * HTTP请求方法
 * <p>
 * Copyright: Copyright (c) 2021-03-22 16:28
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum HttpMethod {

	GET(new HttpGet()),

	POST(new HttpPost()),

	PUT(new HttpPost()),

	DELETE(new HttpDelete()),

	OPTIONS(new HttpOptions()),

	TRACE(new HttpTrace()),

	HEAD(new HttpHead()),

	PATCH(new HttpPatch());

	private HttpRequestBase httpRequestBase;

	HttpMethod(HttpRequestBase httpRequestBase) {
		this.httpRequestBase = httpRequestBase;
	}

	private HttpRequestBase getHttpRequestBase() {
		return httpRequestBase;
	}

	public static HttpRequestBase getMethod(HttpMethod method) {
		for (HttpMethod value : HttpMethod.values()) {
			if (value == method) {
				return value.getHttpRequestBase();
			}
		}
		throw new InvalidHttpTypeException("Invalid request type: " + method.toString());
	}

	@Override
	public String toString() {
		return this.name();
	}
}
