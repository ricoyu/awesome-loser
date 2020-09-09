package com.loserico.jackson.nested;

import com.fasterxml.jackson.databind.JsonNode;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * Copyright: (C), 2020-09-08 10:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NestedJsonTest {
	
	private static String json;
	
	@BeforeClass
	public static void init() {
		json = IOUtils.readClassPathFileAsString("nested-json.json");
	}
	
	/**
	 * We can instruct Jackson to unpack the nested property by using a combination of @JsonProperty and some custom logic that we add to our Product class:
	 */
	@Test
	public void testWhenUsingAnnotations_thenOk() {
		Product product = JacksonUtils.toObject(json, Product.class);
		assertEquals(product.getName(), "The Best Product");
	}
	
	@SneakyThrows
	@Test
	public void testWhenUsingJsonNode_thenOk() {
		JsonNode jsonNode = JacksonUtils.objectMapper().readTree(json);
		
		Product product = new Product();
		product.setId(jsonNode.get("id").textValue());
		product.setName(jsonNode.get("name").textValue());
		product.setBrandName(jsonNode.get("brand").get("name").textValue());
		product.setOwnerName(jsonNode.get("brand").get("owner").get("name").textValue());
		
		assertEquals(product.getName(), "The Best Product");
		assertEquals(product.getBrandName(), "ACME Products");
		assertEquals(product.getOwnerName(), "Ultimate Corp, Inc.");
	}
}
