package com.loserico.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.loserico.jackson.deserializer.ItemDeserializer;
import org.junit.Test;

import java.io.IOException;

public class DeserializerTest {

	public String json = "{\"id\": 1,\"itemName\": \"theItem\",\"owner\": {\"id\": 2,\"name\": \"theUser\"}}";

	public static void main(String[] args) throws JsonProcessingException, JsonMappingException, IOException {
		String json = args[0];
		DeserializerTest test = new DeserializerTest();
		Item item = new ObjectMapper().readValue(json, Item.class);
		System.out.println(item);
	}

	@Test
	public void testDefaultDeserializer() throws JsonParseException, JsonMappingException, IOException {
		Item item = new ObjectMapper().readValue(json, Item.class);
		System.out.println(item);
	}

	@Test
	public void testDeserializerUser() throws JsonParseException, JsonMappingException, IOException {
		String userStr = "{\"id\": \"2\",\"name\": \"theUser\"}";
		User user = new ObjectMapper().readValue(userStr, User.class);
		System.out.println(user);
	}

	@Test(expected=UnrecognizedPropertyException.class)
	public void testCustomDeserializer() throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"id\": 1,\"itemName\": \"theItem\",\"createdBy\": 2}";
		Item item = new ObjectMapper().readValue(json, Item.class);
	}
	
	@Test
	public void testCustomDeserializer2() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Item.class, new ItemDeserializer());
		mapper.registerModule(module);
		 
		Item item = mapper.readValue(json, Item.class);
		System.out.println(item);
	}
}
