package com.loserico.json;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-7-13 0013 21:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JacksonUtilsLocalDateTimeTest {
	
	@Test
	public void test() {
		String json = IOUtils.readClassPathFileAsString("books.json");
		BookSearchResultVO bookSearchResultVO = JacksonUtils.toObject(json, BookSearchResultVO.class);
		System.out.println(bookSearchResultVO.getCreateTime());
	}
}
