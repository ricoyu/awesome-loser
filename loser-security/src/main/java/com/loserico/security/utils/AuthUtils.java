package com.loserico.security.utils;

import com.google.common.base.Strings;
import com.loserico.security.vo.AuthRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.loserico.common.lang.utils.Assert.notNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 专门用来处理HTTP请求参数
 * <p>
 * Copyright: (C), 2021-03-29 17:26
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class AuthUtils {
	
	/**
	 * 将所有参数按字母顺序排序后转成一个字符串
	 * Map<String, String>形式, 转成: param1=value1&param2=value2
	 * Map<String, List<String>>形式, 转成: param1=value1&param2=value2&param2=value2
	 *
	 * @param params
	 * @return
	 */
	public static String toParamString(Map<String, ?> params) {
		if (params == null || params.isEmpty()) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		
		params.keySet()
				.stream()
				.sorted((prev, next) -> prev.compareTo(next))
				.forEach((key) -> {
					Object value = params.get(key);
					if (value == null) {
						return;
					}
					if (Collection.class.isAssignableFrom(value.getClass())) {
						((Collection) value).forEach((element) -> {
							sb.append(key).append("=").append(element).append("&");
						});
						return;
					}
					
					sb.append(key).append("=").append(value).append("&");
				});
		
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	/**
	 * 根据请求URI和请求参数构造AuthRequest对象
	 * @param actualUri
	 * @param queryString
	 * @return AuthRequest
	 */
	public static AuthRequest parseAuthRequest(String actualUri, String queryString) {
		notNull(actualUri, "actualUri cannot be null!");
		
		AuthRequest authRequest = new AuthRequest();
		authRequest.setActualUri(actualUri);
		
		if (isBlank(queryString)) {
			return authRequest;
		}
		
		Map<String, List<String>> paramMap = splitQuery(queryString);
		
		// uri 参数只支持一个
		List<String> uris = paramMap.get("uri");
		if (uris.size() > 0 && isNotBlank(uris.get(0))) {
			authRequest.setUri(uris.get(0));
		}
		
		 
		List<String> timestamps = paramMap.get("timestamp");
		if (timestamps.size() > 0 && isNotBlank(timestamps.get(0))) {
			try {
				authRequest.setTimestamp(Long.parseLong(timestamps.get(0)));
			} catch (NumberFormatException e) {
				log.error("timestamp参数需要传UNIX miliseconds", e);
				throw new IllegalArgumentException("必须穿timestamp参数!");
			}
		}
		
		// access_token 参数只支持一个
		List<String> accessTokens = paramMap.get("access_token");
		if (accessTokens.size() > 0 && isNotBlank(accessTokens.get(0))) {
			authRequest.setAccessToken(accessTokens.get(0));
		}
		
		// 删掉uri, timestamp参数，其他参数继续往后面传递
		paramMap.remove("uri");
		paramMap.remove("timestamp");
		authRequest.setParams(paramMap);
		
		return authRequest;
	}
	
	/**
	 * 将请求参数转成Map, 如:
	 * param1=value1&param2=value2                转成:  Map<String, String>形式
	 * param1=value1&param2=value2&param2=value2  转成: Map<String, List<String>>形式
	 * @param uri
	 * @return Map<String, List<String>>
	 */
	public static Map<String, List<String>> splitQuery(String uri) {
		if (Strings.isNullOrEmpty(uri)) {
			return Collections.emptyMap();
		}
		
		return stream(uri.split("&"))
				.map(AuthUtils::splitQueryParameter)
				.collect(groupingBy(
						SimpleImmutableEntry::getKey,
						LinkedHashMap::new,
						mapping(Map.Entry::getValue, toList())));
	}
	
	private static SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
		final int idx = it.indexOf("=");
		final String key = idx > 0 ? it.substring(0, idx) : it;
		final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
		return new SimpleImmutableEntry<>(key, value);
	}
}
