package com.loserico.common.lang.utils;

/**
 * 定义常见的数组类型class名字
 * <p>
 * Copyright: Copyright (c) 2018-06-13 18:14
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public enum ArrayTypes {

	LONG_WRAPPER("[Ljava.lang.Long;"),
	LONG("[J"),
	INTEGER_WRAPPER("[Ljava.lang.Integer;"),
	INTEGER("[I"),
	DOUBLE_WRAPPER("[Ljava.lang.Double;"),
	DOUBLE("[D"),
	FLOAT_WRAPPER("[Ljava.lang.Float;"),
	FLOAT("[F"),
	STRING("[Ljava.lang.String;");

	private String className;

	private ArrayTypes(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

}