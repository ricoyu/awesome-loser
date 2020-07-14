package com.loserico.common.lang;

import com.loserico.common.lang.utils.GenericUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020/4/20 10:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class GenericUtilsTests {
	
	@Test
	public void testGetClassTypeParameter() {
		Class<?> typeClass = GenericUtils.getTypeArgument(Man.class);
		System.out.println(typeClass.getName());
		System.out.println(typeClass);
	}
	
	static interface Person<T> {
		
	}
	
	static class Man implements Person<String> {
		
	}
}
