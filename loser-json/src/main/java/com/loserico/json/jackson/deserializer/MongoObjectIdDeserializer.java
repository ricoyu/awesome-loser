package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.io.IOException;

/**
 * MongoDB 的Document对象toJson后ObjectId序列化的结果为
 * {
 *   "_id": {
 *     "$oid": "5f17d8c023b6431762b34663"
 * }
 *
 * 这个反序列化器支持将其反序列化为String
 * 使用的时候字段上标注: @JsonDeserialize(using = MongoObjectIdDeserializer.class)
 * 
 * <p>
 * Copyright: (C), 2020-7-22 0022 16:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MongoObjectIdDeserializer extends JsonDeserializer<String> {
	
	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		TreeNode tree = jp.getCodec().readTree(jp);
		if (tree.isObject()) {
			return ((ValueNode) tree.get("$oid")).asText();
		}
		
		return null;
	}
}
