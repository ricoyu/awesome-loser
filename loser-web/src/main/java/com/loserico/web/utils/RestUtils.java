package com.loserico.web.utils;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.web.exception.DownloadException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

import static com.loserico.common.lang.utils.Assert.notNull;

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
@Slf4j
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
	
	public static void download(Path path) {
		notNull(path, "path cannot be null!");
		File file = path.toFile();
		download(file, file.getName());
	}
	
	public static void download(File file) {
		download(file, file.getName());
	}
	
	public static void download(Path path, String filename) {
		notNull(path, "path cannot be null!");
		download(path.toFile(), filename);
	}
	
	public static void download(File file, String filename) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		
		if (response == null || request == null) {
			throw new DownloadException("仅支持Spring Web环境下载");
		}
		
		String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
		if(contentType == null) {
			contentType = "application/octet-stream";
		}
		
		CORS.builder().allowAll().build(response);
		response.setStatus(HttpStatus.OK.value());
		response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
		try {
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("", e);
			throw new DownloadException("下载文件失败", e);
		}
		
		try (OutputStream out = response.getOutputStream()){
			out.write(IOUtils.readFileAsBytes(file));
			out.flush();
		} catch (IOException e) {
			log.error("", e);
			throw new DownloadException("下载文件失败", e);
		}
	}
}
