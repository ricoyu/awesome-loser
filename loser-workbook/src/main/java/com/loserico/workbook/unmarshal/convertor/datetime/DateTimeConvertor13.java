package com.loserico.workbook.unmarshal.convertor.datetime;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.regex.Pattern.compile;

/**
 * 负责处理日期时间字符串长度是13的情况
 * <p>
 * Copyright: Copyright (c) 2019-06-18 14:39
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class DateTimeConvertor13 extends AbstractDateTimeConvertor {

	@Override
	public boolean supports(String datetime) {
		return datetime.length() == 13;
	}

	@Override
	void init() {
		patterns = new Pattern[1];
		formaters = new DateTimeFormatter[1];

		patterns[0] = compile("\\d{4}-\\d{1}-\\d{1}(\\s+)\\d{1}:\\d{2}");
		formaters[0] = ofPattern("yyyy-M-d H:mm");
	}

}
