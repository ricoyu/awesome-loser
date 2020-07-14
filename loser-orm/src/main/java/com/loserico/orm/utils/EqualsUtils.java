package com.loserico.orm.utils;

import java.math.BigDecimal;

public final class EqualsUtils {

	/**
	 * <ul><h2>比较规则</h2>
	 * <li>prev == null, next == null	==> true
	 * <li>prev.equals(next)
	 * </ul>
	 * @param prev
	 * @param next
	 * @return
	 */
	public static boolean equals(Object prev, Object next) {
		if (prev == null && next == null) {
			return true;
		}
		if (prev == null || next == null) {
			return false;
		}
		
		return prev.equals(next);
	}
	
	/**
	 * <ul><h2>比较规则</h2>
	 * <li>prev == null, next == null	==> true
	 * <li>如果都是BigDecimal类型, 在误差(error)范围内认为是equal的
	 * <li>prev.equals(next)
	 * </ul>
	 * @param prev
	 * @param next
	 * @param error 财务上两个BigDecimal相比较, 允许出现的误差值, 在误差值范围内认为是equal的
	 * @return
	 */
	public static boolean equals(Object prev, Object next, BigDecimal error) {
		if (prev == null && next == null) {
			return true;
		}
		if (prev == null || next == null) {
			return false;
		}

		if (prev instanceof BigDecimal && next instanceof BigDecimal) {
			BigDecimal a = (BigDecimal) prev;
			BigDecimal b = (BigDecimal) next;
			return a.subtract(b.subtract(error)).signum() >= 0 && a.subtract(b.add(error)).signum() <= 0;
		}

		return prev.equals(next);
	}
	
	
}
