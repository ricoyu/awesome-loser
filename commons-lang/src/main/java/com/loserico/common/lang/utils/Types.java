package com.loserico.common.lang.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-17 15:16
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class Types {

	private static final Map<String, ArrayTypes> arrayTypeMap = new ConcurrentHashMap<>();
	
	static {
		arrayTypeMap.put(ArrayTypes.LONG.getClassName(), ArrayTypes.LONG);
		arrayTypeMap.put(ArrayTypes.LONG_WRAPPER.getClassName(), ArrayTypes.LONG_WRAPPER);
		arrayTypeMap.put(ArrayTypes.INTEGER.getClassName(), ArrayTypes.INTEGER);
		arrayTypeMap.put(ArrayTypes.INTEGER_WRAPPER.getClassName(), ArrayTypes.INTEGER_WRAPPER);
		arrayTypeMap.put(ArrayTypes.STRING.getClassName(), ArrayTypes.STRING);
		arrayTypeMap.put(ArrayTypes.DOUBLE.getClassName(), ArrayTypes.DOUBLE);
		arrayTypeMap.put(ArrayTypes.DOUBLE_WRAPPER.getClassName(), ArrayTypes.DOUBLE_WRAPPER);
		arrayTypeMap.put(ArrayTypes.FLOAT.getClassName(), ArrayTypes.FLOAT);
		arrayTypeMap.put(ArrayTypes.FLOAT_WRAPPER.getClassName(), ArrayTypes.FLOAT_WRAPPER);
	}
	
	public static ArrayTypes arrayTypes(Object value) {
		if(value == null) {
			return null;
		}
		return arrayTypeMap.get(value.getClass().getName());
	}
}