package com.loserico.workbook.convertor;

/**
 * 负责将Cell中读到的数据转换成POJO中对应字段的数据类型
 * <p>
 * Copyright: Copyright (c) 2019-05-09 21:11
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface Convertor<S, T> {

	/**
	 * 将对象从S转换成T类型
	 * @param source
	 * @return
	 */
	T convert(S source);
}
