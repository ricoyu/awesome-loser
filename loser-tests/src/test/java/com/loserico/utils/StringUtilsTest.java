package com.loserico.utils;

import com.loserico.common.lang.utils.StringUtils;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class StringUtilsTest {

	@Test
	public void testEndWith() {
		List<String> strs = asList("asd", "ddd ", "cc dd cc d");
		strs.forEach((str) -> {
			System.out.println(StringUtils.endWith(str, null));
		});
	}
	
	@Test
	public void testSubStr() {
		String s = "aaaaaaaaaassssssssssssdddxasdf-09asd";
//		System.out.println(StringUtils.subStr(s, -1));
//		System.out.println(StringUtils.subStr(s, -3));
//		System.out.println(StringUtils.subStr(s, -2, 0));
//		System.out.println(StringUtils.subStr(s, 2, 0));
		System.out.println(StringUtils.subStr(s, 0, -1));
	}
	
	@Test
	public void testUniqueKey() {
		for (int i = 0; i < 11; i++) {
			System.out.println(StringUtils.uniqueKey(50));
			String uniqueKey = StringUtils.uniqueKey(50);
			System.out.println(uniqueKey);
		}
	}
	
	@Test
	public void testJoinWith() {
		List<String> ids = asList("123", "456", "123131");
		String result = StringUtils.joinWith(",", ids);
		System.out.println(result);
		assertTrue(!result.endsWith(","));
		ids = asList("123", "456", "123131", "");
		result = StringUtils.joinWith(",", ids);
		System.out.println(result);
		assertTrue(!result.endsWith(","));
	}
}
