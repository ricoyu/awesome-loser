package com.loserico.common.lang.magic;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * <p>
 * Copyright: (C), 2019/11/15 9:07
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class UnsafeInstance {
	
	private UnsafeInstance() {
	}
	
	/**
	 * 通过反射获取Unsafe实例
	 * @return
	 */
	public static Unsafe get() {
		try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			return (Unsafe) field.get(null);
		} catch (Exception e) {
			log.error("", e);
		}
		
		return null;
	}
}
