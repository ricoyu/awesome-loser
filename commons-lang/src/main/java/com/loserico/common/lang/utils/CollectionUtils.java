package com.loserico.common.lang.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集合操作工具类
 * <p>
 * Copyright: Copyright (c) 2019-10-14 16:56
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class CollectionUtils {
	
	/**
	 * 判断集合不为null并且包含至少一个元素
	 *
	 * @param collection
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Collection collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public static boolean isEmpty(Collection collection) {
		return collection == null || collection.isEmpty();
	}
	
	/**
	 * 判断对象是否为集合类型, 如 Map, List, Set, 数组
	 * @param obj
	 * @return boolean
	 */
	public static boolean isCollection(Object obj) {
		if (obj == null) {
			return false;
		}
		
		Class<?> clazz = obj.getClass();
		if (clazz.isArray()) {
			return true;
		}
		
		if (obj instanceof Map) {
			return true;
		}
		
		if (obj instanceof List) {
			return true;
		}
		
		if (obj instanceof Set) {
			return true;
		}
		
		if (obj instanceof Collection) {
			return true;
		}
		
		return false;
	}
}
