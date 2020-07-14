package com.loserico.proxy;

import java.lang.reflect.Proxy;

/**
 * <p>
 * Copyright: (C), 2020/3/2 15:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProxyDemo {
	
	public static void main(String[] args) {
		Foo f = (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(),
				new Class[]{Foo.class},
				new FooProxy(new FooImpl()));
		f.hello("你好");
	}
}
