package com.loserico.common.spring.interceptor;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * 在每个HTTP请求上添加一个TRACE_ID头
 * <p>
 * RestTemplate需要把这个Interceptor配置进去, 示例:
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 * @Bean public RestTemplate restTemplate() {
 * RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
 * restTemplate.setInterceptors(Collections.singletonList(new TraceIdHttpRequestInterceptor()));
 * return restTemplate;
 * }
 *
 * <p>
 * Copyright: (C), 2020/1/3 14:26
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 */
public class TraceIdHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
	                                    ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		/*
		 * 把traceId塞到请求头里面, 使下游服务可以获取到
		 * With this configuration, any requests you make through the RestTemplate will automatically carry the desired HTTP request header.
		 */
		headers.add("TRACE_ID", MDC.get("traceId"));
		return execution.execute(request, body);
	}
}
