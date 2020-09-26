package com.loserico.common.lang.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
@Slf4j
public class ApplicationContextHolder implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
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
}
