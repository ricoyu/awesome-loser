package com.loserico.searchlegacy.cache;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * Copyright: (C), 2021-01-16 8:49
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FieldCache {
	
	/**
	 * 缓存Class对象与定义在这个Class对象里面的标注了@DocId的字段
	 */
	private ConcurrentMap<Class, CacheItem> cache = new ConcurrentHashMap<>();
	
	public void put(Class clazz, Field field) {
		cache.putIfAbsent(clazz, new CacheItem(field));
	}
	
	public CacheItem get(Class clazz) {
		return cache.get(clazz);
	}
	
	public static class CacheItem {
		
		private Field field;
		
		public CacheItem(Field field) {
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}
	}
}
