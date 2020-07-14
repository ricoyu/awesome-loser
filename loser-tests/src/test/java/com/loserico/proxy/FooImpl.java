package com.loserico.proxy;

/**
 * <p>
 * Copyright: (C), 2020/3/2 15:25
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FooImpl implements Foo {
	
	@Override
	public void hello(String message) {
		System.out.println(message);
	}
}
