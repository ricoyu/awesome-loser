package com.loserico.jackson.serializer;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomerSerializerTest {

	@Test
	public void testSerializer() {
		Item myItem = new Item(1, "theItem", new User(2, "theUser"));
		String serialized = null;
		try {
			serialized = new ObjectMapper().writeValueAsString(myItem);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(serialized);
	}

	/*
	 * Now, let’s simplify the JSON output above by only serializing the id of the
	 * User, not the entire User object; we’d like to get the following, simpler JSON:
	 * 
	 * Now, we need to register this custom serializer with the ObjectMapper for the
	 * Item class, and perform the serialization:
	 * 
	 * 或者可以直接在类上标注注解
	 * @JsonSerialize(using = ItemSerializer.class)
	 * 
	 * @on
	 */
	@Test
	public void testCustomerSerializer() {
		Item myItem = new Item(1, "theItem", new User(2, "theUser"));
		ObjectMapper mapper = new ObjectMapper();
		
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Item.class, new ItemSerializer());
		mapper.registerModule(simpleModule);
		
		try {
			String serialized = mapper.writeValueAsString(myItem);
			System.out.println(serialized);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
