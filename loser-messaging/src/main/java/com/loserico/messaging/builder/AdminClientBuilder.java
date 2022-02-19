package com.loserico.messaging.builder;

import org.apache.kafka.clients.admin.Admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * <p>
 * Copyright: (C), 2022-02-18 19:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AdminClientBuilder extends BaseBuilder {
	
	/**
	 * 不用包含Kafka集群中的所有机器, 但最好多几个, 不然只写一台机器的话, 有可能正好那台机器挂了, 然后就连不上了
	 */
	private String bootstrapServers;
	
	/**
	 * 方便编程方式挨个添加BootstrapServer
	 */
	private List<String> bootstrapServerPorts = new ArrayList<>();
	
	/**
	 * Additional producer-specific properties used to configure the client.<p>
	 * 其他一些通用属性
	 */
	protected final Map<String, String> properties = new HashMap<>();
	
	@Override
	public AdminClientBuilder bootstrapServers(String bootstrapServers) {
		super.bootstrapServers(bootstrapServers);
		return this;
	}
	
	/**
	 * 添加一些其他的通用属性
	 *
	 * @param properties
	 * @return
	 */
	public AdminClientBuilder addProperties(Map<String, String> properties) {
		super.addProperties(properties);
		return this;
	}
	
	public Admin build() {
		return Admin.create(buildProperties());
	}
	
	
	private Map<String, Object> buildProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers());
		properties.putAll(this.properties);
		
		return properties;
	}
}
