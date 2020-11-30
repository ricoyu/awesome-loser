package com.loserico.web.utils;

import com.loserico.json.jackson.JacksonUtils;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Copyright: (C), 2020-08-12 15:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class RestUtils {
	
	/**
	 * 将结果以application/json形式写入输出流
	 * @param response
	 * @param result
	 */
	@SneakyThrows
	public static void writeJson(ServletResponse response, Object result) {
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		CORS.builder().allowAll().build(httpServletResponse);
		httpServletResponse.setStatus(HttpStatus.OK.value());
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		JacksonUtils.writeValue(response.getWriter(), result);
	}
	
	/**
	 * 将结果以application/json形式以指定的Http Status写入输出流
	 * @param response
	 * @param result
	 */
	@SneakyThrows
	public static void writeJson(ServletResponse response, HttpStatus httpStatus, Object result) {
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		CORS.builder().allowAll().build(httpServletResponse);
		httpServletResponse.setStatus(httpStatus.value());
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		JacksonUtils.writeValue(response.getWriter(), result);
	}
	
	/**
	 * 将结果以application/json形式写入输出流
	 * @param response
	 * @param result
	 */
	@SneakyThrows
	public static void writeRawJson(ServletResponse response, String result) {
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		CORS.builder().allowAll().build(httpServletResponse);
		httpServletResponse.setStatus(HttpStatus.OK.value());
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(result);
	}
	
	/**
	 * 将结果以application/json形式以指定的Http Status写入输出流
	 * @param response
	 * @param result
	 */
	@SneakyThrows
	public static void writeRawJson(ServletResponse response, HttpStatus httpStatus, String result) {
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		CORS.builder().allowAll().build(httpServletResponse);
		httpServletResponse.setStatus(httpStatus.value());
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(result);
	}
}
