package com.loserico.web.resolver;

import com.loserico.web.utils.RegexUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * 支持Spring Controller LocalTime类型参数绑定 
 * 需要调用WebMvcConfigurer#addArgumentResolvers来添加
 * <p>
 * Copyright: Copyright (c) 2019-10-14 17:17
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalTimeArgumentResolver implements HandlerMethodArgumentResolver {
	
	private static final Pattern ISO_LOCAL_TIME_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return LocalTime.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String value = request.getParameter(parameter.getParameterName());

		if (StringUtils.isBlank(value)) {
			return null;
		}

		LocalTime result = null;
		if (RegexUtils.matches(ISO_LOCAL_TIME_PATTERN, value)) {
			result = LocalTime.parse(value, DateTimeFormatter.ISO_TIME);
		}

		return result;
	}
}