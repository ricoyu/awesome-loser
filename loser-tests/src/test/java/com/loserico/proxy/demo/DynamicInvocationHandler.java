package com.loserico.proxy.demo;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * A simple proxy that doesn't actually do anything except printing
 * what method was requested to be invoked and return a hard-coded number.
 *
 * <p>
 * Copyright: (C), 2021-01-15 21:13
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class DynamicInvocationHandler implements InvocationHandler {
	
	/**
	 * Here we've defined a simple proxy that logs which method was invoked and returns 42.
	 *
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		log.info("Invoke method: {}", method.getName());
		return 42;
	}
}
