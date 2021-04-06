package com.loserico.common.lang.i18n;

import com.loserico.common.lang.exception.BusinessException;
import com.loserico.common.lang.utils.Arrays;
import com.loserico.common.lang.utils.CollectionUtils;
import com.loserico.common.lang.errors.ErrorType;
import org.springframework.context.MessageSource;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 国际化支持类
 * <p>
 * Copyright: (C), 2019-09-04 9:48
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class I18N {
	
	/**
	 * 如果设置了国际化消息模板, 尝试获取国际化消息, 如果取不到, 返回默认消息
	 *
	 * @param errorType
	 * @return
	 */
	public static String i18nMessage(ErrorType errorType) {
		if (isNotBlank(errorType.msgTemplate())) {
			MessageSource messageSource = LocaleContextHolder.getMessageSource();
			if (messageSource != null) {
				return messageSource.getMessage(errorType.msgTemplate(), null, errorType.message(), LocaleContextHolder.getLocale());
			}
		}
		
		return errorType.message();
	}
	
	public static String i18nMessage(BusinessException businessException) {
		if (isNotBlank(businessException.getMsgTemplate())) {
			MessageSource messageSource = LocaleContextHolder.getMessageSource();
			if (messageSource != null) {
				if (CollectionUtils.isNotEmpty(businessException.getMessageParams())) {
					return messageSource.getMessage(businessException.getMsgTemplate(),
							Arrays.asArray(businessException.getMessageParams()),
							businessException.getMessage(),
							LocaleContextHolder.getLocale());
				} else {
					return messageSource.getMessage(businessException.getMsgTemplate(),
							null,
							businessException.getMessage(),
							LocaleContextHolder.getLocale());
				}
			}
		}
		
		return businessException.getMessage();
	}
	
	/**
	 * 根据消息模板获取国际化消息
	 * @param msgTemplate
	 * @return String
	 */
	public static String i18nMessage(String msgTemplate) {
		if (isBlank(msgTemplate)) {
			return null;
		}
		
		MessageSource messageSource = LocaleContextHolder.getMessageSource();
		if (messageSource == null) {
			return null;
		}
		
		return messageSource.getMessage(msgTemplate, null, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 根据消息模板获取国际化消息
	 * @param msgTemplate
	 * @return String
	 */
	public static String i18nMessage(String msgTemplate, Object[] args) {
		if (isBlank(msgTemplate)) {
			return null;
		}
		
		MessageSource messageSource = LocaleContextHolder.getMessageSource();
		if (messageSource == null) {
			return null;
		}
		
		return messageSource.getMessage(msgTemplate, args, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 根据消息模板获取国际化消息
	 * @param msgTemplate
	 * @return String
	 */
	public static String i18nMessage(String msgTemplate, List<?> args) {
		return i18nMessage(msgTemplate, Arrays.asArray(args));
	}
}
