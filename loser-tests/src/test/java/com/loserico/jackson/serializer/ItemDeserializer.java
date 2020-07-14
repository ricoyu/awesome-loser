package com.loserico.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

public class ItemDeserializer extends StdDeserializer<Item> {

	private static final long serialVersionUID = 1817593957526692385L;

	public ItemDeserializer() {
		this(null);
	}

	public ItemDeserializer(Class<?> vc) {
		super(vc);
	}

	/**
	 * As you can see, the deserializer is working with the standard Jackson
	 * representation of JSON – the JsonNode. Once the input JSON is represented as a
	 * JsonNode, we can now extract the relevant information from it and construct our
	 * own Item entity.
	 */
	@Override
	public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		int id = (Integer) ((IntNode) node.get("id")).numberValue();
		String itemName = node.get("itemName").asText();
		int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
		return new Item(id, itemName, new User(userId, null));
	}

}
