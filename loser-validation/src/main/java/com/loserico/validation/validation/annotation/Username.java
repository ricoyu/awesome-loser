package com.loserico.validation.validation.annotation;

import com.loserico.validation.validation.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证用户名是否合法 
 * 可以包含字母、数字、下划线、@符号，必须以字母或者数字开头
 * 
 * <p>
 * Copyright: Copyright (c) 2020-08-18 10:16
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= UsernameValidator.class)
@Documented
public @interface Username {

	String message() default "Please enter valid username";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
