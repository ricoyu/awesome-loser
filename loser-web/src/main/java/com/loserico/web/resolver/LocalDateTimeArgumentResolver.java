package com.loserico.web.resolver;

import com.loserico.common.lang.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;

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

		return DateUtils.toLocalDateTime(value);
	}
}
