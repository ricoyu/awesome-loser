package com.loserico.validation.validation.annotation;

import com.loserico.validation.validation.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证两次输入的密码是否一致 
 * <p>
 * Copyright: Copyright (c) 2020-08-18 10:14
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

	String password() default "password";
	
	String passwordRepeat() default "passwordRepeat";
	
	String message() default "Password not match";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String[] value() default "";
}
