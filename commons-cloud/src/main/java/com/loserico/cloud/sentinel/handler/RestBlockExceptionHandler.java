package com.loserico.cloud.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.loserico.cloud.sentinel.enums.BlockType;
import com.loserico.common.lang.errors.ErrorTypes;
import com.loserico.common.lang.vo.Result;
import com.loserico.common.lang.vo.Results;
import com.loserico.json.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loserico.common.lang.utils.StringUtils.joinWith;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * <p>
 * Copyright: (C), 2022-08-25 18:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class RestBlockExceptionHandler implements BlockExceptionHandler {
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
		Map<String, Object> params = new HashMap<>();
		if (e instanceof FlowException) {
			log.warn("被限流啦!");
			params.put("code", BlockType.FLOW.name());
			params.put("desc", "被限流啦");
			Result result = Results.status(ErrorTypes.TOO_MANY_REQUESTS).result(params);
		} else if (e instanceof DegradeException) {
			log.warn("被熔断啦!");
			params.put("code", BlockType.DEGRADE.name());
			params.put("desc", "被熔断啦");
			Result result = Results.status(ErrorTypes.TOO_MANY_REQUESTS).result(params);
		} else if (e instanceof AuthorityException) {
			log.warn("被授权规则限制啦!");
			params.put("code", BlockType.AUTH.name());
			params.put("desc", "被授权规则限制啦");
		} else if (e instanceof SystemBlockException) {
			log.warn("被系统规则限制啦!");
			params.put("code", BlockType.SYSTEM.name());
			params.put("desc", "被系统规则限制啦");
		}
		
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		CORS.builder().allowAll().build(httpServletResponse);
		httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		System.out.println(JacksonUtils.toJson(params));
		JacksonUtils.writeValue(response.getWriter(), params);
}

private static final class CORS {
	
	public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	
	private static final Logger logger = LoggerFactory.getLogger(CORS.class);
	
	public static CorsBuilder builder() {
		return new CorsBuilder();
	}
	
	public static class CorsBuilder {
		
		private CorsBuilder() {
		}
		
		private List<String> allowedOrigins = new ArrayList<>();
		private List<String> allowedMethods = new ArrayList<>();
		private List<String> allowedHeaders = new ArrayList<>();
		
		public CorsBuilder allowedOrigins(String... origins) {
			for (int i = 0; i < origins.length; i++) {
				String origin = origins[i];
				if (isNotBlank(origin)) {
					this.allowedOrigins.add(trim(origin));
				} else {
					logger.info("给定的Origin为空，忽略之");
				}
			}
			return this;
		}
		
		public CorsBuilder allowedMethods(String... methods) {
			for (int i = 0; i < methods.length; i++) {
				String method = methods[i];
				if (isNotBlank(method)) {
					this.allowedMethods.add(trim(method));
				} else {
					logger.info("给定的Mehtod为空，忽略之");
				}
			}
			return this;
		}
		
		public CorsBuilder allowedHeaders(String... headers) {
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				if (isNotBlank(header)) {
					this.allowedHeaders.add(trim(header));
				} else {
					logger.info("给定的Header为空，忽略之");
				}
			}
			return this;
		}
		
		public CorsBuilder allowAll() {
			this.allowedHeaders.clear();
			this.allowedHeaders.add("*");
			
			this.allowedMethods.clear();
			this.allowedMethods.add("*");
			
			this.allowedOrigins.clear();
			this.allowedOrigins.add("*");
			return this;
		}
		
		public void build(HttpServletResponse response) {
			response.setHeader(HEADER_ACCESS_CONTROL_ALLOW_HEADERS, joinWith(", ", allowedHeaders));
			response.setHeader(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, joinWith(", ", allowedOrigins));
			response.setHeader(HEADER_ACCESS_CONTROL_ALLOW_METHODS, joinWith(", ", allowedMethods));
		}
	}
}
}
