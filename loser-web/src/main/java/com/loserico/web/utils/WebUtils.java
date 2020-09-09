package com.loserico.web.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Web 相关工具类 
 * <p>
 * Copyright: Copyright (c) 2019-10-14 17:30
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class WebUtils {

	/**
	 * 获取客户端IP地址，优先从X-FORWARDED-FOR请求头中取
	 * 
	 * Normally, before the web/proxy server forwards the request to the Java app
	 * server, it will store the real client IP request in a standard header name like
	 * x-forwarded-for, if you can't find the client IP in the entire request headers,
	 * try discussing it with your server administrator.
	 * 
	 * @return String
	 */
	public static String getClientIp(HttpServletRequest request) {
		String remoteAddr = "";
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || remoteAddr.trim() == "") {
				remoteAddr = request.getRemoteAddr();
			}
		}

		return remoteAddr;
	}
	
	
	/**
	 * 读取HttpServletRequest Body
	 */
	public static String bodyString(HttpServletRequest request) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			br = request.getReader();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();
		} catch (IOException e) {
			log.error("获取body参数失败", e);
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("获取body参数失败", e);
				}
			}
		}
		
		return sb.toString().replaceAll("\r|\n|\t", "");
	}
}
