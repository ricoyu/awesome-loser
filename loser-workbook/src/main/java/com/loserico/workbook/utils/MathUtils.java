package com.loserico.workbook.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:42
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class MathUtils {

	/**
	 * 为Double类型四舍五入保留小数点后precision位
	 * 
	 * @param v
	 * @param precision
	 * @return Double
	 */
	public static Double formatDouble(Double v, int precision) {
		if (v == null) {
			return null;
		}
		if (precision < 0) {
			throw new IllegalArgumentException("precision不能为负数");
		}
		BigDecimal b = new BigDecimal(v);
		BigDecimal one = new BigDecimal("1");
		double value = b.divide(one, precision, BigDecimal.ROUND_HALF_UP).doubleValue();
		StringBuilder format = new StringBuilder("0");
		if (precision > 0) {
			format.append(".");
			for (int i = 0; i < precision; i++) {
				format.append("0");
			}
		}
		DecimalFormat df = new DecimalFormat(format.toString());
		return Double.parseDouble(df.format(value));
	}
}