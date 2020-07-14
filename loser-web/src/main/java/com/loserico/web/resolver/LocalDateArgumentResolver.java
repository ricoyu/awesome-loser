package com.loserico.web.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * 支持Controller方法参数各种日期格式绑定, 需要调用WebMvcConfigurer#addArgumentResolvers来添加
 *
 * @author Loser
 * @since May 22, 2016
 * @version
 *
 */
public class LocalDateArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return LocalDate.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String value = request.getParameter(parameter.getParameterName());

		if (StringUtils.isBlank(value)) {
			return null;
		}

		LocalDate result = null;
		if (Pattern.matches("\\d{4}-\\d{2}-\\d{2}", value)) {
			result = LocalDate.parse(value, ofPattern("yyyy-MM-dd"));
		}

		return result;
	}
}