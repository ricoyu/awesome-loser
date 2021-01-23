package com.loserico.proxy.demo;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2021-01-15 21:15
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DynamicProxyDemo {
	
	public static void main(String[] args) {
		Map proxyInstance = (Map) Proxy.newProxyInstance(
				DynamicProxyDemo.class.getClassLoader(),
				new Class[]{Map.class},
				new DynamicInvocationHandler()
		);
		
		Integer value = (Integer) proxyInstance.put("name", "三少爷");
		System.out.println(value);
	}
}
