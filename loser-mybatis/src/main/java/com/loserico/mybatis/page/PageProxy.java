package com.loserico.mybatis.page;

import com.loserico.common.lang.vo.Page;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Mybatis-Plus Page对象的代理类, 在调用setTotal的时候同时设置Loser's Page
 *
 * <p>
 * Copyright: (C), 2021-04-08 16:06
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PageProxy implements MethodInterceptor {
	
	private Page page;
	
	public PageProxy(Page page) {
		this.page = page;
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (method.getName().equals("setTotal")) {
			page.setTotalCount(((Long) args[0]).intValue());
		}
		
		return proxy.invokeSuper(obj, args);
	}
}
