package com.loserico.common.spring.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.loserico.common.lang.utils.Assert.notNull;
import static com.loserico.networking.constants.NetworkConstant.HTTP_HEADER_HTTP_CLIENT_IP;
import static com.loserico.networking.constants.NetworkConstant.HTTP_HEADER_HTTP_X_FORWARDED_FOR;
import static com.loserico.networking.constants.NetworkConstant.HTTP_HEADER_PROXY_CLIENT_IP;
import static com.loserico.networking.constants.NetworkConstant.HTTP_HEADER_WL_PROXY_CLIENT_IP;
import static com.loserico.networking.constants.NetworkConstant.HTTP_HEADER_X_FORWARDED_FOR;
import static com.loserico.networking.constants.NetworkConstant.HTTP_HEADER_X_REAL_IP;
import static com.loserico.networking.constants.NetworkConstant.UNKNOWN;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
	
	private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	public static final String APPLICATION_JSON = "application/json";
	
	public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 获取请求头
	 *
	 * @param header
	 * @return String
	 */
	public static String getHeader(HttpServletRequest request, String header) {
		return request.getHeader(header);
	}
	
	/**
	 * 将所有的请求头放到Map里面返回
	 *
	 * @param request
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getRequestHeadInfo(HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		
		return map;
	}
	
	/**
	 * 获取Content-Type请求头
	 *
	 * @return String
	 */
	public static String contentType(HttpServletRequest request) {
		return getHeader(request, "Content-Type");
	}
	
	/**
	 * 添加请求头
	 *
	 * @param header
	 * @param value
	 */
	public static void setHeader(HttpServletResponse response, String header, String value) {
		response.setHeader(header, value);
	}
	
	/**
	 * 设置日期类型的请求头
	 *
	 * @param header
	 * @param date
	 */
	public static void setDateHeader(HttpServletResponse response, String header, long date) {
		response.setDateHeader(header, date);
	}
	
	/**
	 * 设置int类型请求头
	 *
	 * @param header
	 * @param value
	 */
	public static void setIntHeader(HttpServletResponse response, String header, int value) {
		response.setIntHeader(header, value);
	}
	
	/**
	 * 设置Content-Type请求头
	 *
	 * @param contentType
	 */
	public static void contentType(HttpServletResponse response, String contentType) {
		Assert.notNull(contentType, "contentType 不能为null");
		setHeader(response, "Content-Type", contentType);
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
	 * 获取所有的cookie值, 以Map形式返回
	 *
	 * @param request
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getRequestCookies(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				result.put(cookie.getName(), cookie.getValue());
			}
		}
		return result;
	}
	
	/**
	 * 获取Cookie的值, 并用UTF-8编码做UrlDecode
	 *
	 * @param request
	 * @param cookie
	 * @return String
	 */
	public static String getCookie(HttpServletRequest request, String cookie) {
		if (isBlank(cookie)) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (cookie.equalsIgnoreCase(c.getName())) {
					String value = c.getValue();
					if (!isBlank(value)) {
						return urlDecode(value);
					}
				}
			}
		}
		
		return null;
	}
	
	public static void removeRootCookies(HttpServletRequest request, HttpServletResponse response, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					cookie.setValue(null);
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
					break;
				}
			}
		}
	}
	
	/**
	 * 获取请求的URL
	 * http://localhost:8080/pic_code
	 *
	 * @return String
	 */
	public static String requestUrl(HttpServletRequest request) {
		return request.getRequestURL().toString();
	}
	
	/**
	 * 取X-Requested-With请求头, 判断值是否为XMLHttpRequest, 是的话认为是AJAX请求
	 * 或者返回类型是application/json也认为是AJAX请求
	 *
	 * @return boolean
	 */
	public static boolean isAjax(HttpServletRequest request) {
		String contentType = contentType(request);
		return "XMLHttpRequest".equals(getHeader(request, "X-Requested-With"))
				|| APPLICATION_JSON.equalsIgnoreCase(contentType)
				|| APPLICATION_JSON_UTF8.equalsIgnoreCase(contentType);
	}
	
	/**
	 * 写数据到Response中
	 *
	 * @param data
	 * @throws IOException
	 */
	public static void writeResponse(HttpServletResponse response, String data) {
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
	public static void redirect(HttpServletResponse response, String redirectUrl) {
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			log.error("", e);
			throw new RuntimeException("重定向失败", e);
		}
	}
	
	/**
	 * 读取HttpServletRequest Body
	 */
	public static String readRequestBody(HttpServletRequest request) {
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
	
	/**
	 * 获取用户真实IP地址
	 * <p>
	 * 不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址.<p>
	 * 可是, 如果通过了多级反向代理的话, X-Forwarded-For的值并不止一个, 而是一串IP值
	 *
	 * @param request
	 * @return String
	 */
	public static String getRemoteRealIP(HttpServletRequest request) {
		
		//先尝试从x-forwarded-for请求头拿client IP
		String ip = request.getHeader(HTTP_HEADER_X_FORWARDED_FOR);
		String ipDesc = "";
		
		log.debug("Get IP:{} from {}", ip, HTTP_HEADER_X_FORWARDED_FOR);
		if (!isIpUndetermined(ip)) {
			// 多次反向代理后会有多个ip值, 第一个ip才是真实ip
			if (ip.indexOf(",") != -1) {
				ip = ip.split(",")[0];
				ipDesc = HTTP_HEADER_X_FORWARDED_FOR;
			}
		}
		
		//尝试从Proxy-Client-IP请求头拿client IP
		if (isIpUndetermined(ip)) {
			ip = request.getHeader(HTTP_HEADER_PROXY_CLIENT_IP);
			ipDesc = HTTP_HEADER_PROXY_CLIENT_IP;
			log.debug("Get IP:{} from {}", ip, HTTP_HEADER_PROXY_CLIENT_IP);
		}
		
		//尝试从WL-Proxy-Client-IP请求头拿client IP
		if (isIpUndetermined(ip)) {
			ip = request.getHeader(HTTP_HEADER_WL_PROXY_CLIENT_IP);
			ipDesc = HTTP_HEADER_WL_PROXY_CLIENT_IP;
			log.debug("Get IP:{} from {}", ip, HTTP_HEADER_WL_PROXY_CLIENT_IP);
		}
		
		//尝试从HTTP_CLIENT_IP请求头拿client IP
		if (isIpUndetermined(ip)) {
			ip = request.getHeader(HTTP_HEADER_HTTP_CLIENT_IP);
			ipDesc = HTTP_HEADER_HTTP_CLIENT_IP;
			log.debug("Get IP:{} from {}", ip, HTTP_HEADER_HTTP_CLIENT_IP);
		}
		
		//尝试从HTTP_X_FORWARDED_FOR请求头拿client IP
		if (isIpUndetermined(ip)) {
			ip = request.getHeader(HTTP_HEADER_HTTP_X_FORWARDED_FOR);
			ipDesc = HTTP_HEADER_HTTP_X_FORWARDED_FOR;
			log.debug("Get IP:{} from {}", ip, HTTP_HEADER_HTTP_X_FORWARDED_FOR);
		}
		
		//尝试从X-Real-IP请求头拿client IP
		if (isIpUndetermined(ip)) {
			ip = request.getHeader(HTTP_HEADER_X_REAL_IP);
			ipDesc = HTTP_HEADER_X_REAL_IP;
			log.debug("Get IP:{} from {}", ip, HTTP_HEADER_X_REAL_IP);
		}
		
		//最后一招
		if (isIpUndetermined(ip)) {
			ip = request.getRemoteAddr();
			ipDesc = "RemoteAddr";
			log.debug("Get IP:{} from {}", ip, "request.getRemoteAddr()");
		}
		log.debug("获取请求真实IP，IP来源: {}", ipDesc);
		return ip;
	}
	
	/**
	 * 获取请求的URI
	 *
	 * @param request
	 * @return String
	 */
	public static String requestPath(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		return requestPath(httpServletRequest);
	}
	
	/**
	 * 获取请求的URI
	 *
	 * @param request
	 * @return String
	 */
	public static String requestPath(HttpServletRequest request) {
		String path = request.getServletPath();
		
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			path = StringUtils.hasLength(path) ? path + pathInfo : pathInfo;
		}
		
		return path;
	}
	
	/**
	 * 判断请求的URI是否与给定的matchPath匹配(以matchPath结尾)
	 *
	 * @param request
	 * @param matchPath 要匹配的URI
	 * @return boolean
	 */
	public static boolean pathMatch(HttpServletRequest request, String matchPath) {
		notNull(matchPath, "matchPath cannot be null");
		String path = requestPath(request);
		return antPathMatcher.match(matchPath, path);
	}
	
	/**
	 * 判断请求的URI是否与给定的matchPath匹配(以matchPath结尾)
	 *
	 * @param request
	 * @param matchPath 要匹配的URI
	 * @return boolean
	 */
	public static boolean pathMatch(ServletRequest request, String matchPath) {
		notNull(matchPath, "matchPath cannot be null");
		return pathMatch((HttpServletRequest) request, matchPath);
	}
	
	/**
	 * 判断path是否匹配matchPattern定义的规则
	 *
	 * @param path
	 * @param matchPattern
	 * @return boolean
	 */
	public static boolean pathMatch(String path, String matchPattern) {
		notNull(matchPattern, "matchPattern cannot be null");
		notNull(path, "path cannot be null");
		return antPathMatcher.match(matchPattern, path);
	}
	
	
	/**
	 * URL编码, 包括Cookie的编码
	 *
	 * @param s
	 * @return
	 */
	@SneakyThrows
	public static String urlEncode(String s) {
		return URLEncoder.encode(s, DEFAULT_CHARSET);
	}
	
	/**
	 * URL解码, 包括Cookie值的解码
	 *
	 * @param s
	 * @return
	 */
	@SneakyThrows
	public static String urlDecode(String s) {
		return URLDecoder.decode(s, DEFAULT_CHARSET);
	}
	
	/**
	 * 获取完整的请求路径
	 *
	 * @param request
	 * @return String
	 */
	public static String getRedirectUrl(HttpServletRequest request) {
		
		String httpFullUrl = getHttpFullUrl(request, request.getRequestURI());
		StringBuilder sb1 = new StringBuilder(httpFullUrl);
		Map<String, Object> params = handleServletParameter(request);
		StringBuilder sb2 = new StringBuilder("");
		
		if (params.size() > 0) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if (isNotEmpty(sb2.toString())) {
					sb2.append("&");
				}
				sb2.append(entry.getKey() + "=" + entry.getValue());
			}
			
			if (sb1.indexOf("?") != -1) {
				sb1.append("&");
			} else {
				sb1.append("?");
			}
			sb1.append(sb2);
		}
		return sb1.toString();
	}
	
	/**
	 * @param request
	 * @param url
	 * @return
	 */
	public static String getHttpFullUrl(HttpServletRequest request, String url) {
		String portString = "";
		int port = request.getServerPort();
		if (port != 80 && port != 443) {
			portString = ":" + port;
		}
		return new StringBuilder().append(request.getScheme())
				.append("://")
				.append(request.getServerName())
				.append(portString)
				.append(url)
				.toString();
	}
	
	/**
	 * 把Map所有元素, 按字母排序, 然后按照 "参数=参数值" 的模式用"&"字符拼接成字符串
	 *
	 * @param params 需要签名的参数
	 * @return String URL请求参数字符串
	 */
	public static String toUrlParamStr(Map<String, Object> params) {
		SortedMap<String, Object> sortedMap = new TreeMap<String, Object>(params);
		
		StringBuffer sb = new StringBuffer();
		Set es = sortedMap.entrySet();
		Iterator it = es.iterator();
		
		while (it.hasNext()) {
			Map.Entry sign = (Map.Entry) it.next();
			String k = (String) sign.getKey();
			String v = (String) sign.getValue();
			
			if (isNotEmpty(k) && isNotEmpty(v)) {
				
				if (isNotEmpty(sb.toString())) {
					sb.append("&");
				}
				
				sb.append(k);
				sb.append("=");
				sb.append(v);
			}
		}
		return sb.toString();
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
		public Cookie build(HttpServletResponse response) {
			String encoded = this.value == null ? null : urlEncode(this.value);
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
	
	private static Map<String, Object> handleServletParameter(HttpServletRequest request) {
		Map<String, String[]> requestParameter = request.getParameterMap();
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.putAll(requestParameter);
		
		Map<String, Object> requestParameters = new HashMap<String, Object>(0);
		for (Map.Entry<String, Object> m : parameter.entrySet()) {
			String key = m.getKey();
			Object[] obj = (Object[]) parameter.get(key);
			requestParameters.put(key, (obj.length > 1) ? obj : obj[0]);
		}
		return requestParameters;
	}
	
	/**
	 * IP为空或者unknown
	 *
	 * @param ip
	 * @return boolean
	 */
	private static boolean isIpUndetermined(String ip) {
		return ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip);
	}
	
	/**
	 * 判断s是null或者空字符串/只包含空格的字符串
	 *
	 * @param s
	 * @return boolean
	 */
	private static boolean isBlank(String s) {
		return s == null || "".equals(s.trim());
	}
	
}
