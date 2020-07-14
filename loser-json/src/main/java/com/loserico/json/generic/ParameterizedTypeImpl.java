package com.loserico.json.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:30
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ParameterizedTypeImpl implements ParameterizedType {
	
	@SuppressWarnings("rawtypes")
	private final Class raw;
	private final Type[] args;

	public ParameterizedTypeImpl(@SuppressWarnings("rawtypes") Class raw, Type[] args) {
		this.raw = raw;
		this.args = args != null ? args : new Type[0];
	}

	@Override
	public Type[] getActualTypeArguments() {
		return args;
	}

	@Override
	public Type getRawType() {
		return raw;
	}

	@Override
	public Type getOwnerType() {
		return null;
	}
}