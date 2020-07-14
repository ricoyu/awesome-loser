package com.loserico.classloader;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2019/12/4 18:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BootstrapClassLoaderTest {
	
	@Test
	public void testBootstrapClassLoaderName() {
		System.out.println(String.class.getClassLoader());
	}
}
