package com.loserico.common.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Annotation that marks a method to be executed after initialize complete.<br/>
 * Initialize complete means all properties of this bean is set and transaction is ready.
 * @author xuehuyu
 * @since Mar 19, 2015
 * @version 1.0
 *
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostInitialize {
    /**
     * Defines the order used to initialize the method
     *
     * @return The order
     * @since 1.0
     */
    int order() default 0;
    
    /**
     * 分组执行，每组内的按照order依次执行
     * @return
     */
    String group() default "";
    
    /**
     * 默认立即执行
     * @return
     */
    int delay() default 0;
    
    /**
     * 延迟执行的单位，默认分钟
     * @return
     */
    TimeUnit delayUnit() default MINUTES;
}