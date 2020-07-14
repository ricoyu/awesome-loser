package com.loserico.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 通过匿名内部类在运行时保存类型信息
 * <p>
 * Copyright: (C), 2020/6/13 21:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class TypeReference<T> {
	
	private final Type type;
	
	public TypeReference() {
		Type superclass = getClass().getGenericSuperclass();
		type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
	}
	
	public Type getType() {
		return type;
	}
}
