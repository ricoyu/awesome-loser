package com.loserico.jvm.oom;

/**
 * VM Args: -Xms5m -Xmx5m -XX:+PrintGCDetails
 * <p>
 * Copyright: Copyright (c) 2019-08-06 11:17
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class OldGenerationOOM {

	public static void main(String[] args) {
		// byte[] data = new byte[2 * 1024 * 1024];
		{
			byte[] data = new byte[2 * 1024 * 1024];

			/*
			 * 显式置为null的话data就会被回收了
			 */
			// data = null;
		}

		/*
		 * 如果不定义变量a, 不管上面的data放不放到代码块里面都不会被回收
		 * 因为局部变量表里面的slot还引用着data
		 * 
		 * 而当定义了a过后, 这个slot被复用, 所以data没有被引用到了, 所以被回收了
		 */
		// int a = 0;

		System.gc();
	}
}
