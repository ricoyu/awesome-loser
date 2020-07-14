package com.loserico.web.utils;

import com.loserico.common.lang.context.ApplicationContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * 获取MessageSource帮助类
 * <p>
 * Copyright: Copyright (c) 2019-10-14 15:26
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MessageHelper {

	private static volatile MessageSource messageSource;

	public static final String getMessage(String code) {
		if (messageSource == null) {
			synchronized (MessageHelper.class) {
				messageSource = ApplicationContextHolder.getBean(MessageSource.class);
				if (messageSource == null) {
					return null;
				}
			}
		}

		try {
			return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			return null;
		}
	}

	public static final String getMessage(Supplier<String> templateSupplier) {
		return getMessage(templateSupplier.get());
	}

	public static final String getMessage(String code, String defaultMessage) {
		if (messageSource == null) {
			synchronized (MessageHelper.class) {
				messageSource = ApplicationContextHolder.getBean(MessageSource.class);
				if (messageSource == null) {
					return null;
				}
			}
		}

		return messageSource.getMessage(code, null, defaultMessage, LocaleContextHolder.getLocale());
	}

	public static final String getMessage(String code, List<Object> args) {
		if (messageSource == null) {
			synchronized (MessageHelper.class) {
				messageSource = ApplicationContextHolder.getBean(MessageSource.class);
				if (messageSource == null) {
					return null;
				}
			}
		}

		try {
			return messageSource.getMessage(code, args.stream().toArray(Object[]::new), LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			return null;
		}
	}

	public static final String getMessage(String code, String defaultMessage, Object... args) {
		if (messageSource == null) {
			synchronized (MessageHelper.class) {
				messageSource = ApplicationContextHolder.getBean(MessageSource.class);
				if (messageSource == null) {
					return null;
				}
			}
		}

		try {
			return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			return null;
		}
	}

	/**
	 * 获取指定语言的信息
	 *
	 * @param code
	 * @param locale
	 * @param args
	 * @return
	 */
	public static String getMessage(String code, Locale locale, Object... args) {
		if (messageSource == null) {
			synchronized (MessageHelper.class) {
				messageSource = ApplicationContextHolder.getBean(MessageSource.class);
				if (messageSource == null) {
					return null;
				}
			}
		}

		try {
			return messageSource.getMessage(code, args, code, locale);
		} catch (NoSuchMessageException e) {
			return null;
		}
	}

	public static String getParameteredMessage(String code, Object... args) {
		if (messageSource == null) {
			synchronized (MessageHelper.class) {
				messageSource = ApplicationContextHolder.getBean(MessageSource.class);
				if (messageSource == null) {
					return null;
				}
			}
		}

		try {
			return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			return null;
		}
	}

}