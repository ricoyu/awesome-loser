package com.loserico.common.lang.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-16 13:50
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class BeanUtils {

	/**
	 * 从source拷贝到target, 不包括值为null的属性
	 * 
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source, Object target) {
		copyProperties(source, target, true);
	}

	/**
	 * 根据class创建相应对象，从source拷贝到target<br/>
	 * 拷贝所有，不包括值为null的属性<br/>
	 * 该类需要有一个默认构造函数<p>
	 * 
	 * @param source
	 * @param clazz
	 * @on
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyProperties(Object source, Class<? super T> clazz) {
		T target = null;
		try {
			target = (T) clazz.newInstance();
			copyProperties(source, target, true);
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("msg", e);
		}
		return target;
	}

	/**
	 * 从 sources 中取出元素挨个拷贝属性
	 * 根据class创建相应对象，从source拷贝到target，拷贝所有，不包括值为null的属性
	 * 
	 * 该类需要有一个默认构造函数
	 * 
	 * @param sources
	 * @param clazz
	 * @on
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> copyProperties(List<?> sources, Class<? super T> clazz) {
		List<T> results = new ArrayList<>();
		for (Object source : sources) {
			results.add((T) copyProperties(source, clazz));
		}

		return results;
	}

	/**
	 * 从 sources 中取出元素挨个拷贝属性
	 * 根据class创建相应对象，从source拷贝到target，不包括值为null的属性
	 * 
	 * 该类需要有一个默认构造函数
	 * 
	 * @param sources
	 * @param clazz
	 * @param ignoreProperties
	 * @on
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> copyProperties(List<?> sources, Class<? super T> clazz, String... ignoreProperties) {
		List<T> results = new ArrayList<>();
		for (Object source : sources) {
			results.add((T) copyProperties(source, clazz, ignoreProperties));
		}

		return results;
	}

	/**
	 * 从source拷贝到target，可以指定忽略哪些属性，不包括值为null的属性
	 * 
	 * @param source
	 * @param target
	 * @param ignoreProperties
	 */
	public static void copyProperties(Object source, Object target, String... ignoreProperties) {
		copyProperties(source, target, true, ignoreProperties);
	}

	/**
	 * 从source拷贝到target，可以指定忽略哪些属性，不包括值为null的属性
	 * 
	 * @param source
	 * @param clazz
	 * @param ignoreProperties
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyProperties(Object source, Class<? super T> clazz, String... ignoreProperties) {
		T target = null;
		try {
			target = (T) clazz.newInstance();
			copyProperties(source, target, true, ignoreProperties);
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("msg", e);
		}
		return target;
	}

	/**
	 * 拷贝source到target，不拷贝指定的属性
	 * 
	 * @param source
	 * @param clazz
	 * @param ignoreNull
	 * @param ignoreProperties
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyProperties(Object source, Class<? super T> clazz, boolean ignoreNull,
			String... ignoreProperties) {
		T target = null;
		try {
			target = (T) clazz.newInstance();
			copyProperties(source, target, ignoreNull, ignoreProperties);
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("msg", e);
		}
		return target;
	}


	/**
	 * 拷贝source到target，不拷贝指定的属性
	 *
	 * @param source
	 * @param target
	 * @param ignoreNull
	 * @param ignoreProperties
	 */
	public static void copyProperties(Object source, Object target, boolean ignoreNull, String... ignoreProperties) {

		Objects.requireNonNull(source, "Source must not be null");
		Objects.requireNonNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if (readMethod != null &&
							ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(source);
							//如果指定了ignoreNull，则不拷贝值为null的属性
							if (ignoreNull && value == null) {
								continue;
							}
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						} catch (Throwable ex) {
							throw new FatalBeanException(
									"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
						}
					}
				}
			}
		}
	}
}
