package com.loserico.workbook.unmarshal.convertor.datetime;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.regex.Pattern.compile;

/**
 * 负责处理日期时间字符串长度是16的情况
 * <p>
 * Copyright: Copyright (c) 2019-06-18 14:25
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class DateTimeConvertor16 extends AbstractDateTimeConvertor {

	@Override
	public boolean supports(String datetime) {
		return datetime.length() == 16;
	}

	@Override
	protected void init() {
		patterns = new Pattern[3];
		formaters = new DateTimeFormatter[3];

		patterns[0] = compile("\\d{4}-\\d{1}-\\d{1}(\\s+)\\d{1}:\\d{2}:\\d{2}");
		patterns[1] = compile("\\d{4}-\\d{2}-\\d{2}(\\s+)\\d{2}:\\d{2}");
		patterns[2] = compile("\\d{4}/\\d{1}/\\d{1}(\\s+)\\d{1}:\\d{2}:\\d{2}");

		formaters[0] = ofPattern("yyyy-M-d H:mm:ss");
		formaters[1] = ofPattern("yyyy-MM-dd HH:mm");
		formaters[2] = ofPattern("yyyy/M/d H:mm:ss");
	}

}
