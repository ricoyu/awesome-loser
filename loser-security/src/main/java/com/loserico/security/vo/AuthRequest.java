package com.loserico.security.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与安全相关的一个请求对象封装
 * <p>
 * Copyright: Copyright (c) 2018-07-31 17:17
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Data
public class AuthRequest {
	
	/**
	 * 请求参数里面的uri
	 */
	private String uri;
	
	/**
	 * 该请求实际访问的URI
	 */
	private String actualUri;
	
	/**
	 * 表示这个token的有效期
	 */
	private long timestamp;
	
	/**
	 * 登录成功后服务端返回的token
	 */
	private String accessToken;
	
	/**
	 * 所有的参数
	 */
	private Map<String, List<String>> params = new HashMap<>();
	
	public AuthRequest() {
	}
	
	public AuthRequest(String uri, long timestamp, Map<String, List<String>> params) {
		this.uri = uri;
		this.timestamp = timestamp;
		this.params = params;
	}
	
	/**
	 * 检查声称要访问的URI和实际访问的URI是否一致
	 *
	 * @return
	 */
	public boolean requestPathMatchs(String contextPath) {
		String withoutContextPath = actualUri.replaceFirst(contextPath, "");
		return uri.endsWith(withoutContextPath);
	}
	
	/**
	 * token中的timestamp和url里面传的参数timestamp要一致
	 *
	 * @param currentTimestamp
	 * @return boolean
	 */
	public boolean matches(long currentTimestamp) {
		return timestamp != currentTimestamp;
	}
	
}
