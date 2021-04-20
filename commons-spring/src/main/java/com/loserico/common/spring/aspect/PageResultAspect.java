package com.loserico.common.spring.aspect;

import com.loserico.common.lang.utils.CollectionUtils;
import com.loserico.common.lang.utils.PrimitiveUtils;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.common.lang.vo.Page;
import com.loserico.common.lang.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * 分页查询时, 在返回结果前, 将分页信息写入Result对象
 * <p>
 * Copyright: (C), 2021-04-17 19:48
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
@Aspect
public class PageResultAspect {
	
	/**
	 * 分页方法需要持有一个Page类型参数, 或者第一个参数VO里面包含一个Page类型的字段page
	 *
	 * @param joinPoint
	 */
	@AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.PostMapping)", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		Object[] args = joinPoint.getArgs();
		//没参数就不需要处理了
		if (args == null || args.length == 0) {
			return;
		}
		
		//返回值类型必须是Result才需要拦截
		if (!(result instanceof Result)) {
			return;
		}
		
		//先看参数列表中是否有一个是Page类型
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if (arg instanceof Page) {
				setPage(result, arg);
				return;
			}
		}
		
		//如果参数列表中没有Page类型的参数, 那么查看第一个参数中是否有一个Page类型的属性
		Object firstParam = args[0];
		//参数值为null, 不需要处理
		if (firstParam == null) {
			return;
		}
		//参数是基本类型, 不需要处理
		if (PrimitiveUtils.isPrimitive(firstParam)) {
			return;
		}
		//集合类型不需要处理
		if (CollectionUtils.isCollection(firstParam)) {
			return;
		}
		
		Object page = ReflectionUtils.getField(firstParam, "page");
		if (page == null || !(page instanceof Page)) {
			return;
		}
		
		setPage(result, page);
	}
	
	private void setPage(Object result, Object page) {
		Result resultObj = (Result) result;
		Page pageObj = (Page) page;
		resultObj.setPage(pageObj);
	}
	
	public static void main(String[] args) {
		System.out.println(null instanceof Page);
	}
}
