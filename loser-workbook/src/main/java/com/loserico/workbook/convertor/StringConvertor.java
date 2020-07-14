package com.loserico.workbook.convertor;

import java.util.function.Function;

/**
 * 将Cell中读到的数据转换成字符串类型
 * <p>
 * Copyright: Copyright (c) 2019-05-23 10:10
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class StringConvertor implements Convertor<Object, String> {
	
	private volatile Function<Object, String> convertFunction;

	@Override
	public String convert(Object source) {
		if (source == null) {
			return null;
		}
		
		if (convertFunction != null) {
			return convertFunction.apply(source);
		}

		if (source instanceof String) {
			convertFunction = obj -> ((String) source).trim();
			return convertFunction.apply(source);
		}
		
		return source.toString();
	}

}
