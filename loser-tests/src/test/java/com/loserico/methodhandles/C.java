package com.loserico.methodhandles;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class C {

	public String sayHello(String name) {
		String message = "Hello " + name;
		System.out.println(message);
		return message;
	}

	public static void main(String[] args) {
		try {
			MethodHandle sayHelloHandle = MethodHandles.lookup().findVirtual(C.class, "sayHello",
					MethodType.methodType(String.class, String.class));
			System.out.println(sayHelloHandle);
			MethodHandle ready = sayHelloHandle.bindTo(new C());
			ready.invokeWithArguments("rico");
			ready.invokeWithArguments("zaizai");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}