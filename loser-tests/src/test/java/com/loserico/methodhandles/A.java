package com.loserico.methodhandles;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public interface A {
	String amplify(Number a);

	Number getA();

	String toString();

	default String zzz() {
		System.out.println("Sleeping: zzzz");
		return "42";
	};

	public static void main(String[] args) {
		//newProxyInstance的第三个参数是用lambda表达式创建的InvocationHandler实例
		A a = (A) Proxy.newProxyInstance(C.class.getClassLoader(), new Class[] { A.class }, (proxy, method, arguments) -> {
			System.out.println("Proxying: " + method.getName() + " " + Arrays.toString(arguments));
			return "Success";
		});

		/**
		 * When something calls a method on our proxy, like a.amplify(0), the invocation handler will receive the control flow. 
		 * And we can decide how to handle the method call. 
		 * A good practice usually is to provide an actual instance of the interface to which you can delegate all the work.
		 */
		a.amplify(0);//输出Proxying: amplify [0]
	}
}