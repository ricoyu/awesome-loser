package com.loserico.workbook.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * POJO中的字段与Excel中的某列映射
 * <p>
 * Copyright: Copyright (c) 2019-05-23 14:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Col {
	
	/**
	 * 要映射的Excel列名
	 *
	 * @return Excel列名
	 */
	String name() default "";
	
	/**
	 * 实际操作中经常会同样的数据, 出现多个版本的字段名. 比如某列今天叫单据号, 一段时间后改成业务单号
	 * fallback()的作用就是在找不到name()指定的列名时, 转而去找fallback()指定的列
	 *
	 * @return String
	 */
	String fallback() default "";
	
	/**
	 * 有时可能不想用列名去匹配, 比如我确定这个字段要映射到第一列.
	 * 如果指定了index, 那么name和fallback就忽略了
	 *
	 * @return int
	 */
	int index() default -1;
	
	/**
	 * 如果该字段是enum类型, 并且Enum中定义了property, 而Excel中这一列的值正好是要匹配Enum对象的某个property
	 * 这里需要指定property的名字
	 * 如果这里匹配的是Enum的orginal或者name则不需要指定
	 * @return
	 */
	String enumProperty() default "";
}
