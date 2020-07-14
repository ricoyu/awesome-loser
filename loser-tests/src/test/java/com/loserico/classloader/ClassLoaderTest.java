package com.loserico.classloader;

public class ClassLoaderTest {

	public static void main(String[] args) throws ClassNotFoundException {
		MyClassLoader classLoader = new MyClassLoader();
		String name = "com.sishuok.hello.Hello";
		Class<?> clazz = classLoader.loadClass(name);
		System.out.println(clazz.getClassLoader());
		System.out.println(clazz.getClassLoader().getParent());
		System.out.println(clazz.getClassLoader().getParent().getParent());
		System.out.println(clazz.getClassLoader().getParent().getParent().getParent());
		
		Thread.currentThread().setContextClassLoader(classLoader);
		
		Class<?> class0 = Thread.currentThread().getContextClassLoader().loadClass(name);
		System.out.println(class0.getClassLoader());
		System.out.println(class0 == clazz);
		
		Class<?> clazz1 = Class.forName(name);
		System.out.println(clazz1.getClassLoader());
	}
}
