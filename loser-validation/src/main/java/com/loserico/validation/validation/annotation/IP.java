package com.loserico.validation.validation.annotation;

import com.loserico.validation.enums.IPCategory;
import com.loserico.validation.validation.IPValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.loserico.validation.enums.IPCategory.UN_RESTRICTED;

/**
 * IP规则校验<br/>
 * 支持IPv4, IPv6校验<br/>
 * 本规则不对数据做必填校验, 所以数据为空的话跳过验证, 相当于验证通过<br/>
 * <p>
 * Copyright: Copyright (c) 2021-02-02 16:53
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = IPValidator.class)
public @interface IP {
	
	/**
	 * 可以直接给出验证错误消息, 也可以给出国际化消息模板, 如{ip.notvalid}
	 * @return String
	 */
	String message() default "IP 地址不合法";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
	/**
	 * IP地址类型, 可以指定这必须是一个IPv4地址或者一个IPv6地址, 或者不限制
	 * @return
	 */
	IPCategory category() default UN_RESTRICTED;
}
