package com.loserico.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Now, let’s simplify the JSON output above by only serializing the id of the User,
 * not the entire User object; we’d like to get the following, simpler JSON:
 * 
 * @author Rico Yu ricoyu520@gmail.com
 * @since 2017-08-08 15:19
 * @version 1.0
 *
 */
public class ItemSerializer extends StdSerializer<Item> {

	public ItemSerializer() {
		this(null);
	}

	public ItemSerializer(Class<Item> t) {
		super(t);
	}

	private static final long serialVersionUID = 6817170336184808574L;

	@Override
	public void serialize(Item value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id", value.id);
		gen.writeStringField("itemName", value.itemName);
		gen.writeNumberField("owner", value.owner.id);
		gen.writeEndObject();
	}

}
