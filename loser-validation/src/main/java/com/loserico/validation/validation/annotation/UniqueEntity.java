package com.loserico.validation.validation.annotation;

import com.loserico.validation.service.UniqueEntityService;
import com.loserico.validation.validation.UniqueEntityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 根据fieldNames指定的几个字段查询数据库, 查出来的记录应该是唯一的
 * 如果提供了primaryKey, 那么查询的时候要排除自己
 * isSoftDelete, 如果有软删除的数据, 那么查询的时候还要抛出isSoftDelete=true的数据
 * 
 * @author Rico Yu ricoyu520@gmail.com
 * @since 2017-06-05 17:29
 * @version 1.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = UniqueEntityValidator.class)
@Repeatable(value = UniqueEntities.class)
@Documented
public @interface UniqueEntity {

	/**
	 * 要检查唯一性的数据库表字段名
	 * 
	 * @return
	 */
	String[] fieldNames();

	/**
	 * fieldNames对应的Bean属性名
	 * 
	 * @return
	 */
	String[] properties();

	/**
	 * Bean中持有主键的属性名, 默认为id, 表的主键名默认为ID
	 * 在检查唯一性的时候, 如果带主键, 那么要排除自己
	 * 
	 * @return
	 */
	String primaryKey() default "id";
	
	/**
	 * <blockquote><pre>
	 * 表设计是否采用了软删除，默认为true
	 * 即一个已经存在的被软删除的对象不会影响数据校验的结果
	 * </pre></blockquote>
	 */
	boolean isSoftDelete() default true;
	
	/**
	 * 查数据库的bean class, 需实现UniqueEntityService接口
	 * @return
	 */
	Class<? extends UniqueEntityService> serviceBean();

	String message() default "Entity already exists.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String[] value() default "";
}