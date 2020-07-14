package com.loserico.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.common.lang.context.ApplicationContextHolder;
import com.loserico.common.lang.utils.ReflectionUtils;

/**
 * ObjectMapper 工厂类
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:01
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ObjectMapperFactory {

	private static volatile ObjectMapper objectMapper;
	
	/**
	 * 如果是Spring环境, 优先从Spring容器中取ObjectMapper, 如果没有, 则自己创建一个
	 * @return ObjectMapper
	 */
	public static ObjectMapper createOrFromBeanFactory() {
		if (objectMapper == null) {
			synchronized (ObjectMapperFactory.class) {
				if (objectMapper == null) {
					boolean exists = ReflectionUtils.existsClass("org.springframework.context.ApplicationContext");
					if (exists) {
						objectMapper = (ObjectMapper) ReflectionUtils.invokeStatic(ApplicationContextHolder.class, "getBean", ObjectMapper.class);
					}
					if (objectMapper == null) {
						objectMapper = new ObjectMapper();
						return objectMapper;
					}
					return objectMapper;
				}
				
				return objectMapper;
			}
		}
		
		return objectMapper;
	}
}