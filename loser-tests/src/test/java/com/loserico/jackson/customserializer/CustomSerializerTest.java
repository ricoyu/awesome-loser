package com.loserico.jackson.customserializer;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2020-09-17 10:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CustomSerializerTest {
	
	@SneakyThrows
	@Test
	public void testCustomSerializer() {
		SimpleModule simpleModule = new SimpleModule("CustomCarSerializer", 
				new Version(1, 0, 0, null, null, null));
		simpleModule.addSerializer(Car.class, new CustomCarSerializer());
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(simpleModule);
		
		Car car = new Car("Yellow", "BMW");
		String str = mapper.writeValueAsString(car);
		assertThat(str.contains("car_brand"));
		System.out.println(str);
	}
}
