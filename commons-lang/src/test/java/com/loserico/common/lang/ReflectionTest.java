package com.loserico.common.lang;

import org.springframework.context.annotation.Bean;

/**
 * <p>
 * Copyright: (C), 2020/4/21 12:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ReflectionTest {
	
	static class MyClass {
		
		@Bean
		public void sayHello() {
			System.out.println("...");
		}
	}
	
}
