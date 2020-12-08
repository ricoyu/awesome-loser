package com.loserico.mongo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记这是一个MongoDB字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoField {
	
	/**
	 * 对应MongoDB字段名
	 * @return String
	 */
	String value() default "";
}
