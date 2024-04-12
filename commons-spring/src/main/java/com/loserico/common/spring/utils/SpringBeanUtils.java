package com.loserico.common.spring.utils;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.Enhancer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理Spring容器中bean的工具类
 * <p>
 * Copyright: (C), 2024-01-25 17:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class SpringBeanUtils {
	
	/**
	 * 实测如果一个类加了@Transactional注解, 那么会有两个bean实例, 一个是代理类的实例, 一个是原始类的实例
	 * 这个方法把原始类的实例过滤掉, 只保留代理类的实例
	 * @param Collection<Object> beans
	 * @return
	 */
	public static Collection<Object> distinctBeans(Collection<Object> allBeans) {
		Map<String, Object> filteredBeans = new HashMap<>();
		
		for (Object bean : allBeans) {
			Class<?> beanClass = bean.getClass();
			String simpleName = beanClass.getSimpleName();
			int index = simpleName.indexOf("$$");
			String name = simpleName.substring(0, index == -1 ? simpleName.length() : index);
			// 检查当前bean是否是代理（CGLIB或JDK动态代理）
			if (!filteredBeans.containsKey(name)) {
				filteredBeans.put(name, bean);
			} else {
				// 如果是代理, 直接塞进去
				if (index != -1) {
					filteredBeans.put(name, bean);
				}
			}
		}
		
		for (Object obj : filteredBeans.values()) {
			if (obj.getClass().getName().indexOf("PlcStatus") != -1) {
				System.out.println(obj.getClass().getName());
			}
		}
		return filteredBeans.values();
	}
	
	/**
	 * 实测如果一个类加了@Transactional注解, 那么会有两个bean实例, 一个是代理类的实例, 一个是原始类的实例
	 * 这个方法把原始类的实例过滤掉, 只保留代理类的实例
	 * @param Collection<Object> beans
	 * @return
	 */
	public static Collection<Object> filterOutProxiedBeans(Collection<Object> allBeans) {
		Map<Class<?>, Object> filteredBeans = new HashMap<>();
		
		for (Object bean : allBeans) {
			Class<?> beanClass = bean.getClass();
			
			// 检查当前bean是否是代理（CGLIB或JDK动态代理）
			if (bean instanceof Advised || Enhancer.isEnhanced(beanClass)) {
				// 获取代理的目标类
				Class<?> targetClass = findTargetClass(bean);
				filteredBeans.put(targetClass, bean);
			} else {
				// 如果不是代理，且之前没有存储过代理
				if (!filteredBeans.containsKey(beanClass)) {
					filteredBeans.put(beanClass, bean);
				}
			}
		}
		
		for (Object obj : filteredBeans.values()) {
			if (obj.getClass().getName().indexOf("PlcStatus") != -1) {
				System.out.println(obj.getClass().getName());
			}
		}
		return filteredBeans.values();
	}
	
	private static Class<?> findTargetClass(Object proxy) {
		if (AopUtils.isAopProxy(proxy) && proxy instanceof Advised) {
			try {
				Object target = ((Advised) proxy).getTargetSource().getTarget();
				return target != null ? target.getClass() : proxy.getClass();
			} catch (Exception e) {
				throw new RuntimeException("Failed to get target class from proxy", e);
			}
		}
		return proxy.getClass();
	}
}
