package com.loserico.workbook.unmarshal.convertor.datetime;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.regex.Pattern.compile;

/**
 * 负责处理日期时间字符串长度是14的情况
 * <p>
 * Copyright: Copyright (c) 2019-06-18 14:34
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class DateTimeConvertor14 extends AbstractDateTimeConvertor {

	@Override
	public boolean supports(String datetime) {
		return datetime.length() == 14;
	}

	@Override
	void init() {
		patterns = new Pattern[3];
		formaters = new DateTimeFormatter[3];

		patterns[0] = compile("\\d{4}-\\d{1}-\\d{1}(\\s+)\\d{2}:\\d{2}");
		patterns[1] = compile("\\d{4}-\\d{2}-\\d{1}(\\s+)\\d{1}:\\d{2}");
		patterns[2] = compile("\\d{4}-\\d{1}-\\d{2}(\\s+)\\d{1}:\\d{2}");

		// 14
		formaters[0] = ofPattern("yyyy-M-d HH:mm");
		// 14
		formaters[1] = ofPattern("yyyy-MM-d H:mm");
		// 14
		formaters[2] = ofPattern("yyyy-M-dd H:mm");
	}

}
