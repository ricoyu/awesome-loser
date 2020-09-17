package com.loserico.mongo;

import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Copyright: (C), 2020-09-15 17:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MongoTest {
	
	@Test
	public void testVarPlaceHolder() {
		String regex = "#\\{(^[a-zA-Z0-9]+[_@]*[a-zA-Z0-9]*$)\\}";
		Pattern pattern = Pattern.compile(regex);
		List<String> placeholders = asList("#{ss}");
		for (String placeholder : placeholders) {
			assertTrue(pattern.matcher(placeholder).matches());
		}
	}
}
