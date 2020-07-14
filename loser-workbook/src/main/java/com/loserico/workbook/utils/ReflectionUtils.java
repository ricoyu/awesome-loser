package com.loserico.workbook.utils;

import com.loserico.workbook.exception.FieldWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 专为读写Excel定制的反射工具类
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:39
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ReflectionUtils {

	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private static final Map<Class<?>, Field[]> FIELD_CACHE = new ConcurrentReferenceHashMap<Class<?>, Field[]>(256);
	private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentReferenceHashMap<Class<?>, Field[]>(256);
	private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap<Class<?>, Method[]>(256);

	private static final Field[] NO_FIELDS = {};
	private static final Method[] NO_METHODS = {};

	/**
	 * 在pojoType中查找标注了annotationClass注解的字段, 并封装成Field -> annotationClassInstance的形式
	 *
	 * @param <T>
	 * @param pojoType
	 * @param annotationClass
	 * @return Map<Field, T>
	 */
	public static <T extends Annotation> Map<Field, T> annotatedField(Class<?> pojoType, Class<T> annotationClass) {
		Map<Field, T> fieldAnnotationMap = new HashMap<>();
		Field[] fields = getFields(pojoType);
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			T annotation = field.getDeclaredAnnotation(annotationClass);
			fieldAnnotationMap.put(field, annotation);
		}

		return fieldAnnotationMap;
	}

	public static <T> T getFieldValue(String fieldName, Object target) {
		Field field = findField(target.getClass(), fieldName);
		if (field == null) {
			return null;
		}

		try {
			field.setAccessible(true);
			return (T) field.get(target);
		} catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) &&
						(type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static void setField(Field field, Object target, Object value) {
		try {
			makeAccessible(field);
			field.set(target, value);
		} catch (IllegalAccessException ex) {
			throw new FieldWriteException(ex);
		}
	}

	public static Field[] getDeclaredFields(Class<?> clazz) {
		Field[] result = declaredFieldsCache.get(clazz);
		if (result == null) {
			result = clazz.getDeclaredFields();
			declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
		}
		return result;
	}
	
	public static void makeAccessible(Field field) {
		field.setAccessible(true);
	}

	/**
	 * 拿所有的field, 包括父类的field
	 *
	 * @param clazz
	 * @return Field[]
	 */
	public static Field[] getFields(Class<?> clazz) {
		Field[] result = FIELD_CACHE.get(clazz);
		if (result == null) {
			result = clazz.getDeclaredFields();
			FIELD_CACHE.put(clazz, (result.length == 0 ? NO_FIELDS : result));
			getFieldsFromSuper(clazz, clazz.getSuperclass());
		}
		return FIELD_CACHE.get(clazz);
	}

	public static Object invokeGetterMethod(Object target, String name) {
		Assert.notNull(target, "Target object must not be null");
		Assert.hasText(name, "Method name must not be empty");

		String getterMethodName = name;
		if (!name.startsWith(GETTER_PREFIX)) {
			getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
		}
		Method method = findMethod(target.getClass(), getterMethodName);
		if (method == null && !getterMethodName.equals(name)) {
			getterMethodName = name;
			method = findMethod(target.getClass(), getterMethodName);
		}
		if (method == null) {
			throw new IllegalArgumentException(String.format(
					"Could not find getter method '%s' on %s", getterMethodName, safeToString(target)));
		}

		if (log.isDebugEnabled()) {
			log.debug(String.format("Invoking getter method '%s' on %s", getterMethodName, safeToString(target)));
		}
		makeAccessible(method);
		return org.springframework.util.ReflectionUtils.invokeMethod(method, target);
	}

	public static Method[] getDeclaredMethods(Class<?> clazz) {
		Method[] result = declaredMethodsCache.get(clazz);
		if (result == null) {
			Method[] declaredMethods = clazz.getDeclaredMethods();
			List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
			if (defaultMethods != null) {
				result = new Method[declaredMethods.length + defaultMethods.size()];
				System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
				int index = declaredMethods.length;
				for (Method defaultMethod : defaultMethods) {
					result[index] = defaultMethod;
					index++;
				}
			} else {
				result = declaredMethods;
			}
			declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
		}
		return result;
	}

	public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
			for (Method method : methods) {
				if (name.equals(method.getName()) &&
						(paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) ||
				!Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	private static void getFieldsFromSuper(Class<?> originalClazz, Class<?> ancesterClazz) {
		if (ancesterClazz == Object.class) {
			return;
		}
		Field[] fieldsFromSuper = ancesterClazz.getDeclaredFields();
		if (fieldsFromSuper.length > 0) {
			Field[] fields = FIELD_CACHE.get(originalClazz);
			List<Field> fieldList = new ArrayList<>();
			if (fields != null) {
				fieldList.addAll(Arrays.asList(fields));
			}

			for (int j = 0; j < fieldsFromSuper.length; j++) {
				//检查父类的field是不是被子类覆盖了
				boolean overrided = false;
				Field fieldFromSuper = fieldsFromSuper[j];
				if (fields != null) {
					for (int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						if (field.getName().equals(fieldFromSuper.getName())) {
							overrided = true;
							break;
						}
					}
				}
				if (!overrided) {
					fieldList.add(fieldFromSuper);
				}
			}

			fields = fieldList.stream().toArray(Field[]::new);
			FIELD_CACHE.put(originalClazz, fields);
		}

		if (ancesterClazz.getSuperclass() != Object.class) {
			getFieldsFromSuper(originalClazz, ancesterClazz.getSuperclass());
		}
	}

	private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
		List<Method> result = null;
		for (Class<?> ifc : clazz.getInterfaces()) {
			for (Method ifcMethod : ifc.getMethods()) {
				if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
					if (result == null) {
						result = new LinkedList<Method>();
					}
					result.add(ifcMethod);
				}
			}
		}
		return result;
	}

	private static String safeToString(Object target) {
		try {
			return String.format("target object [%s]", target);
		} catch (Exception ex) {
			return String.format("target of type [%s] whose toString() method threw [%s]",
					(target != null ? target.getClass().getName() : "unknown"), ex);
		}
	}

	private static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}

	private static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	private static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}
}
