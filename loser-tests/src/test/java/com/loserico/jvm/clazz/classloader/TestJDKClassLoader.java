package com.loserico.jvm.clazz.classloader;

public class TestJDKClassLoader {

	public static void main(String[] args) {
		System.out.println(String.class.getClassLoader()); //启动类加载器是C++语言实现，所以打印不出来
		System.out.println(com.sun.crypto.provider.DESKeyFactory.class.getClassLoader());
		System.out.println(TestJDKClassLoader.class.getClassLoader());
		System.out.println(ClassLoader.getSystemClassLoader().getClass().getName());
	}
}
