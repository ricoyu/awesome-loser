package com.loserico.workbook.unmarshal.convertor.datetime;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * 处理的转LocalDateTime主逻辑, 子类需要完成patterns和formaters的初始化
 * <p>
 * Copyright: Copyright (c) 2019-06-18 14:15
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public abstract class AbstractDateTimeConvertor implements DateTimeConvertor {

	protected Pattern[] patterns = null;

	protected DateTimeFormatter[] formaters = null;
	
	@Override
	public LocalDateTime convert(String datetime) {
		if (patterns == null) {
			init();
		}

		for (int i = 0; i < patterns.length; i++) {
			Pattern pattern = patterns[i];
			if (pattern.matcher(datetime).matches()) {
				return LocalDateTime.parse(datetime, formaters[i]);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("Not supported datetime {}", datetime);
		}
		return null;
	}

	/**
	 * 初始化
	 */
	abstract void init();
}
