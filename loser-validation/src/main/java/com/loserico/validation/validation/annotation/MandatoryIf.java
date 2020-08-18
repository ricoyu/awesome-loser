package com.loserico.validation.validation.annotation;

import com.loserico.validation.validation.MandatoryIfValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果referenceField的值等于referenceValue, 那么mandatoryField为必填项
 * <p>
 * Copyright: Copyright (c) 2020-08-18 10:17
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy= MandatoryIfValidator.class)
@Repeatable(value=MandatoryIfs.class)
@Documented
public @interface MandatoryIf {

	/**
	 * Bean中哪个属性是必填的
	 * @return
	 */
	String mandatoryField();
	
	/**
	 * 参考字段的名字。即这个参考字段值满足什么条件必填字段才被验证为必填
	 * @return
	 */
	String referenceField();
	
	/**
	 * 参考字段的值，不提供则只验证参考字段不为null
	 * @return
	 */
	String referenceValue() default "";
	
	String message() default "Mandatory.";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String[] value() default "";
}
