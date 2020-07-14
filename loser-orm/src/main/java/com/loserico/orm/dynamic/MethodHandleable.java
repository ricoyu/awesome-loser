package com.loserico.orm.dynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public interface MethodHandleable {

	default public Map<Class<?>, MethodHandle> toMethodHandleCache() {
		Method[] methods = getClass().getDeclaredMethods();
		Map<Class<?>, MethodHandle> methodHandleCache = new HashMap<Class<?>, MethodHandle>();
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		try {
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				methodHandleCache.put(method.getParameterTypes()[0], lookup.unreflect(method));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("创建MethodHandle出错！", e);
		}
		return methodHandleCache;
	}
}
