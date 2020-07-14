package com.loserico.pattern.singleton;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * There are some other ways to break the singleton pattern.
 * 
 * <pre>
 * If the class is Serializable.
 * If it’s Clonable.
 * It can be break by Reflection.
 * And also if, the class is loaded by multiple class loaders.
 * </pre>
 * 
 * The following example shows how you can protect your class from getting
 * instantiated more than once.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public class EagerAntiSerializeCloneClassloaderSingleton implements Serializable {
	private static final long serialVersionUID = -5L;
	private static EagerAntiSerializeCloneClassloaderSingleton sc = new EagerAntiSerializeCloneClassloaderSingleton();

	/*
	 * An "if condition" inside the constructor can prevent the singleton from
	 * getting instantiated more than once using reflection.
	 */
	private EagerAntiSerializeCloneClassloaderSingleton() {
		if (sc != null) {
			throw new IllegalStateException("Already created.");
		}
	}

	public static EagerAntiSerializeCloneClassloaderSingleton getInstance() {
		return sc;
	}

	/*
	 * Implement the readResolve() and writeReplace() methods in your singleton
	 * class and return the same object through them.
	 * 
	 * 为了避免此问题，我们需要提供 readResolve() 方法的实现。readResolve(）代替了从流中读取对象。
	 * 这就确保了在序列化和反序列化的过程中没人可以创建新的实例。
	 * @on
	 */
	private Object readResolve() throws ObjectStreamException {
		return sc;
	}
	private Object writeReplace() throws ObjectStreamException {
		return sc;
	}

	
	/*
	 * You should also implement the clone() method and throw an exception so that
	 * the singleton cannot be cloned.
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Singleton8, cannot be clonned");
	}

	/*
	 * To prevent the singleton getting instantiated from different class loaders,
	 * you can implement the getClass() method. 
	 * 
	 * The above getClass() method associates the classloader with the current thread; 
	 * if that classloader is null, the method uses the same classloader that loaded the singleton class.
	 * @on
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private static Class getClass(String classname) throws ClassNotFoundException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null)
			classLoader = EagerAntiSerializeCloneClassloaderSingleton.class.getClassLoader();
		return (classLoader.loadClass(classname));
	}
}