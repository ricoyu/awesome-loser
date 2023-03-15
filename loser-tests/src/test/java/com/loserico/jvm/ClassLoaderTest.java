package com.loserico.jvm;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2023-02-22 8:27
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ClassLoaderTest {
	
	@Test
	public void testBootstrapClassLoader() {
		assertNull(String.class.getClassLoader());
	}
}
