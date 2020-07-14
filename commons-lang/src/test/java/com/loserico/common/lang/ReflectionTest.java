package com.loserico.common.lang;

import com.loserico.common.lang.utils.ReflectionUtils;
import org.junit.Test;
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
	
	@Test
	public void test() {
		boolean exists = ReflectionUtils.existsAnnotation(Bean.class, MyClass.class);
		System.out.println(exists);
	}
	
	static class MyClass {
		
		@Bean
		public void sayHello() {
			System.out.println("...");
		}
	}
}
