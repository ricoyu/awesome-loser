package com.loserico.web.advice;

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
import java.util.Date;

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
				Date result = DateUtils.parse(text);
				setValue(result);
			}
		}
		
	}
	
	public class LocalDateTimeEditor extends PropertyEditorSupport {
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (StringUtils.isBlank(text)) {
				setValue(null);
			} else {
				LocalDateTime result = DateUtils.toLocalDateTime(text);
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
				LocalDate result = DateUtils.toLocalDate(text);
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
				LocalTime result = DateUtils.toLocalTime(text);
				setValue(result);
			}
		}
		
	}
	
}
