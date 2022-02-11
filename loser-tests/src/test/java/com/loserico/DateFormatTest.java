package com.loserico;

import com.loserico.common.lang.utils.DateUtils;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * <p>
 * Copyright: (C), 2022-02-11 11:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DateFormatTest {
	
	@Test
	public void test() {
		LocalDateTime date = LocalDateTime.now();
		String format = DateUtils.format(date, "yyyy-MM-dd-HH");
		System.out.println(format);
		
		date = LocalDateTime.of(2022, 02, 11, 14, 00, 00);
		format = DateUtils.format(date, "yyyy-MM-dd-HH");
		System.out.println(format);
	}
}
