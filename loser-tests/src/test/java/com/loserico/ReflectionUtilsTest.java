package com.loserico;

import com.loserico.common.lang.utils.ReflectionUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-10-23 17:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ReflectionUtilsTest {
	
	@Test
	public void testGetAllFieldsExceptionObject() {
		Field[] fields = ReflectionUtils.getFields(Programmer.class);
		for (int i = 0; i < fields.length; i++) {
			System.out.println(fields[i].getName());
		}
	}
	
	@Test
	public void testClassEqual() {
		Class clazz = HashMap.class;
		if (Map.class.isAssignableFrom(clazz)) {
			System.out.println("yes");
		}
	}
}

class Programmer extends Person{
	
	private volatile int id;
	
	private String language;
}

class Person {
	
	private volatile int uuid;
	
	private String gender;
}
