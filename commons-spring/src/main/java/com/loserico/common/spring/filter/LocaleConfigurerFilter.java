package com.loserico.common.spring.filter;

import com.loserico.common.lang.context.ApplicationContextHolder;
import com.loserico.common.lang.i18n.LocaleContextHolder;
import com.loserico.common.spring.utils.LocaleUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:54
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LocaleConfigurerFilter implements Filter {
	
	/**
	 * 通过请求参数切换locale时的参数名
	 */
	private String paramName = "lang";
	
	public LocaleConfigurerFilter() {}
	
	public LocaleConfigurerFilter(String paramName) {
		this.paramName = paramName;
	}
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		LocaleContextHolder.setMessageSource(ApplicationContextHolder.getBean(MessageSource.class));
		
		Locale locale = null;
		LocaleResolver localeResolver = (LocaleResolver) ApplicationContextHolder.getBean("localeResolver");
		
		String newLocale = request.getParameter(paramName);
		if (isNotBlank(newLocale)) {
			locale = LocaleUtils.toLocale(newLocale);
		}
		
		if (locale != null) {
			if (localeResolver != null) {
				localeResolver.setLocale((HttpServletRequest) request, (HttpServletResponse) response, locale);
			}
			LocaleContextHolder.setLocale(locale);
			chain.doFilter(request, response);
			return;
		}
		
		
		if (localeResolver != null) {
			locale = localeResolver.resolveLocale((HttpServletRequest) request);
			localeResolver.setLocale((HttpServletRequest) request, (HttpServletResponse) response, locale);
		} else {
			locale = RequestContextUtils.getLocale((HttpServletRequest) request);
		}
		LocaleContextHolder.setLocale(locale);
		
		chain.doFilter(request, response);
	}
}
