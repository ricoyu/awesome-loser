package com.loserico.messaging.serializer;

import com.loserico.json.jackson.JacksonUtils;
import org.apache.kafka.common.serialization.Serializer;

/**
 * <p>
 * Copyright: (C), 2021-04-28 11:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JsonSerializer implements Serializer {
	
	@Override
	public byte[] serialize(String topic, Object data) {
		return JacksonUtils.toBytes(data);
	}
}
