package com.loserico.json.jackson.serializer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在序列化Enum类型时,如果有国际化需求, 可以加上这个注解 
 * <p>
 * Copyright: (C), 2021-08-13 10:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumI18N {
	
	/**
	 * 标记Enum对象的哪个属性记录了国际化消息模板, 序列化时通过反射获取
	 * @return
	 */
	String property();
	
	/**
	 * 如果根据property指定的属性获取不到国际化消息, 则回退到使用默认消息, 该默认消息由fallbackTo指定的属性提供
	 * @return
	 */
	String fallbackTo() default "";
	
}
