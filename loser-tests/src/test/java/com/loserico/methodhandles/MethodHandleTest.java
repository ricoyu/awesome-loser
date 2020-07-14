package com.loserico.methodhandles;

import org.junit.Test;

import java.lang.invoke.MethodHandles;

/**
 * http://www.baeldung.com/java-method-handles?utm_source=drip&utm_medium=email&utm_campaign=Latest+article+about+Java+%E2%80%93+on+Baeldung
 * A method handle is a typed, directly executable reference to an underlying method, constructor, 
 * field, or similar low-level operation, with optional transformations of arguments or return values. 
 * 
 * These transformations are quite general, and include such patterns as conversion, insertion, deletion, and substitution. 
 * <p>
 * Copyright: Copyright (c) 2018-03-04 11:36
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class MethodHandleTest {

	@Test
	public void test() {
		/*
		 * publicLookup()用来访问public方法
		 */
		MethodHandles.Lookup lookup = MethodHandles.publicLookup();
		/*
		 * 如果要通过MehtodHandles访问private, protected方法, 使用lookup()
		 */
		MethodHandles.Lookup lookup1 = MethodHandles.lookup();
	}
}
