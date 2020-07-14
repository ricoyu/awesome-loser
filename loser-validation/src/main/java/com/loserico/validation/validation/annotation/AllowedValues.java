package com.loserico.validation.validation.annotation;

import com.loserico.validation.validation.AllowedValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证只接受给定的值 
 * <p>
 * Copyright: Copyright (c) 2019-10-14 13:57
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedValueValidator.class)
@Documented
public @interface AllowedValues {

	/**
	 * Allowed candidate values, default to all values
	 * @return String[]
	 */
	String[] value() default {};
	
	/**
	 * All values are allowed except values provided here
	 * @return String[]
	 */
	String[] except() default {};
	
	/**
	 * Should each value in value and except be checked in case sensitive mode? default false
	 * @return boolean
	 */
	boolean caseSensitive() default false;
	
	//control if this field is mandatory
	boolean mandatory() default true;

	String message() default "Provided value is not valid.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
