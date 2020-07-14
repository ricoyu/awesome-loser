package com.loserico.web.advice;

import com.loserico.common.lang.constants.DateConstants;
import com.loserico.common.lang.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.loserico.common.lang.utils.StringUtils.equalTo;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * 将字符串表示的日期转换成java.util.Date类型，支持yyyy-MM-dd HH:mm:ss和yyyy-MM-dd HH:mm格式
 * <p>
 * Copyright: Copyright (c) 2019-10-11 16:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@ControllerAdvice
public class GlobalBindingAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalBindingAdvice.class);
	
	private static final String WHITE_SPACES = " ";
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	private static final String DATETIME = "\\d{4}-\\d{2}-\\d{2}(\\s+)\\d{2}:\\d{2}:\\d{2}";
	private static final Pattern DATETIME_PATTERN = Pattern.compile(DATETIME);
	private static final DateTimeFormatter DATETIME_FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * yyyy-MM-dd
	 */
	private static final String DATE = "\\d{4}-\\d{2}-\\d{2}";
	private static final Pattern DATE_PATTERN = Pattern.compile(DATE);
	private static final DateTimeFormatter DATE_FORMATTER = ofPattern("yyyy-MM-dd");
	
	
	/**
	 * HH:mm:ss 17:17:29
	 */
	private static final String TIME_LONG = "\\d{2}:\\d{2}:\\d{2}";
	private static final Pattern TIME_LONG_PATTERN = Pattern.compile(TIME_LONG);
	
	
	/**
	 * HH:mm 17:18
	 */
	private static final String TIME_MIDDLE = "\\d{2}:\\d{2}";
	private static final Pattern TIME_MIDDLE_PATTERN = Pattern.compile(TIME_MIDDLE);
	
	public static final DateTimeFormatter TIME_LONG_FORMATTER = ofPattern("HH:mm:ss");
	public static final DateTimeFormatter TIME_SHORT_PATTERN = ofPattern("HH:mm");
	
	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateEditor());
		binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
		binder.registerCustomEditor(LocalDateTime.class, new LocalDateTimeEditor());
		binder.registerCustomEditor(LocalTime.class, new LocalTimeEditor());
	}
	
	public class DateEditor extends PropertyEditorSupport {
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (StringUtils.isBlank(text)) {
				setValue(null);
			} else {
				Date result = null;
				try {
					Matcher matcher = DATETIME_PATTERN.matcher(text);
					if (matcher.matches()) {
						String spaces = matcher.group(1);
						if (!equalTo(spaces, WHITE_SPACES)) {
							text = text.replace(spaces, WHITE_SPACES);
						}
						result = DateUtils.parse(text, DateConstants.FMT_ISO_DATETIME);
						setValue(result);
						return;
					}
					
					if (matches(DATE_PATTERN, text)) {
						result = DateUtils.parse(text, DateConstants.FMT_ISO_DATE);
						setValue(result);
					}
				} catch (Exception e) {
					logger.error("msg", e);
				}
				
			}
		}
		
	}
	
	public class LocalDateTimeEditor extends PropertyEditorSupport {
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (StringUtils.isBlank(text)) {
				setValue(null);
			} else {
				LocalDateTime result = null;
				try {
					if (matches(DATETIME_PATTERN, text)) {
						result = LocalDateTime.parse(text, DATETIME_FORMATTER);
					} else if (matches(DATE_PATTERN, text)) {
						result = LocalDateTime.parse(text + " 00:00:00", DATETIME_FORMATTER);
					}
				} catch (Exception e) {
					logger.error("msg", e);
				}
				
				setValue(result);
			}
		}
		
	}
	
	public class LocalDateEditor extends PropertyEditorSupport {
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (StringUtils.isBlank(text)) {
				setValue(null);
			} else {
				LocalDate result = null;
				try {
					if (Pattern.matches(DATE, text)) {
						result = LocalDate.parse(text, DATE_FORMATTER);
					}
				} catch (Exception e) {
					logger.error("{} 转换成LocalDate失败", text);
				}
				
				setValue(result);
			}
		}
		
	}
	
	public class LocalTimeEditor extends PropertyEditorSupport {
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (StringUtils.isBlank(text)) {
				setValue(null);
			} else {
				LocalTime result = null;
				try {
					if (matches(TIME_LONG_PATTERN, text)) {
						result = LocalTime.parse(text, TIME_LONG_FORMATTER);
					} else if (matches(TIME_MIDDLE_PATTERN, text)) {
						result = LocalTime.parse(text, TIME_SHORT_PATTERN);
					}
				} catch (Exception e) {
					logger.error("msg", e);
				}
				
				setValue(result);
			}
		}
		
	}
	
	/**
	 * 测试给定text是否匹配给定模式
	 *
	 * @param pattern
	 * @param text
	 * @return boolean
	 */
	private boolean matches(Pattern pattern, String text) {
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	
}