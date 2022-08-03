package com.loserico.jvm.clazz.classloader;

import java.io.FileInputStream;

/**
 * 图灵学院自定义ClassLoader测试
 * <p>
 * Copyright: Copyright (c) 2019-09-07 11:02
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class TunlingClassLoaderTest {

	static class MyClassLoader extends ClassLoader {

		private String classPath;

		public MyClassLoader(String classPath) {
			this.classPath = classPath;
		}

		@Override
		public Class<?> findClass(String name) throws ClassNotFoundException {
			try {
				byte[] data = loadByte(name);
				// defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组
				return defineClass(name, data, 0, data.length);
			} catch (Exception e) {
				throw new ClassNotFoundException(name);
			}
		}

		private byte[] loadByte(String name) throws Exception {
			name = name.replaceAll("\\.", "/");
			FileInputStream fis = new FileInputStream(classPath + "/" + name + ".class");
			int length = fis.available();
			byte[] data = new byte[length];
			fis.read(data);
			fis.close();
			return data;
		}

	}

	static class MyClassLoader2 extends ClassLoader {

		private String classPath;

		public MyClassLoader2(String classPath) {
			this.classPath = classPath;
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			synchronized (getClassLoadingLock(name)) {
				// First, check if the class has already been loaded
				Class<?> c = findLoadedClass(name);
				if (c == null) {
					long t0 = System.nanoTime();

					// If still not found, then invoke findClass in order
					// to find the class.
					long t1 = System.nanoTime();
					c = findClass(name);

					// this is the defining class loader; record the stats
					//sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
					//sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
					//sun.misc.PerfCounter.getFindClasses().increment();
				}
				if (resolve) {
					resolveClass(c);
				}
				return c;
			}
		}

		@Override
		public Class<?> findClass(String name) throws ClassNotFoundException {
			try {
				byte[] data = loadByte(name);
				// defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组
				return defineClass(name, data, 0, data.length);
			} catch (Exception e) {
				throw new ClassNotFoundException(name);
			}
		}

		private byte[] loadByte(String name) throws Exception {
			name = name.replaceAll("\\.", "/");
			FileInputStream fis = new FileInputStream(classPath + "/" + name + ".class");
			int length = fis.available();
			byte[] data = new byte[length];
			fis.read(data);
			fis.close();
			return data;
		}

	}

	public static void main(String[] args) throws Exception {
		MyClassLoader2 myClassLoader = new MyClassLoader2("D:\\Learning\\Test");
		Class<?> clazz = myClassLoader.loadClass("java.lang.String");
		Object obj = clazz.newInstance();
//		Method method = clazz.getDeclaredMethod("sout", null);
//		method.invoke(obj, null);
		System.out.println(clazz.getClassLoader());
	}
}
