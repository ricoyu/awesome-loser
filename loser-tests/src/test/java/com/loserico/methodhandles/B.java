package com.loserico.methodhandles;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class B {

	public static void main(String[] args) {
		A a = (A) Proxy.newProxyInstance(B.class.getClassLoader(), new Class[] { A.class }, (proxy, method, arguments) -> {
			if (method.isDefault()) {
				final Class<?> declaringClass = method.getDeclaringClass();
				Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class,
						int.class);
				constructor.setAccessible(true);
				return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
						.unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(arguments);
			}
			System.out.println("Proxying: " + method.getName() + " " + Arrays.toString(args));
			return "Success";
		});
		a.amplify(1);
		a.zzz();
	}
}
