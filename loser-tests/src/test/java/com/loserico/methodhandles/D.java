package com.loserico.methodhandles;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class D {

	public static void main(String[] args) throws Throwable {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle concat = lookup.findVirtual(String.class, "concat", MethodType.methodType(String.class, String.class));
		String result = (String) concat.invokeExact("x", "y");
		System.out.println("xy".equals(result));

		MethodType bigType = concat.type().insertParameterTypes(0, int.class, String.class);
		MethodHandle d0 = MethodHandles.dropArguments(concat, 0, bigType.parameterList().subList(0, 2));
		System.out.println(bigType == d0.type());
		System.out.println(bigType.equals(d0.type()));
		System.out.println("yz".equals((String) d0.invokeExact(123, "x", "y", "z")));
	}
}
