package com.loserico.jvm.clazz.classloader;

import java.io.IOException;
import java.io.InputStream;

public class ClassLoaderTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ClassLoader myLoader = new ClassLoader() {

			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				try {
					String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";

					InputStream in = getClass().getResourceAsStream(fileName);
					if (in == null) {
						return super.loadClass(name);
					}
					byte[] bytes = new byte[in.available()];
					in.read(bytes);
					return defineClass(name, bytes, 0, bytes.length);
				} catch (IOException e) {
					throw new ClassNotFoundException(name);
				}
			}

		};
		
		Class<?> myLoaderClass = myLoader.loadClass("com.loserico.jvm.clazz.classloader.ClassLoaderTest");
		Object obj = myLoaderClass.newInstance();
		System.out.println(obj.getClass());
		System.out.println(obj instanceof ClassLoaderTest);
	}
}
