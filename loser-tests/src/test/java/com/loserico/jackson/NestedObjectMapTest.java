package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NestedObjectMapTest {

	@Test
	public void usingAnnotationsTest() throws IOException {
	    Product product = new ObjectMapper()
	      .readerFor(Product.class)
	      .readValue(IOUtils.readClassPathFileAsString("nested-object.json"));
	 
	    assertEquals(product.getName(), "The Best Product");
	    assertEquals(product.getBrandName(), "ACME Products");
	    assertEquals(product.getOwnerName(), "Ultimate Corp, Inc.");
	}
	
	@Test
	public void usingJsonNodeTest() throws IOException {
	    JsonNode productNode = new ObjectMapper().readTree(IOUtils.readClassPathFileAsString("nested-object.json"));
	 
	    Product product = new Product();
	    product.setId(productNode.get("id").textValue());
	    product.setName(productNode.get("name").textValue());
	    product.setBrandName(productNode.get("brand").get("name").textValue());
	    product.setOwnerName(productNode.get("brand").get("owner").get("name").textValue());
	 
	    assertEquals(product.getName(), "The Best Product");
	    assertEquals(product.getBrandName(), "ACME Products");
	    assertEquals(product.getOwnerName(), "Ultimate Corp, Inc.");
	}
	
	@Test
	public void usingDeserializerManuallyRegisteredTest()
	 throws IOException {
	  
	    ObjectMapper mapper = new ObjectMapper();
	    SimpleModule module = new SimpleModule();
	    module.addDeserializer(Product.class, new ProductDeserializer());
	    mapper.registerModule(module);
	 
	    Product product = mapper.readValue(IOUtils.readClassPathFileAsString("nested-object.json"), Product.class);
	  
	    assertEquals(product.getName(), "The Best Product");
	    assertEquals(product.getBrandName(), "ACME Products");
	    assertEquals(product.getOwnerName(), "Ultimate Corp, Inc.");
	}
	
	/**
	 * 需要在Product类上标注
	 * @JsonDeserialize(using = ProductDeserializer.class)
	 * @throws IOException
	 */
	@Test
	public void usingDeserializerAutoRegisteredTest()
	  throws IOException {
	  
	    ObjectMapper mapper = new ObjectMapper();
	    Product product = mapper.readValue(IOUtils.readClassPathFileAsString("nested-object.json"), Product.class);
	 
	    assertEquals(product.getName(), "The Best Product");
	    assertEquals(product.getBrandName(), "ACME Products");
	    assertEquals(product.getOwnerName(), "Ultimate Corp, Inc.");
	}
	
	public static class Product {

		private String id;
		private String name;
		private String brandName;
		private String ownerName;

		@JsonProperty("brand")
		private void unpackNested(Map<String, Object> brand) {
			this.brandName = (String) brand.get("name");
			Map<String, String> owner = (Map<String, String>) brand.get("owner");
			this.ownerName = owner.get("name");
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBrandName() {
			return brandName;
		}

		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}

	}
	
	public static class ProductDeserializer extends StdDeserializer<Product> {
		 
	    public ProductDeserializer() {
	        this(null);
	    }
	 
	    public ProductDeserializer(Class<?> vc) {
	        super(vc);
	    }
	 
	    @Override
	    public Product deserialize(JsonParser jp, DeserializationContext ctxt) 
	      throws IOException, JsonProcessingException {
	  
	        JsonNode productNode = jp.getCodec().readTree(jp);
	        Product product = new Product();
	        product.setId(productNode.get("id").textValue());
	        product.setName(productNode.get("name").textValue());
	        product.setBrandName(productNode.get("brand")
	          .get("name").textValue());
	        product.setOwnerName(productNode.get("brand").get("owner")
	          .get("name").textValue());        
	        return product;
	    }
	}
}
