package com.loserico.common.spring.utils;

import com.loserico.networking.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Spring 环境下读写Http Servlet相关接口
 * <p>
 * Copyright: (C), 2020/4/23 12:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ServletUtils {
	
	public static final String APPLICATION_JSON = "application/json";
	
	public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
	
	/**
	 * 获取HttpServletRequest
	 *
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest request() {
		return getRequest();
	}
	
	/**
	 * 获取HttpServletResponse
	 *
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse response() {
		return getResponse();
	}
	
	/**
	 * 获取请求头
	 *
	 * @param header
	 * @return String
	 */
	public static String getHeader(String header) {
		HttpServletRequest request = getRequest();
		return request.getHeader(header);
	}
	
	/**
	 * 添加请求头
	 *
	 * @param header
	 * @param value
	 */
	public static void setHeader(String header, String value) {
		HttpServletResponse response = getResponse();
		response.setHeader(header, value);
	}
	
	/**
	 * 设置日期类型的请求头
	 *
	 * @param header
	 * @param date
	 */
	public static void setDateHeader(String header, long date) {
		HttpServletResponse response = getResponse();
		response.setDateHeader(header, date);
	}
	
	/**
	 * 设置int类型请求头
	 *
	 * @param header
	 * @param value
	 */
	public static void setIntHeader(String header, int value) {
		HttpServletResponse response = getResponse();
		response.setIntHeader(header, value);
	}
	
	/**
	 * 获取Cookie
	 *
	 * @param name
	 * @return String
	 */
	public static String getCookie(String name) {
		HttpServletRequest request = getRequest();
		return HttpUtils.getCookie(request, name);
	}
	
	
	/**
	 * 获取请求参数
	 *
	 * @param parameter
	 * @return String
	 */
	public static String getParameter(String parameter) {
		HttpServletRequest request = getRequest();
		return request.getParameter(parameter);
	}
	
	/**
	 * 添加Cookie
	 *
	 * @param name
	 * @param value
	 * @return CookieBuilder
	 */
	public static CookieBuilder addCookie(String name, String value) {
		return new CookieBuilder(name, value);
	}
	
	/**
	 * 添加Session attribute, 没有过期时间
	 *
	 * @param attributeName
	 * @param value
	 */
	public static void setSessionAttribute(String attributeName, Object value) {
		Assert.notNull(attributeName, "attributeName cannot be null");
		getSession().setAttribute(attributeName, value);
	}
	
	/**
	 * 添加Session attribute, 同时设置session过期时间
	 *
	 * @param attributeName
	 * @param value
	 * @param expireInSeconds
	 */
	public static void setSessionAttribute(String attributeName, Object value, int expireInSeconds) {
		Assert.notNull(attributeName, "attributeName cannot be null");
		HttpSession session = getSession();
		session.setAttribute(attributeName, value);
		session.setMaxInactiveInterval(expireInSeconds);
	}
	
	/**
	 * 从session获取attribute
	 *
	 * @param attributeName
	 * @param <T>
	 * @return T
	 */
	public static <T> T getSessionAttribute(String attributeName) {
		Assert.notNull(attributeName, "attributeName cannot be null");
		return (T) getSession().getAttribute(attributeName);
	}
	
	/**
	 * 清除session
	 */
	public static void invalidateSession() {
		getSession().invalidate();
	}
	
	/**
	 * 获取请求的URL
	 *
	 * @return String
	 */
	public static String requestUrl() {
		return request().getRequestURL().toString();
	}
	
	/**
	 * 获取Content-Type请求头
	 *
	 * @return String
	 */
	public static String contentType() {
		return getHeader("Content-Type");
	}
	
	/**
	 * 设置Content-Type请求头
	 *
	 * @param contentType
	 */
	public static void contentType(String contentType) {
		Assert.notNull(contentType, "contentType 不能为null");
		setHeader("Content-Type", contentType);
	}
	
	/**
	 * 判断是否是ajax请求
	 *
	 * @return boolean
	 */
	public static boolean isAjax() {
		String contentType = contentType();
		return "XMLHttpRequest".equals(getHeader("X-Requested-With"))
				|| APPLICATION_JSON.equalsIgnoreCase(contentType)
				|| APPLICATION_JSON_UTF8.equalsIgnoreCase(contentType);
	}
	
	/**
	 * 写数据到Response中
	 *
	 * @param data
	 * @throws IOException
	 */
	public static void writeResponse(String data) {
		HttpServletResponse response = getResponse();
		try {
			response.getWriter().write(data);
		} catch (IOException e) {
			log.error("", e);
			throw new RuntimeException("写response失败", e);
		}
	}
	
	/**
	 * Servlet重定向
	 *
	 * @param redirectUrl
	 */
	public static void redirect(String redirectUrl) {
		try {
			getResponse().sendRedirect(redirectUrl);
		} catch (IOException e) {
			log.error("", e);
			throw new RuntimeException("重定向失败", e);
		}
	}
	
	/**
	 * 读取HttpServletRequest Body
	 */
	public String readRequestBody() {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			br = request().getReader();
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
	
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 如果是多级代理，那么取第一个ip为客户端ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(0, ip.indexOf(",")).trim();
		}
		
		return ip;
	}
	
	public static class CookieBuilder {
		
		private static final int MAX_AGE_NOT_SET = -2;
		
		private String name;
		
		private String value;
		
		/**
		 * 0  maxAge 设置为 0, 表示将Cookie删除
		 * -1 表示永久有效
		 * -2 是我自定义的, 表示builder没有设置maxAge
		 */
		private int maxAge = MAX_AGE_NOT_SET;
		
		private String domain;
		
		private String path = "/";
		
		private boolean secure;
		
		/**
		 * 防止脚本攻击
		 */
		private boolean httpOnly = false;
		
		public CookieBuilder(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		/**
		 * 0  maxAge 设置为 0, 表示将Cookie删除
		 * -1 表示永久有效
		 *
		 * @param maxAge
		 * @return
		 */
		public CookieBuilder maxAge(int maxAge) {
			this.maxAge = maxAge;
			return this;
		}
		
		public CookieBuilder domain(String domain) {
			this.domain = domain;
			return this;
		}
		
		public CookieBuilder path(String path) {
			this.path = path;
			return this;
		}
		
		public CookieBuilder httpOnly(boolean httpOnly) {
			this.httpOnly = httpOnly;
			return this;
		}
		
		/**
		 * Indicates to the browser whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL.
		 * The default value is false.
		 *
		 * @param secure
		 * @return CookieBuilder
		 */
		public CookieBuilder secure(boolean secure) {
			this.secure = secure;
			return this;
		}
		
		/**
		 * 创建Cookie对象并添加到response
		 *
		 * @return Cookie
		 */
		public Cookie build() {
			HttpServletResponse response = getResponse();
			String encoded = this.value == null ? null : HttpUtils.urlEncode(this.value);
			Cookie cookie = new Cookie(name, encoded);
			if (MAX_AGE_NOT_SET != maxAge) {
				cookie.setMaxAge(maxAge);
			}
			cookie.setDomain(domain);
			cookie.setPath(path);
			cookie.setHttpOnly(httpOnly);
			cookie.setSecure(secure);
			response.addCookie(cookie);
			return cookie;
		}
	}
	
	private static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	private static HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
	
	private static HttpSession getSession() {
		return getRequest().getSession();
	}
	
}
