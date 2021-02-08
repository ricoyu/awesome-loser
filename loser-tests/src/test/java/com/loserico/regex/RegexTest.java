package com.loserico.regex;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-02-04 14:40
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RegexTest {
	
	/*
	 * https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	 * https://stackoverflow.com/questions/201323/how-to-validate-an-email-address-using-a-regular-expression
	 */
	@Test
	public void testEmail() {
		String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
		String email = "12345@xxx.ccc.yy";
		boolean matches = email.matches(regex);
		//assertTrue(matches);
		
		regex = "^(?=.{1,64}@)[\\\\p{L}0-9_-]+(\\\\.[\\\\p{L}0-9_-]+)*@[^-][\\\\p{L}0-9-]+(\\\\.[\\\\p{L}0-9-]+)*(\\\\.[\\\\p{L}]{2,})$";
		/*matches = email.matches(regex);
		assertTrue(matches);*/
		
		System.out.println("h@example.example-example.com".matches(regex));
		System.out.println("12345@xxx.ccc.yy".matches(regex));
		
		String regex_v1 = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		
		System.out.println("h@example.example-example.com".matches(regex_v1));
		System.out.println("12345@xxx.ccc.yy".matches(regex_v1));
		System.out.println("ricoyu520@gmail.com".matches(regex_v1));
		System.out.println("yuxh@idss-cn.com".matches(regex_v1));
	}
}
