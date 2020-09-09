package com.loserico.jackson.nested;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * <p>
 * Copyright: (C), 2020-09-08 10:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProductDeserializer extends StdDeserializer<Product> {
	
	public ProductDeserializer() {
		this(null);
	}
	
	public ProductDeserializer(Class<?> vc) {
		super(vc);
	}
	
	@Override
	public Product deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode productNode = jp.getCodec().readTree(jp);
		Product product = new Product();
		product.setId(productNode.get("id").textValue());
		product.setName(productNode.get("name").textValue());
		product.setBrandName(productNode.get("brand").get("name").textValue());
		product.setOwnerName(productNode.get("brand").get("owner").get("name").textValue());
		return product;
	}
	
}
