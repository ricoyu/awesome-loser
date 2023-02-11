package com.loserico.rocketmq;

import com.loserico.common.lang.utils.PrimitiveUtils;
import com.loserico.json.jackson.JacksonUtils;
import org.apache.rocketmq.common.message.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2023-01-16 15:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class MessageBuilder {
	
	private String topic;
	
	private String tags;
	
	private String keys;
	
	private byte[] body;
	
	private Integer delayLevel;
	
	private Map<String, String> userProperties = new HashMap<>();
	
	private MessageBuilder() {
		
	}
	
	public static MessageBuilder create(String topic) {
		MessageBuilder builder = new MessageBuilder();
		builder.setTopic(topic);
		return builder;
	}
	
	void setTopic(String topic) {
		this.topic = topic;
	}
	
	public MessageBuilder tags(String tags) {
		this.tags = tags;
		return this;
	}
	
	public MessageBuilder delayLevel(Integer delayLevel) {
		this.delayLevel = delayLevel;
		return this;
	}
	
	public MessageBuilder keys(String keys) {
		this.keys = keys;
		return this;
	}
	
	/**
	 * 添加用户自定义属性, 用于消费端SQL过滤
	 *
	 * @param key
	 * @param value
	 * @return MessageBuilder
	 */
	public MessageBuilder userProperty(String key, String value) {
		this.userProperties.put(key, value);
		return this;
	}
	
	public <T> MessageBuilder body(T body) {
		Objects.requireNonNull(body, "body cannot be null!");
		if (body instanceof String) {
			this.body = ((String) body).getBytes(UTF_8);
		} else if (PrimitiveUtils.isByteArray(body)) {
			this.body = (byte[]) body;
		} else if (PrimitiveUtils.isPrimitive(body)) {
			this.body = PrimitiveUtils.toString(body).getBytes(UTF_8);
		} else {
			this.body = JacksonUtils.toBytes(body);
		}
		return this;
	}
	
	public Message build() {
		Message message = new Message(topic, tags, keys, body);
		if (this.delayLevel != null) {
			message.setDelayTimeLevel(delayLevel);
		}
		if (!userProperties.isEmpty()) {
			for (Map.Entry<String, String> entry : userProperties.entrySet()) {
				message.putUserProperty(entry.getKey(), entry.getValue());
			}
		}
		return message;
	}
}
