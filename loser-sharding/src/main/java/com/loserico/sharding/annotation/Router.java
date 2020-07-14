package com.loserico.sharding.annotation;

import com.loserico.sharding.constant.RoutingConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由注解
 * <p>
 * Copyright: Copyright (c) 2020-02-14 18:34
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Router {
	
	/**
	 * 路由字段
	 * @return
	 */
	String routingField() default RoutingConstant.DEFAULT_ROUTING_FIELD;
}
