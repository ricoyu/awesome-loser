package com.loserico.messaging;

import com.loserico.messaging.builder.ConsumerBuilder;
import com.loserico.messaging.builder.ProducerBuilder;

/**
 * Kafka 收发消息工具类 
 * <p>
 * Copyright: Copyright (c) 2021-04-28 9:41
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class KafkaUtils {
	
	/**
	 * 创建一个新的Kafka Producer, bootstrapServers是 ip:port,ip:port这种形式
	 * @param bootstrapServers
	 * @return Producer
	 */
	public static ProducerBuilder newProducer(String bootstrapServers) {
		ProducerBuilder builder = new ProducerBuilder();
		builder.bootstrapServers(bootstrapServers);
		return builder;
	}
	
	/**
	 * 创建一个新的Kafka Producer
	 * 
	 * @param ip
	 * @param port
	 * @return ProducerBuilder
	 */
	public static ProducerBuilder newProducer(String ip, String port) {
		ProducerBuilder builder = new ProducerBuilder();
		builder.bootstrapServer(ip, port);
		return builder;
	}
	
	/**
	 * 创建一个新的Kafka Consumer, bootstrapServers是 ip:port,ip:port这种形式
	 * @param bootstrapServers
	 * @return ConsumerBuilder
	 */
	public static ConsumerBuilder newConsumer(String bootstrapServers) {
		ConsumerBuilder builder = new ConsumerBuilder();
		builder.bootstrapServers(bootstrapServers);
		return builder;
	}
	
	/**
	 * 创建一个新的Kafka Consumer
	 * @param ip
	 * @param port
	 * @return ConsumerBuilder
	 */
	public static ConsumerBuilder newConsumer(String ip, String port) {
		ConsumerBuilder builder = new ConsumerBuilder();
		builder.bootstrapServer(ip, port);
		return builder;
	}
}
