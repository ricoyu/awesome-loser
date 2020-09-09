package com.loserico.common.lang.utils;

import java.util.Map;

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
public final class Maps {
	
	/**
	 * 如果map为null或者没有任何element, 返回true, 否则返回false
	 * @return boolean
	 */
	public static boolean isEmpty(Map map) {
		return map == null || map.isEmpty();
	}
}
