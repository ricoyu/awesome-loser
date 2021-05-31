package com.loserico.web.resolver;

import com.loserico.common.lang.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

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

		return DateUtils.toLocalDate(value);
	}
}
