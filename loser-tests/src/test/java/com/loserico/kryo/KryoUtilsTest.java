package com.loserico.kryo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.common.lang.utils.KryoUtils;
import com.loserico.jackson.nested.Product;
import com.loserico.jackson.nested.ProductDeserializer;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-01-19 20:49
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class KryoUtilsTest {
	
	@SneakyThrows
	@Test
	public void test() {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Product.class, new ProductDeserializer());
		objectMapper.registerModule(simpleModule);
		
		String json = IOUtils.readClassPathFileAsString("nested-json.json");
		Product product = objectMapper.readValue(json, Product.class);
		
		byte[] bytes = KryoUtils.toBytes(product);
		product = KryoUtils.toObject(bytes);
		System.out.println(product.getName());
	}
}
