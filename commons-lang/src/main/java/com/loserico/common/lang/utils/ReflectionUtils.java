package com.loserico.common.lang.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.MethodInvoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 反射工具类
 * <p>
 * Copyright: Copyright (c) 2019-10-31 20:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ReflectionUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);
	
	private static final String SETTER_PREFIX = "set";
	
	private static final String GETTER_PREFIX = "get";
	
	private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);
	
	/**
	 * Naming prefix for CGLIB-renamed methods.
	 *
	 * @see #isCglibRenamedMethod
	 */
	private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";
	
	private static final Method[] NO_METHODS = {};
	
	private static final Field[] NO_FIELDS = {};
	
	/**
	 * Cache for {@link Class#getDeclaredMethods()} plus equivalent default methods
	 * from Java 8 based interfaces, allowing for fast iteration.
	 */
	private static final Map<Class<?>, Method[]> declaredMethodsCache =
			new ConcurrentReferenceHashMap<Class<?>, Method[]>(256);
	
	/**
	 * Cache for {@link Class#getDeclaredFields()}, allowing for fast iteration.
	 */
	private static final Map<Class<?>, Field[]> declaredFieldsCache =
			new ConcurrentReferenceHashMap<Class<?>, Field[]>(256);
	
	/**
	 * 这个类声明的或者是其父类中声明的field缓存
	 */
	private static final Map<Class<?>, Field[]> fieldsCache = new ConcurrentReferenceHashMap<Class<?>, Field[]>(256);
	
	/**
	 * 根据fieldName查找Field对象时, 宽松模式会去掉fieldName中的 "-" "_" "空白符"
	 */
	private static final Pattern flaxableNamePattern = Pattern.compile("[-_\\s]");
	
	/**
	 * Action to take on each method.
	 */
	public interface MethodCallback {
		
		/**
		 * Perform an operation using the given method.
		 *
		 * @param method the method to operate on
		 */
		void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
	}
	
	/**
	 * Callback optionally used to filter methods to be operated on by a method callback.
	 */
	public interface MethodFilter {
		
		/**
		 * Determine whether the given method matches.
		 *
		 * @param method the method to check
		 */
		boolean matches(Method method);
	}
	
	/**
	 * Callback interface invoked on each field in the hierarchy.
	 */
	public interface FieldCallback {
		
		/**
		 * Perform an operation using the given field.
		 *
		 * @param field the field to operate on
		 */
		void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
	}
	
	/**
	 * Callback optionally used to filter fields to be operated on by a field
	 * callback.
	 */
	public interface FieldFilter {
		
		/**
		 * Determine whether the given field matches.
		 *
		 * @param field the field to check
		 */
		boolean matches(Field field);
	}
	
	/**
	 * 检查指定对象是否有name属性, 会往上找其父类, 但不包括Object
	 *
	 * @param obj
	 * @param name
	 * @return boolean
	 */
	public static boolean existsField(Object obj, String name) {
		Assert.notNull(obj, "obj must not be null");
		Assert.notNull(name, "name must not be null");
		
		Class<?> searchType = obj.getClass();
		
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if (name.equals(field.getName())) {
					return true;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return false;
	}
	
	/**
	 * 设置字段值
	 * Set the field represented by the supplied {@link Field field object} on the
	 * specified {@link Object target object} to the specified {@code value}. In
	 * accordance with {@link Field#set(Object, Object)} semantics, the new value is
	 * automatically unwrapped if the underlying field has a primitive type.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 *
	 * @param field  the field to set
	 * @param target the target object on which to set the field
	 * @param value  the value to set (may be {@code null})
	 */
	public static void setField(Field field, Object target, Object value) {
		try {
			makeAccessible(field);
			field.set(target, value);
		} catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}
	
	public static void setField(String fieldName, Object target, Object value) {
		try {
			Field field = findField(fieldName, target.getClass());
			if (field == null) {
				return;
			}
			makeAccessible(field);
			field.set(target, value);
		} catch (IllegalAccessException ex) {
			logger.error("", ex);
			logger.error("Set value {} for field {} failed!", value, fieldName);
		}
	}
	
	/**
	 * Set the static {@linkplain Field field} with the given {@code name} on the
	 * provided {@code targetClass} to the supplied {@code value}.
	 * <p>
	 * This method delegates to
	 * {@code null} for the {@code targetObject} and {@code type} arguments.
	 *
	 * @param targetClass the target class on which to set the static field; never
	 *                    {@code null}
	 * @param name        the name of the field to set; never {@code null}
	 * @param value       the value to set
	 * @since 4.2
	 */
	public static void setField(String name, Class<?> targetClass, Object value) {
		Assert.isTrue(targetClass != null, "targetClass for the field must be specified");

		Field field = findField(name, targetClass);
		if (field == null) {
			throw new IllegalArgumentException(String.format(
					"Could not find field '%s' on target class [%s]", name, targetClass));
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format(
					"Setting field '%s' on target class [%s] to value [%s]", name, targetClass, value));
		}
		makeAccessible(field);
		org.springframework.util.ReflectionUtils.setField(field, null, value);
	}
	

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name}. Searches all superclasses up to {@link Object}.
	 *
	 * @param clazz the class to introspect
	 * @param name  the name of the field
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	public static Field findField(String name, Class<?> clazz) {
		return findField(clazz, name, null);
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name} and/or {@link Class type}. Searches all superclasses up
	 * to {@link Object}.
	 *
	 * @param clazz the class to introspect
	 * @param name  the name of the field (may be {@code null} if type is specified)
	 * @param type  the type of the field (may be {@code null} if name is specified)
	 * @return the corresponding Field object, or {@code null} if not found
	 */
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
	
	/**
	 * 去掉name中的中划线, 下划线, 空白符, 然后跟Field大小写不敏感匹配
	 *
	 * @param clazz
	 * @param name
	 * @param type
	 * @return Field
	 */
	public static Field findFieldRelaxable(Class<?> clazz, String name, Class<?> type) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
		name = flaxableNamePattern.matcher(name).replaceAll("");
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if ((name == null || name.equalsIgnoreCase(field.getName())) &&
						(type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	/**
	 * This variant retrieves {@link Class#getDeclaredFields()} from a local cache
	 * in order to avoid the JVM's SecurityManager check and defensive array
	 * copying.
	 *
	 * @param clazz the class to introspect
	 * @return the cached array of fields
	 * @see Class#getDeclaredFields()
	 */
	public static Field[] getDeclaredFields(Class<?> clazz) {
		Field[] result = declaredFieldsCache.get(clazz);
		if (result == null) {
			result = clazz.getDeclaredFields();
			declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
		}
		return result;
	}
	
	/**
	 * 拿所有的field, 包括父类的field, 但是不包括Object类中的字段
	 *
	 * @param clazz
	 * @return Field[]
	 */
	public static Field[] getFields(Class<?> clazz) {
		Field[] result = fieldsCache.get(clazz);
		if (result == null) {
			result = clazz.getDeclaredFields();
			fieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
			getFieldsFromSuper(clazz, clazz.getSuperclass());
		}
		return fieldsCache.get(clazz);
	}
	
	public static Object getFieldValue(String fieldName, Class<?> targetClass) {
		Assert.isTrue(targetClass != null,
				"targetClass for the field must be specified");
		
		Field field = findField(fieldName, targetClass);
		if (field == null) {
			// throw new IllegalArgumentException(String.format("Could not find field '%s'
			// on %s or target class [%s]", name, safeToString(ultimateTarget),
			// targetClass));
			return null;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Getting field '%s' target class [%s]", fieldName, targetClass));
		}
		makeAccessible(field);
		return org.springframework.util.ReflectionUtils.getField(field, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(String fieldName, Object target) {
		Field field = findField(fieldName, target.getClass());
		if (field == null) {
			return null;
		}
		
		try {
			makeAccessible(field);
			return (T) field.get(target);
		} catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}
	
	/**
	 * Get the field represented by the supplied {@link Field field object} on the
	 * specified {@link Object target object}. In accordance with
	 * {@link Field#get(Object)} semantics, the returned value is automatically
	 * wrapped if the underlying field has a primitive type.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 *
	 * @param field  the field to get
	 * @param target the target object from which to get the field
	 * @return the field's current value
	 */
	public static Object getFieldValue(Field field, Object target) {
		try {
			makeAccessible(field);
			return field.get(target);
		} catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}
	
	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name
	 * and parameter types. Searches all superclasses up to {@code Object}.
	 * <p>
	 * Returns {@code null} if no {@link Method} can be found.
	 *
	 * @param clazz      the class to introspect
	 * @param methodName       the name of the method
	 * @param paramTypes the parameter types of the method (may be {@code null} to
	 *                   indicate any signature)
	 * @return the Method object, or {@code null} if none found
	 */
	public static Method findMethod(String methodName, Class<?> clazz, Class<?>... paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
			for (Method method : methods) {
				if (methodName.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	/**
	 * Invoke the specified {@link Method} against the supplied target object with
	 * no arguments. The target object can be {@code null} when invoking a static
	 * {@link Method}.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException}.
	 *
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @return the invocation result, if any
	 * @see #invokeMethod(Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, new Object[0]);
	}
	
	/**
	 * Invoke the specified {@link Method} against the supplied target object with
	 * the supplied arguments. The target object can be {@code null} when invoking a
	 * static {@link Method}.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException}.
	 *
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @param args   the invocation arguments (may be {@code null})
	 * @return the invocation result, if any
	 */
	public static Object invokeMethod(Method method, Object target, Object... args) {
		try {
			return method.invoke(target, args);
		} catch (Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}
	
	/**
	 * Invoke the method with the given {@code name} on the supplied target object
	 * with the supplied arguments.
	 * <p>
	 * This method traverses the class hierarchy in search of the desired method. In
	 * addition, an attempt will be made to make non-{@code public} methods
	 * <em>accessible</em>, thus allowing one to invoke {@code protected},
	 * {@code private}, and <em>package-private</em> methods.
	 *
	 * @param target the target object on which to invoke the specified method
	 * @param methodName   the name of the method to invoke
	 * @param args   the arguments to provide to the method
	 * @return the invocation result, if any
	 * @see MethodInvoker
	 * @see ReflectionUtils#makeAccessible(Method)
	 * @see ReflectionUtils#invokeMethod(Method, Object, Object[])
	 * @see ReflectionUtils#handleReflectionException(Exception)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(String methodName, Object target, Object... args) {
		Assert.notNull(target, "Target object must not be null");
		Assert.hasText(methodName, "Method name must not be empty");
		
		try {
			MethodInvoker methodInvoker = new MethodInvoker();
			methodInvoker.setTargetObject(target);
			methodInvoker.setTargetMethod(methodName);
			methodInvoker.setArguments(args);
			methodInvoker.prepare();
			
			//if (logger.isDebugEnabled()) {
			//	logger.debug(String.format("Invoking method '%s' on %s with arguments %s", name, safeToString(target), nullSafeToString(args)));
			//}
			
			return (T) methodInvoker.invoke();
		} catch (Exception ex) {
			handleReflectionException(ex);
			throw new IllegalStateException("Should never get here");
		}
	}
	
	
	public static <T> T invokeMethod(String methodName, Object target, String arg) {
		try {
			Method method = target.getClass().getMethod(methodName, String.class);
			return (T) method.invoke(target, arg);
		} catch (NoSuchMethodException |
				SecurityException |
				IllegalAccessException |
				IllegalArgumentException |
				InvocationTargetException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T invokeMethod(String methodName, Object target, Runnable arg) {
		try {
			Method method = target.getClass().getMethod(methodName, Runnable.class);
			return (T) method.invoke(target, arg);
		} catch (NoSuchMethodException |
				SecurityException |
				IllegalAccessException |
				IllegalArgumentException |
				InvocationTargetException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 调用给定完全限定类名的静态方法, 找不到给定类或者给定类没有指定方法同样抛RuntimeException
	 *
	 * @param className
	 * @param methodName
	 * @return Object
	 */
	public static Object invokeStatic(String methodName, String className) {
		Class<?> clazz = getClass(className);
		return invokeStatic(methodName, clazz);
	}
	
	/**
	 * 调用给定类的静态方法, 找不到指定方法同样抛RuntimeException
	 * 如果给出了args, args中不能出现值为null的参数
	 *
	 * @param clazz
	 * @param methodName
	 * @return Object
	 */
	public static Object invokeStatic(String methodName, Class clazz, Object... args) {
		Objects.requireNonNull(clazz, "clazz can not be null");
		
		// 确定参数类型
		Class<?>[] parameterTypes = null;
		if (args != null && args.length != 0) {
			parameterTypes = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				Object obj = args[i];
				if (obj == null) {
					log.warn("第{}个参数为null, 无法确定参数类型!", i);
					return null;
				}
				parameterTypes[i] = obj.getClass();
			}
		}
		try {
			Method method = clazz.getMethod(methodName, parameterTypes);
			return method.invoke(null, args);
		} catch (NoSuchMethodException e) {
			String msg = "No such method " + methodName;
			log.error(msg, e);
			throw new RuntimeException(msg, e);
		} catch (IllegalAccessException | InvocationTargetException e) {
			String msg = "Invoke method " + methodName + " failed!";
			log.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
	
	/**
	 * Determine whether the given field is a "public static final" constant.
	 *
	 * @param field the field to check
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}
	
	/**
	 * Determine whether the given method is an "equals" method.
	 *
	 * @see Object#equals(Object)
	 */
	public static boolean isEqualsMethod(Method method) {
		if (method == null || !method.getName().equals("equals")) {
			return false;
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		return (paramTypes.length == 1 && paramTypes[0] == Object.class);
	}
	
	/**
	 * Determine whether the given method is a "hashCode" method.
	 *
	 * @see Object#hashCode()
	 */
	public static boolean isHashCodeMethod(Method method) {
		return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
	}
	
	/**
	 * Determine whether the given method is a "toString" method.
	 *
	 * @see Object#toString()
	 */
	public static boolean isToStringMethod(Method method) {
		return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
	}
	
	/**
	 * Determine whether the given method is originally declared by
	 * {@link Object}.
	 */
	public static boolean isObjectMethod(Method method) {
		if (method == null) {
			return false;
		}
		try {
			Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean isCglibRenamedMethod(Method renamedMethod) {
		String name = renamedMethod.getName();
		if (name.startsWith(CGLIB_RENAMED_METHOD_PREFIX)) {
			int i = name.length() - 1;
			while (i >= 0 && Character.isDigit(name.charAt(i))) {
				i--;
			}
			return ((i > CGLIB_RENAMED_METHOD_PREFIX.length()) &&
					(i < name.length() - 1) && name.charAt(i) == '$');
		}
		return false;
	}
	
	/**
	 * 检查targetClasses是否标注了annotationClass注解
	 *
	 * @param annotationClass
	 * @param targetClass
	 * @return boolean
	 */
	public static boolean existsAnnotation(Class<? extends Annotation> annotationClass, Class targetClass) {
		if (targetClass == null || annotationClass == null) {
			return false;
		}
		
		return targetClass.getAnnotation(annotationClass) != null;
	}
	
	/**
	 * 检查targetClasses的任意public方法(包含父类中的)是否标注了annotationClass注解
	 *
	 * @param annotationClass
	 * @param method
	 * @return
	 */
	public static boolean existsAnnotation(Class<? extends Annotation> annotationClass, Method method) {
		if (annotationClass == null || method == null) {
			return false;
		}
		Annotation annotation = method.getAnnotation(annotationClass);
		return annotation != null;
	}
	
	/**
	 * 在targetClass的public方法上找所有的由annotationClasses指定的注解instance
	 *
	 * @param targetClass
	 * @param annotationClasses
	 * @return List<Annotation>
	 */
	public static List<Annotation> getMethodAnnotations(Class targetClass, Class<? extends Annotation>... annotationClasses) {
		if (targetClass == null || annotationClasses == null || annotationClasses.length == 0) {
			return Collections.emptyList();
		}
		
		List<Annotation> annotations = new ArrayList<>();
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			for (Class annorationClass : annotationClasses) {
				Annotation annotation = method.getAnnotation(annorationClass);
				if (annotation != null) {
					annotations.add(annotation);
				}
			}
		}
		
		return annotations;
	}
	
	/**
	 * 在targetClass的公共方法找找所有标注了指定注解的方法
	 *
	 * @param targetClass
	 * @param annotationClasses
	 * @return Set<Method>
	 */
	public static Set<Method> filterMethodByAnnotation(Class targetClass, Class<? extends Annotation>... annotationClasses) {
		if (targetClass == null || annotationClasses == null || annotationClasses.length == 0) {
			return Collections.emptySet();
		}
		
		Set<Method> methodSet = new HashSet<>();
		Method[] methods = targetClass.getMethods();
		
		for (Method method : methods) {
			for (Class annorationClass : annotationClasses) {
				Annotation annotation = method.getAnnotation(annorationClass);
				if (annotation != null) {
					methodSet.add(method);
				}
			}
		}
		
		return methodSet;
	}
	
	/**
	 * 在targetClass中找所有标注了指定注解的字段, 包括自己声明的和父类中的字段, 但不包括Object类中的
	 *
	 * @param targetClass
	 * @param annotationClasses
	 * @return Set<Method>
	 */
	public static Set<Field> filterFieldByAnnotation(Class targetClass, Class<? extends Annotation>... annotationClasses) {
		if (targetClass == null || annotationClasses == null || annotationClasses.length == 0) {
			return Collections.emptySet();
		}
		
		Set<Field> fieldSet = new HashSet<>();
		Field[] fields = getFields(targetClass);
		
		for (Field field : fields) {
			for (Class annorationClass : annotationClasses) {
				Annotation annotation = field.getAnnotation(annorationClass);
				if (annotation != null) {
					fieldSet.add(field);
				}
			}
		}
		
		return fieldSet;
	}
	
	/**
	 * Make the given field accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called when
	 * actually necessary, to avoid unnecessary conflicts with a JVM SecurityManager
	 * (if active).
	 *
	 * @param field the field to make accessible
	 * @see Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
				Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}
	
	/**
	 * Make the given method accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called when
	 * actually necessary, to avoid unnecessary conflicts with a JVM SecurityManager
	 * (if active).
	 *
	 * @param method the method to make accessible
	 * @see Method#setAccessible
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) ||
				!Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
			method.setAccessible(true);
		}
	}
	
	/**
	 * Make the given constructor accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called when
	 * actually necessary, to avoid unnecessary conflicts with a JVM SecurityManager
	 * (if active).
	 *
	 * @param ctor the constructor to make accessible
	 * @see Constructor#setAccessible
	 */
	public static void makeAccessible(Constructor<?> ctor) {
		if ((!Modifier.isPublic(ctor.getModifiers()) ||
				!Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
			ctor.setAccessible(true);
		}
	}
	
	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class, as locally declared or equivalent thereof (such as default methods on
	 * Java 8 based interfaces that the given class implements).
	 *
	 * @param clazz the class to introspect
	 * @param mc    the callback to invoke for each method
	 * @see #doWithMethods
	 * @since 4.2
	 */
	public static void doWithLocalMethods(Class<?> clazz, MethodCallback mc) {
		Method[] methods = getDeclaredMethods(clazz);
		for (Method method : methods) {
			try {
				mc.doWith(method);
			} catch (IllegalAccessException ex) {
				throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
			}
		}
	}
	
	/**
	 * 这个方法会在clazz中找到所有的方法, 包括父类中的方法, 但不包括Object类中的方法, 
	 * 但是如果clazz是Spring启动CGlib动态代理的时候生成的代理类, 则不会再去找代理类的父类中的方法了 
	 * 一次是代理类的, 一次是这个代理类的父类
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses.
	 * <p>
	 * The same named method occurring on subclass and superclass will appear twice,
	 * unless excluded by a {@link MethodFilter}.
	 *
	 * @param clazz the class to introspect
	 * @param mc    the callback to invoke for each method
	 * @see #doWithMethods(Class, MethodCallback, MethodFilter)
	 */
	public static void doWithMethods(Class<?> clazz, MethodCallback mc) {
		doWithMethods(clazz, mc, null);
	}
	
	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses (or given interface and super-interfaces).
	 * 当在代理类和代理类的父类中找到同名方法时, 只调用代理类中的方法
	 * <p>
	 * The same named method occurring on subclass and superclass will appear twice,
	 * unless excluded by the specified {@link MethodFilter}.
	 *
	 * @param clazz the class to introspect
	 * @param mc    the callback to invoke for each method
	 * @param mf    the filter that determines the methods to apply the callback to
	 */
	public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf) {
		// Keep backing up the inheritance hierarchy.
		Method[] methods = getDeclaredMethods(clazz);
		//methods = filterProxyMethods(clazz, methods);
		for (Method method : methods) {
			if (mf != null && !mf.matches(method)) {
				continue;
			}
			try {
				mc.doWith(method);
			} catch (IllegalAccessException ex) {
				throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
			}
		}
		
		// 获取父类, 但如果clazz是代理类的话就不找其父类了
		if (!clazz.getName().contains("$$") && clazz.getSuperclass() != null) {
			doWithMethods(clazz.getSuperclass(), mc, mf);
		} else if (clazz.isInterface()) {
			for (Class<?> superIfc : clazz.getInterfaces()) {
				doWithMethods(superIfc, mc, mf);
			}
		}
	}
	
	public static Method[] filterProxyMethods(Class<?> clazz, Method[]  allMethods) {
		// 获取原始类, 如果clazz是代理类的话
		Class<?> originalClass = (clazz.getName().contains("$$")) ? clazz.getSuperclass() : clazz;
		
		Map<String, Method> uniqueMethods = new HashMap<>();
		
		for (Method method : allMethods) {
			String methodName = method.getName();
			Class<?> declaringClass = method.getDeclaringClass();
			
			// 如果方法声明的类不是原始类，则优先保留
			if (!declaringClass.equals(originalClass)) {
				uniqueMethods.put(methodName, method);
			} else {
				// 如果方法声明的类是原始类，且在代理类中不存在同名方法，则保留
				uniqueMethods.putIfAbsent(methodName, method);
			}
		}
		
		return uniqueMethods.values().toArray(new Method[0]);
	}
	
	/**
	 * Get all declared methods on the leaf class and all superclasses. Leaf class
	 * methods are included first.
	 *
	 * @param leafClass the class to introspect
	 */
	public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
		final List<Method> methods = new ArrayList<Method>(32);
		doWithMethods(leafClass, new MethodCallback() {
			@Override
			public void doWith(Method method) {
				methods.add(method);
			}
		});
		return methods.toArray(new Method[methods.size()]);
	}
	
	/**
	 * Get the unique set of declared methods on the leaf class and all
	 * superclasses. Leaf class methods are included first and while traversing the
	 * superclass hierarchy any methods found with signatures matching a method
	 * already included are filtered out.
	 *
	 * @param leafClass the class to introspect
	 */
	public static Method[] getUniqueDeclaredMethods(Class<?> leafClass) {
		final List<Method> methods = new ArrayList<Method>(32);
		doWithMethods(leafClass, new MethodCallback() {
			@Override
			public void doWith(Method method) {
				boolean knownSignature = false;
				Method methodBeingOverriddenWithCovariantReturnType = null;
				for (Method existingMethod : methods) {
					if (method.getName().equals(existingMethod.getName()) &&
							Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())) {
						// Is this a covariant return type situation?
						if (existingMethod.getReturnType() != method.getReturnType() &&
								existingMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
							methodBeingOverriddenWithCovariantReturnType = existingMethod;
						} else {
							knownSignature = true;
						}
						break;
					}
				}
				if (methodBeingOverriddenWithCovariantReturnType != null) {
					methods.remove(methodBeingOverriddenWithCovariantReturnType);
				}
				if (!knownSignature && !isCglibRenamedMethod(method)) {
					methods.add(method);
				}
			}
		});
		return methods.toArray(new Method[methods.size()]);
	}
	
	/**
	 * 拿到所有在这个类中声明的方法, 包括private方法, 但不包括父类中的方法
	 * 如果这个类实现了接口, 接口中的default方法也会被包括进来
	 * This variant retrieves {@link Class#getDeclaredMethods()} from a local cache
	 * in order to avoid the JVM's SecurityManager check and defensive array
	 * copying. In addition, it also includes Java 8 default methods from locally
	 * implemented interfaces, since those are effectively to be treated just like
	 * declared methods.
	 *
	 * @param clazz the class to introspect
	 * @return the cached array of methods
	 * @see Class#getDeclaredMethods()
	 */
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
	
	/**
	 * Handle the given reflection exception. Should only be called if no checked
	 * exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message or
	 * UndeclaredThrowableException otherwise.
	 *
	 * @param ex the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
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
	
	/**
	 * Handle the given invocation target exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of such a root cause.
	 * Throws an UndeclaredThrowableException otherwise.
	 *
	 * @param ex the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}
	
	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}. Should
	 * only be called if no checked exception is expected to be thrown by the target
	 * method.
	 * <p>
	 * Rethrows the underlying exception cast to a {@link RuntimeException} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link UndeclaredThrowableException}.
	 *
	 * @param ex the exception to rethrow
	 * @throws RuntimeException the rethrown exception
	 */
	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}
	
	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}. Should
	 * only be called if no checked exception is expected to be thrown by the target
	 * method.
	 * <p>
	 * Rethrows the underlying exception cast to an {@link Exception} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link UndeclaredThrowableException}.
	 *
	 * @param ex the exception to rethrow
	 * @throws Exception the rethrown exception (in case of a checked exception)
	 */
	public static void rethrowException(Throwable ex) throws Exception {
		if (ex instanceof Exception) {
			throw (Exception) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}
	
	/**
	 * Determine whether the given method explicitly declares the given exception or
	 * one of its superclasses, which means that an exception of that type can be
	 * propagated as-is within a reflective invocation.
	 *
	 * @param method        the declaring method
	 * @param exceptionType the exception to throw
	 * @return {@code true} if the exception can be thrown as-is; {@code false} if
	 * it needs to be wrapped
	 */
	public static boolean declaresException(Method method, Class<?> exceptionType) {
		Assert.notNull(method, "Method must not be null");
		Class<?>[] declaredExceptions = method.getExceptionTypes();
		for (Class<?> declaredException : declaredExceptions) {
			if (declaredException.isAssignableFrom(exceptionType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据完全限定类名获取Class对象, 找不到抛RuntimeException
	 *
	 * @param className
	 * @return Class<?>
	 */
	public static Class<?> getClass(String className) {
		if (className == null || "".equals(className.trim())) {
			return null;
		}
		
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			String msg = "No class found for " + className + ", please add jar to classpath";
			log.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
	
	/**
	 * 判断某个类是否存在
	 *
	 * @param className
	 * @return
	 */
	public static boolean existsClass(String className) {
		if (className == null || "".equals(className.trim())) {
			return false;
		}
		
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			String msg = "No class found for " + className + ", please add jar to classpath";
			return false;
		}
	}
	
	/**
	 * Pre-built FieldFilter that matches all non-static, non-final fields.
	 */
	public static final FieldFilter COPYABLE_FIELDS = new FieldFilter() {
		
		@Override
		public boolean matches(Field field) {
			return !(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()));
		}
	};
	
	/**
	 * Pre-built MethodFilter that matches all non-bridge methods.
	 */
	public static final MethodFilter NON_BRIDGED_METHODS = new MethodFilter() {
		
		@Override
		public boolean matches(Method method) {
			return !method.isBridge();
		}
	};
	
	/**
	 * Pre-built MethodFilter that matches all non-bridge methods which are not
	 * declared on {@code java.lang.Object}.
	 */
	public static final MethodFilter USER_DECLARED_METHODS = new MethodFilter() {
		
		@Override
		public boolean matches(Method method) {
			return (!method.isBridge() && method.getDeclaringClass() != Object.class);
		}
	};
	
	private static String safeToString(Object target) {
		try {
			return String.format("target object [%s]", target);
		} catch (Exception ex) {
			return String.format("target of type [%s] whose toString() method threw [%s]",
					(target != null ? target.getClass().getName() : "unknown"), ex);
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
	
	private static void getFieldsFromSuper(Class<?> originalClazz, Class<?> ancesterClazz) {
		if (ancesterClazz == Object.class) {
			return;
		}
		Field[] fieldsFromSuper = ancesterClazz.getDeclaredFields();
		if (fieldsFromSuper.length > 0) {
			Field[] fields = fieldsCache.get(originalClazz);
			List<Field> fieldList = new ArrayList<>();
			if (fields != null) {
				fieldList.addAll(Arrays.asList(fields));
			}
			
			for (int j = 0; j < fieldsFromSuper.length; j++) {
				boolean overrided = false; //检查父类的field是不是被子类覆盖了
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
			fieldsCache.put(originalClazz, fields);
		}
		
		if (ancesterClazz.getSuperclass() != Object.class) {
			getFieldsFromSuper(originalClazz, ancesterClazz.getSuperclass());
		}
	}
	
	/**
	 * 判断一个对象是否为POJO<p/>
	 * 以下这些认为false:<ol>
	 * <li/>null
	 * <li/>String
	 * <li/>Integer
	 * <li/>Long
	 * <li/>Boolean
	 * <li/>Float
	 * <li/>Double
	 * <li/>Character
	 * <li/>Byte
	 * <li/>BigDecimal
	 * <li/>Map
	 * <li/>Collection
	 * </ol>
	 *
	 * @return
	 */
	public static boolean isPojo(Object value) {
		if (value == null) {
			return false;
		}
		
		if (value instanceof String) {
			return false;
		}
		
		if (value instanceof Collection) {
			return false;
		}
		
		if (value instanceof Map) {
			return false;
		}
		
		if (value instanceof Integer) {
			return false;
		}
		
		if (value instanceof Long) {
			return false;
		}
		
		if (value instanceof Boolean) {
			return false;
		}
		
		if (value instanceof Float) {
			return false;
		}
		
		if (value instanceof Double) {
			return false;
		}
		
		if (value instanceof Character) {
			return false;
		}
		
		if (value instanceof Byte) {
			return false;
		}
		
		if (value instanceof BigDecimal) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 判断一个对象是否为POJO<p/>
	 * 以下这些认为false:<ol>
	 * <li/>null
	 * <li/>String
	 * <li/>Integer
	 * <li/>Long
	 * <li/>Boolean
	 * <li/>Float
	 * <li/>Double
	 * <li/>Character
	 * <li/>Byte
	 * <li/>BigDecimal
	 * <li/>Map
	 * <li/>Collection
	 * </ol>
	 *
	 * @return
	 */
	public static boolean isPojo(Class clazz) {
		if (clazz == null) {
			return false;
		}
		
		if (clazz == String.class) {
			return false;
		}
		
		if (Collection.class.isAssignableFrom(clazz)) {
			return false;
		}
		
		if (Map.class.isAssignableFrom(clazz)) {
			return false;
		}
		
		if (clazz == Integer.class) {
			return false;
		}
		
		if (clazz == Integer.TYPE) {
			return false;
		}
		
		if (clazz == Long.class) {
			return false;
		}
		
		if (clazz == Long.TYPE) {
			return false;
		}
		
		if (clazz == Boolean.class) {
			return false;
		}
		
		if (clazz == Boolean.TYPE) {
			return false;
		}
		
		if (clazz == Float.class) {
			return false;
		}
		
		if (clazz == Float.TYPE) {
			return false;
		}
		
		if (clazz == Double.class) {
			return false;
		}
		
		if (clazz == Double.TYPE) {
			return false;
		}
		
		if (clazz == Character.class) {
			return false;
		}
		
		if (clazz == Character.TYPE) {
			return false;
		}
		
		if (clazz == Byte.class) {
			return false;
		}
		
		if (clazz == Byte.TYPE) {
			return false;
		}
		
		if (clazz == BigDecimal.class) {
			return false;
		}
		
		return true;
	}

	/**
	 * 将下划线风格的数据库字段名转换为驼峰式的Java属性名。
	 *
	 * @param underscoreName 下划线风格的名称
	 * @return 驼峰式的名称
	 */
	public static String toPropertyName(String underscoreName) {
		if (underscoreName == null || underscoreName.isEmpty()) {
			return "";
		}

		StringBuilder camelCaseName = new StringBuilder();
		boolean nextCharUpperCase = false;

		for (int i = 0; i < underscoreName.length(); i++) {
			char c = underscoreName.charAt(i);

			if (c == '_') {
				if (camelCaseName.length() > 0) {
					nextCharUpperCase = true;
				}
			} else {
				if (nextCharUpperCase) {
					camelCaseName.append(Character.toUpperCase(c));
					nextCharUpperCase = false;
				} else {
					if (camelCaseName.length() == 0) {
						camelCaseName.append(Character.toLowerCase(c));
					} else {
						camelCaseName.append(c);
					}
				}
			}
		}

		return camelCaseName.toString();
	}

}
