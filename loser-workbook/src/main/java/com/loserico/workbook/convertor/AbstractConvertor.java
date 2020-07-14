package com.loserico.workbook.convertor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * 将Cell中读到的数据转换成POJO中对应字段的数据类型, 提供一些公共属性, 方法给子类使用
 * 
 * <p>
 * Copyright: Copyright (c) 2019-05-09 21:22
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 * @param <S>
 * @param <T>
 */
@Data
@Slf4j
public abstract class AbstractConvertor<S, T> implements Convertor<S, T> {

	private Field field;

	/**
	 * 保存了Cell所在列名, 没有的话则为null
	 */
	private String columnName;

	/**
	 * Cell的索引, 从0开始
	 */
	private int cellIndex;
}
