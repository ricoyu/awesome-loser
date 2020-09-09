package com.loserico.jackson.nested;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * Copyright: (C), 2020-09-08 10:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NestJsonCustomDeserializerTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Product.class, new ProductDeserializer());
		objectMapper.registerModule(simpleModule);
		
		String json = IOUtils.readClassPathFileAsString("nested-json.json");
		Product product = objectMapper.readValue(json, Product.class);
		assertEquals(product.getName(), "The Best Product");
		assertEquals(product.getBrandName(), "ACME Products");
		assertEquals(product.getOwnerName(), "Ultimate Corp, Inc.");
	}
}
