package com.loserico.cache.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * <p>
 * Copyright: Copyright (c) 2019/10/17 14:17
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class DateUtils {

	/**
	 * 用默认时区将LocalDateTime转成1970-1-1 00:00:00以来的毫秒数
	 *
	 * @param localDateTime
	 * @return long
	 */
	public static long toEpochMilis(LocalDateTime localDateTime) {
		Objects.nonNull(localDateTime);
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
}
