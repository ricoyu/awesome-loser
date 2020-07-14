package com.loserico.web.resolver;

import com.loserico.common.lang.constants.DateConstants;
import com.loserico.common.lang.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支持Controller方法参数各种日期格式绑定, 需要调用WebMvcConfigurer#addArgumentResolvers来添加
 * 
 * @author Loser
 * @since May 22, 2016
 * @version 
 *
 */
public class DateArgumentResolver implements HandlerMethodArgumentResolver {
	
	private static final Pattern ISO_DATETIME_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}");
	private static final Pattern ISO_DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Date.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String value = request.getParameter(parameter.getParameterName());

		if (StringUtils.isBlank(value)) {
			return null;
		}

		Date result = null;
		if (matches(ISO_DATETIME_PATTERN, value)) {
			result = DateUtils.parse(value, DateConstants.FMT_ISO_DATETIME);
		} else if (matches(ISO_DATE_PATTERN, value)) {
			result = DateUtils.parse(value, DateConstants.FMT_ISO_DATE);
		}

		return result;
	}
	
	private boolean matches(Pattern pattern, String text) {
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
}
