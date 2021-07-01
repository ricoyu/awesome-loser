package com.loserico.messaging.builder;

import com.loserico.common.lang.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.loserico.common.lang.utils.Assert.notNull;
import static com.loserico.common.lang.utils.CollectionUtils.isEmpty;
import static com.loserico.common.lang.utils.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: (C), 2021-04-27 14:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class BaseBuilder {
	
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
	
	/**
	 * 设置bootstrap.servers属性, 多个以逗号隔开<p>
	 * 192.168.100.101:9092,192.168.100.102:9092
	 *
	 * @param bootstrapServers
	 * @return BaseBuilder
	 */
	protected BaseBuilder bootstrapServers(String bootstrapServers) {
		notNull(bootstrapServers, "bootstrapServers cannot be null!");
		this.bootstrapServers = bootstrapServers;
		return this;
	}
	
	/**
	 * 添加BootstrapServer
	 *
	 * @param host
	 * @param port
	 * @return BaseBuilder
	 */
	protected BaseBuilder bootstrapServer(String host, String port) {
		notNull(host, "host cannot be null!");
		notNull(port, "port cannot be null!");
		bootstrapServerPorts.add(host + ":" + port);
		return this;
	}
	
	/**
	 * 添加一些其他的通用属性
	 * @param properties
	 * @return
	 */
	public BaseBuilder addProperties(Map<String, String> properties) {
		this.properties.putAll(properties);
		return this;
	}
	
	protected String bootstrapServers() {
		if (isNotBlank(bootstrapServers) && isEmpty(bootstrapServerPorts)) {
			return bootstrapServers;
		}
		
		if (isBlank(bootstrapServers) && isNotEmpty(bootstrapServerPorts)) {
			return StringUtils.joinWith(",", bootstrapServerPorts);
		}
		
		if (isNotBlank(bootstrapServers) && isNotEmpty(bootstrapServerPorts)) {
			String[] serverPortArray = bootstrapServers.split(",");
			for (int i = 0; i < serverPortArray.length; i++) {
				bootstrapServerPorts.add(serverPortArray[i]);
			}
		}
		
		return bootstrapServerPorts.stream()
				.distinct()
				.collect(Collectors.joining(","));
	}
}
