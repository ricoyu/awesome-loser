package com.loserico.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * <p>
 * Copyright: (C), 2022-01-24 11:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CglibProxyDemo {
	
	static class Original {
		
		public void originalMethod(String s) {
			System.out.println(s);
		}
	}
	
	static class Handler implements MethodInterceptor {
		
		private final Original original;
		
		public Handler(Original original) {
			this.original = original;
		}
		
		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			System.out.println("BEFORE");
			method.invoke(original, args);
			System.out.println("AFTER");
			return null;
		}
	}
	
	public static void main(String[] args) {
		Original original = new Original();
		MethodInterceptor handler = new Handler(original);
		Original proxyObj = (Original)Enhancer.create(Original.class, handler);
		proxyObj.originalMethod("hello, guys~");
	}
}
