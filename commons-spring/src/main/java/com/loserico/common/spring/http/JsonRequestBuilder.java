package com.loserico.common.spring.http;

import com.loserico.common.lang.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.function.Consumer;

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
	 * 返回的数据类型
	 */
	private ParameterizedTypeReference typeReference;
	
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
	 * 设置提交的json数据
	 *
	 * @param json
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder body(String json) {
		this.jsonBody = json;
		return this;
	}
	
	/**
	 * 设置返回的结果类型
	 *
	 * @param typeReference
	 * @return JsonRequestBuilder
	 */
	public JsonRequestBuilder typeReference(ParameterizedTypeReference typeReference) {
		this.typeReference = typeReference;
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
	public JsonRequestBuilder onError(Consumer<Exception> errorCallback) {
		super.onError(errorCallback);
		return this;
	}
	
	/**
	 * 自动从Spring容器中获取RestTemplate发送HTTP请求, 返回结果
	 *
	 * @return T
	 */
	public <T> T request() {
		return request(null);
	}
	
	/**
	 * 用指定的RestTemplate发送HTTP请求, 返回结果
	 *
	 * @return T
	 */
	public <T> T request(RestTemplate restTemplate) {
		if (restTemplate == null) {
			restTemplate = ApplicationContextHolder.getBean(RestTemplate.class);
		}
		HttpEntity<MultiValueMap<String, String>> entity = null;
		switch (httpMethod) {
			case GET:
				entity = new HttpEntity(null, headers);
				break;
			case POST:
				entity = new HttpEntity(jsonBody, headers);
				break;
			default:
				entity = new HttpEntity(null, headers);
		}
		
		try {
			ResponseEntity<T> responseEntity = null;
			if (typeReference != null) {
				responseEntity = restTemplate.exchange(url, httpMethod, entity, typeReference);
			} else if (responseType != null) {
				responseEntity = restTemplate.exchange(url, httpMethod, entity, responseType);
			}
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
