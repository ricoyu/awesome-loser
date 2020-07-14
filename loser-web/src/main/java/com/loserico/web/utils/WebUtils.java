package com.loserico.web.utils;

import javax.servlet.http.HttpServletRequest;

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

}
