package com.loserico.generic;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020/6/13 21:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TypeReferenceTest {
	
	@Test
	public void testTypeRefernece() {
		/*
		 * The constructor does the following steps to preserve the type information:
		 * First, it gets the generic superclass metadata for this particular instance – in this case, the generic superclass is TypeReference<Map<String, Integer>>
		 * Then, it gets and stores the actual type parameter for the generic superclass – in this case, it would be Map<String, Integer>
		 * 
		 * This approach for preserving the generic type information is usually known as super type token:
		 */
		TypeReference<Map<String, Integer>> token = new TypeReference<Map<String, Integer>>() {};
		
		Type type = token.getType();
		Assert.assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", type.getTypeName());
		
		Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
		Assert.assertEquals("java.lang.String", typeArguments[0].getTypeName());
		Assert.assertEquals("java.lang.Integer", typeArguments[1].getTypeName());
	}
}
