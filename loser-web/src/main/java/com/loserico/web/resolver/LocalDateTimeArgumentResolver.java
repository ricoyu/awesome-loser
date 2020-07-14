package com.loserico.web.resolver;

import com.loserico.web.utils.RegexUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Controller LocalDateTime 参数类型绑定
 * 需要调用WebMvcConfigurer#addArgumentResolvers来添加
 * <p>
 * Copyright: Copyright (c) 2019-10-14 17:08
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalDateTimeArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	private static final Pattern ISO_DATETIME_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}");
	private static final DateTimeFormatter ISO_DATETIME_FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * YYYY-mm-DD
	 */
	public static final Pattern ISO_DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
	public static final DateTimeFormatter ISO_DATE_FORMATTER = ofPattern("yyyy-MM-dd");

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return LocalDateTime.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String value = request.getParameter(parameter.getParameterName());

		if (StringUtils.isBlank(value)) {
			return null;
		}

		LocalDateTime result = null;
		if (RegexUtils.matches(ISO_DATETIME_PATTERN, value)) {
			result = LocalDateTime.parse(value, ISO_DATETIME_FORMATTER);
		} else if (RegexUtils.matches(ISO_DATE_PATTERN, value)) {
			result = LocalDateTime.parse(value, ISO_DATE_FORMATTER);
		}

		return result;
	}
}