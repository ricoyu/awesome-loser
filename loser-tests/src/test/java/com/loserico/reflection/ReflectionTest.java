package com.loserico.reflection;

import com.loserico.common.lang.utils.ReflectionUtils;
import org.junit.Test;

import java.lang.reflect.Field;

public class ReflectionTest {

	@Test
	public void testFields() {
		Field[] fields = Man.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			System.out.println(field.toGenericString());
			System.out.println(field.getName());
		}
	}
	
	@Test
	public void testGetFields() {
		Field[] fields  = ReflectionUtils.getFields(Man.class);
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			System.out.println(field.toGenericString());
		}
	}
	
	@Test
	public void testArrayType() {
		System.out.println(new int[]{}.getClass());
		System.out.println(new int[]{}.getClass().isArray());
		System.out.println(new String[]{}.getClass().isArray());
	}
}
