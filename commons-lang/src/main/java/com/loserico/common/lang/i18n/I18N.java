package com.loserico.common.lang.i18n;

import com.loserico.common.lang.errors.ErrorType;
import com.loserico.common.lang.exception.BusinessException;
import com.loserico.common.lang.utils.Arrays;
import com.loserico.common.lang.utils.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * 如果给的是国际化消息模板, 如{ip.notvalid}, 那么需要先抽取模板ip.notvalid, 再获取国际化消息
	 */
	private static final Pattern TEMPLATE_REGEX = Pattern.compile("^\\s*\\{(\\S+)\\}\\s*$");
	
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
							businessException.getMessageParams().stream().toArray(Object[]::new),
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
	 *
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
		
		return messageSource.getMessage(i18nTemplateDefault(msgTemplate), null, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 根据消息模板获取国际化消息, 没有这个国际化消息的话使用默认消息
	 *
	 * @param msgTemplate
	 * @return String
	 */
	public static String i18nMessage(String msgTemplate, String defaultMessage) {
		if (isBlank(msgTemplate)) {
			return null;
		}
		
		MessageSource messageSource = LocaleContextHolder.getMessageSource();
		if (messageSource == null) {
			return defaultMessage;
		}
		
		try {
			return messageSource.getMessage(i18nTemplateDefault(msgTemplate), null, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			return defaultMessage;
		}
	}
	
	/**
	 * 根据消息模板获取国际化消息
	 *
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
		
		return messageSource.getMessage(i18nTemplateDefault(msgTemplate), args, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 根据消息模板获取国际化消息
	 *
	 * @param msgTemplate
	 * @return String
	 */
	public static String i18nMessage(String msgTemplate, Object[] args, String defaultMessage) {
		if (isBlank(msgTemplate)) {
			return null;
		}
		
		MessageSource messageSource = LocaleContextHolder.getMessageSource();
		if (messageSource == null) {
			return null;
		}
		
		try {
			return messageSource.getMessage(i18nTemplateDefault(msgTemplate), args, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			return defaultMessage;
		}
	}
	
	/**
	 * 根据消息模板获取国际化消息
	 *
	 * @param msgTemplate
	 * @return String
	 */
	public static String i18nMessage(String msgTemplate, List<?> args) {
		return i18nMessage(msgTemplate, Arrays.asArray(args));
	}
	
	
	/**
	 * 根据消息模板获取国际化消息
	 *
	 * @param msgTemplate
	 * @return String
	 */
	public static String i18nMessage(String msgTemplate, List<?> args, String defaultMessage) {
		return i18nMessage(msgTemplate, Arrays.asArray(args), defaultMessage);
	}
	
	/**
	 * 从类似{ip.notvalid}的模板字符串中抽取ip.notvalid部分,
	 * 如果不是{ip.notvalid}这种形式的, 直接原样返回
	 *
	 * @return String
	 */
	private static String i18nTemplateDefault(String msg) {
		if (isBlank(msg)) {
			return msg;
		}
		
		Matcher matcher = TEMPLATE_REGEX.matcher(msg);
		if (!matcher.matches()) {
			return msg;
		}
		
		return matcher.group(1);
	}
}
