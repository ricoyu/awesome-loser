package com.loserico;

/**
 * <p>
 * Copyright: (C), 2019/12/4 19:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ClassLoaderEcho {
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.ext.dirs"));
		System.out.println(ClassLoaderEcho.class.getClassLoader());
	}
}
