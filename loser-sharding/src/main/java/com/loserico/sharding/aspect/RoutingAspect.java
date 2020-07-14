package com.loserico.sharding.aspect;

import com.loserico.sharding.annotation.Router;
import com.loserico.sharding.core.RoutingStrategy;
import com.loserico.sharding.dynamic.DataSourceHolder;
import com.loserico.sharding.enumeration.RoutingErrors;
import com.loserico.sharding.exception.RoutingFieldException;
import com.loserico.sharding.exception.RoutingStategyUnmatchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 拦截切面组件
 * <p>
 * Copyright: (C), 2020/2/14 18:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Component
@Aspect
@Slf4j
public class RoutingAspect {
	
	@Autowired
	private RoutingStrategy routing;
	
	@Pointcut("@annotation(com.loserico.sharding.annotation.Router)")
	public void pointcut() {
	}
	
	@Before("pointcut()")
	public void before(JoinPoint joinPoint) throws RoutingStategyUnmatchException, RoutingFieldException,
			IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		long beginTime = System.currentTimeMillis();
		
		Method method = getInvokeMethod(joinPoint);
		//获取方法上标注的@Router注解
		Router router = method.getAnnotation(Router.class);
		//获取路由key
		String routingField = router.routingField();
		
		//入参
		Object[] args = joinPoint.getArgs();
		
		boolean havingRoutingField = false;
		
		if (args == null || args.length == 0) {
			return;
		}
		
		for (int i = 0; i < args.length; i++) {
			String routingFieldValue = BeanUtils.getProperty(args[i], routingField);
			if (StringUtils.isEmpty(routingFieldValue)) {
				continue;
			}
			
			String dataSourceKey = routing.dataSourceKey(routingFieldValue);
			String tableKey = routing.tableKey(routingFieldValue);
			log.info("选择的DataSource key是:{}, Table Key是:{}", dataSourceKey, tableKey);
			havingRoutingField = true;
			break;
		}
		
		//判断入参中没有路由字段
		if (!havingRoutingField) {
			log.warn("入参{}中没有包含路由字段:{}", args, routingField);
			throw new RoutingFieldException(RoutingErrors.NO_ROUTING_FIELD);
		}
	}
	
	/**
	 * 清除线程缓存
	 *
	 * @param joinPoint
	 */
	@After("pointcut()")
	public void after(JoinPoint joinPoint) {
		DataSourceHolder.clearDataSourceKey();
		DataSourceHolder.clearTableKey();
	}
	
	/**
	 * 获取调用方法的名称
	 *
	 * @param joinPoint
	 * @return Method
	 */
	private Method getInvokeMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getMethod();
	}
}
