package com.loserico.common.spring.interceptor;

import com.loserico.common.spring.utils.LocaleUtils;

import java.util.Locale;

/**
 * 动态切换locale
 * 支持的参数:
 * paramName: 默认locale, url里面可以通过参数locale=zh_CN来切换
 * ignoreInvalidLocale: locale=zh_CN这个参数值如果传了不合法的locale字符串, 是否忽略, 默认true
 * languageTagCompliant: true
 * <p>
 * Copyright: Copyright (c) 2020-09-02 14:03
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LocaleChangeInterceptor extends org.springframework.web.servlet.i18n.LocaleChangeInterceptor{

	@Override
	protected Locale parseLocaleValue(String locale) {
		return LocaleUtils.toLocale(locale);
	}

}
