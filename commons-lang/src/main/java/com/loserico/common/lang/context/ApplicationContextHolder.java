package com.loserico.common.lang.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 获取ApplicationContext对象的帮助类
 * 需要定义Spring Bean ApplicationContextHolder，以注入applicationContext
 * <p>
 * Copyright: Copyright (c) 2019-10-14 15:16
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ApplicationContextHolder implements ApplicationContextAware {
	
	private static final Logger log = LoggerFactory.getLogger(ApplicationContextHolder.class);
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * 判断是否在Spring环境
	 *
	 * @return
	 */
	public static boolean isSpringApp() {
		return applicationContext != null;
	}
	
	public static Object getBean(String beanName) {
		if (applicationContext == null) {
			log.warn("applicationContext is null, consider add bean of ApplicationContextHolder type or add dependency loser-spring-boot-starter");
			return null;
		}
		return applicationContext.getBean(beanName);
	}
	
	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (applicationContext == null) {
			log.warn("applicationContext is null, consider add bean of ApplicationContextHolder type or add dependency loser-spring-boot-starter");
			return null;
		}
		return applicationContext.getBean(beanName, clazz);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		if (applicationContext == null) {
			log.warn("applicationContext is null, consider add bean of ApplicationContextHolder type or add dependency loser-spring-boot-starter");
			return null;
		}
		return applicationContext.getBean(clazz);
	}
	
	public static <T> List<T> getBeans(Class<T> clazz) {
		if (applicationContext == null) {
			log.warn("applicationContext is null, consider add bean of ApplicationContextHolder type or add dependency loser-spring-boot-starter");
			return null;
		}
		Map<String, T> beansMap = applicationContext.getBeansOfType(clazz);
		return beansMap.values().stream().collect(toList());
	}
	
	public static String getProperty(String propertyName) {
		if (applicationContext != null) {
			return applicationContext.getEnvironment().getProperty(propertyName);
		}
		return null;
	}
}
