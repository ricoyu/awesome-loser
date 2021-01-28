package com.loserico.messages.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kafka 通用属性, Producer, Consumer 属性配置
 * <p>
 * Copyright: (C), 2021-01-27 15:27
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class KafkaProperties {
	
	/**
	 * 逗号隔开的Kafka Server列表, 默认localhost:9092
	 * 192.168.100.101:9092,192.168.100.102:9092
	 */
	private List<String> bootstrapServers = new ArrayList<>(Collections.singletonList("localhost:9092"));
	
	/**
	 * ID to pass to the server when making requests. Used for server-side logging.
	 */
	private String clientId;
	
	/**
	 * 一些额外的针对client端的通用配置项
	 * Additional properties, common to producers and consumers, used to configure the client.
	 */
	private final Map<String, String> properties = new HashMap<>();
	
	private final Consumer consumer = new Consumer();
	
	private final Producer producer = new Producer();
}
