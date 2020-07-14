package com.loserico.common.lang.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * 泛型相关的反射操作
 * <p>
 * Copyright: (C), 2020/4/20 10:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class GenericUtils {
	
	/**
	 * 获取cls的第一个泛型参数
	 * @param cls
	 * @return
	 */
	public static Class<?> getTypeArgument(Class<?> cls) {
		Type[] types = cls.getGenericInterfaces();
		ParameterizedType target = null;
		for (Type type : types) {
			if (type instanceof ParameterizedType) {
				Type[] typeArray = ((ParameterizedType) type).getActualTypeArguments();
				if (ArrayUtils.isNotEmpty(typeArray)) {
					for (Type t : typeArray) {
						if (t instanceof TypeVariable || t instanceof WildcardType) {
							break;
						} else {
							target = (ParameterizedType) type;
							break;
						}
					}
				}
				break;
			}
		}
		return target == null ? null : (Class<?>) target.getActualTypeArguments()[0];
	}
}
