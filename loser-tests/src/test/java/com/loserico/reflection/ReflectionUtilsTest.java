package com.loserico.reflection;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.common.lang.vo.Result;
import com.loserico.common.lang.vo.Results;
import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Copyright: (C), 2020-11-18 9:51
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ReflectionUtilsTest {
	
	@Test
	public void testGetSpringMvcControllerAnnotation() {
		boolean exists = ReflectionUtils.existsAnnotation(RestController.class, MyController.class);
		if (exists) {
			Set<String> uris = new HashSet<>();
			Set<Method> methods = ReflectionUtils.filterMethodByAnnotation(MyController.class, SpeedUpDev.class);
			for (Method method : methods) {
				String[] values = null;
				GetMapping getMapping = method.getAnnotation(GetMapping.class);
				if (getMapping != null) {
					values = getMapping.value();
					for (int i = 0; i < values.length; i++) {
						String uri = values[i];
						if (uri!= null) {
						uris.add(uri);
						}
					}
					
				} else {
					PostMapping postMapping = method.getAnnotation(PostMapping.class);
					if (postMapping != null) {
						values = postMapping.value();
						for (int i = 0; i < values.length; i++) {
							String uri = values[i];
							if (uri!= null) {
								uris.add(uri);
							}
						}
					}
				}
			}
			
			uris.forEach(System.out::println);
		}
	}
	
	@Inherited
	@Documented
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface SpeedUpDev {
		
	}
	
	@RestController
	static class MyController {
		
		@SpeedUpDev
		@GetMapping("/hi")
		public Result hi() {
			return Results.success().build();
		}
		
		@SpeedUpDev
		@PostMapping("/create")
		public Result create() {
			return Results.success().build();
		}
	}
}
