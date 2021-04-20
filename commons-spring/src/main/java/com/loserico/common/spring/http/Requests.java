package com.loserico.common.spring.http;

import com.loserico.common.lang.context.ApplicationContextHolder;
import com.loserico.common.spring.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * 基于Spring的RestTemplate实现<p>
 * 需要配置Bean: ApplicationContextHolder<p>
 *     
 * Copyright: (C), 2020/5/1 11:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class Requests {
	
	private static final String SCHEMA_HTTP = "http";
	
	private static final String SCHEMA_HTTPS = "https";
	
	
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
	 *
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
	 *
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
	
	/**
	 * 请求转发
	 *
	 * @param request
	 * @param destAddress ip:port 或者 ip:port/contextPath
	 * @param <T>
	 * @return T
	 */
	public static <T> T transmit(HttpServletRequest request, String destAddress, Class responseType) {
		return transmit(request, destAddress, responseType, null, null);
	}
	
	/**
	 * 请求转发
	 *
	 * @param request
	 * @param destAddress ip:port 或者 ip:port/contextPath
	 * @param <T>
	 * @return T
	 */
	public static <T> T transmit(HttpServletRequest request, String destAddress, Class responseType,
	                             Map<String, String> additionalHeaders, Map<String, String> additionalParameters) {
		// 从request拿出所有的请求头, 构造HttpHeaders, 一起转发过去
		HttpHeaders headers = new HttpHeaders();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);
			headers.add(name, value);
		}
		if (additionalHeaders != null && !additionalHeaders.isEmpty()) {
			for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
				headers.add(entry.getKey(), entry.getValue());
			}
		}
		
		// 复制 request 的参数
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		Set<String> keys = parameterMap.keySet();
		for (String key : keys) {
			String[] value = parameterMap.get(key);
			params.add(key, value[0]);
		}
		if (additionalParameters != null && !additionalParameters.isEmpty()) {
			for (Map.Entry<String, String> entry : additionalParameters.entrySet()) {
				params.add(entry.getKey(), entry.getValue());
			}
		}
		
		/*
		 * x-www-form-urlencoded 没有 request body
		 * application/json 一般有request body
		 */
		String body = ServletUtils.readRequestBody(request);
		HttpEntity<Object> requestEntity = null;
		if (body != null && !body.isEmpty()) {
			requestEntity = new HttpEntity<>(body, headers);
		} else {
			requestEntity = new HttpEntity<>(params, headers);
		}
		
		/*
		 * 一个完整的url长这样, 如果配置了contextPath /device
		 * http://127.0.0.1:8080/device/pic_code?name=value&age=1
		 *
		 */
		String schema = request.getScheme(); //http or https
		String contextPath = request.getContextPath(); // /device 如果配了contextPath /device
		String servletPath = request.getServletPath(); // /pic_code
		String method = request.getMethod();
		String queryStr = request.getQueryString(); //name=value&age=1
		int port = request.getLocalPort();
		String requestURL = request.getRequestURL().toString();
		
		StringBuilder sb = new StringBuilder();
		String destUrl = sb.append(schema)
				.append("://")
				.append(destAddress)
				.append(servletPath)
				.append("?")
				.append(queryStr)
				.toString();
		
		HttpMethod httpMethod = HttpMethod.resolve(method);
		
		try {
			URI url = new URI(destUrl);
			ResponseEntity<T> responseEntity = null;
			RestTemplate restTemplate = ApplicationContextHolder.getBean(RestTemplate.class);
			
			if (responseType == null) {
				responseType = String.class;
			}
			
			responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, responseType);
			return responseEntity.getBody();
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 如果url是带schema的, 比如HTTP, HTTPS, 那么不做说明
	 * 否则补全schema部分
	 *
	 * @param url
	 * @return
	 */
	public static String fillSchema(String url, HttpServletRequest request) {
		if (url == null || request == null) {
			return url;
		}
		
		String tempUrl = url.toLowerCase();
		if (tempUrl.indexOf(SCHEMA_HTTP) == 0 || tempUrl.indexOf(SCHEMA_HTTPS) == 0) {
			return url;
		}
		
		String scheme = request.getScheme();
		return scheme + "://" + url;
	}
}
