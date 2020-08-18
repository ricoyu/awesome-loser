package com.loserico.validation.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 支持多个UniqueEntity 
 * <p>
 * Copyright: Copyright (c) 2020-08-18 10:20
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface UniqueEntities {
	UniqueEntity[] value();
}
