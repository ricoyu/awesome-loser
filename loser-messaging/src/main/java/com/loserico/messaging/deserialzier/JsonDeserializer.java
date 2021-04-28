package com.loserico.messaging.deserialzier;

import com.loserico.json.jackson.JacksonUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2021-04-28 15:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JsonDeserializer implements Deserializer {
	
	private Class clazz;
	
	@Override
	public void configure(Map configs, boolean isKey) {
		clazz = (Class) configs.get("message.class");
	}
	
	@Override
	public Object deserialize(String topic, byte[] data) {
		if (clazz != null) {
			return JacksonUtils.toObject(data, clazz);
		}
		return new String(data, UTF_8);
	}
}
