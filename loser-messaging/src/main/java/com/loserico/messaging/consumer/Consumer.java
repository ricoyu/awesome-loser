package com.loserico.messaging.consumer;

import com.loserico.common.lang.concurrent.LoserThreadExecutor;
import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.DateUtils;
import com.loserico.messaging.listener.ConsumerListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

/**
 * <p>
 * Copyright: (C), 2021-04-28 13:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class Consumer<K, V> extends KafkaConsumer {
	
	/**
	 * Consumer poll的超时时间, Spring Kafka也是默认5000 ms<p>
	 * Set the max time to block in the consumer waiting for records.
	 */
	private Integer pollTimeout = 5000;
	
	private Duration pollDuration;
	
	private LoserThreadExecutor POOL = new LoserThreadExecutor(Runtime.getRuntime().availableProcessors() + 1,
			20,
			100, TimeUnit.SECONDS);
	
	public Consumer(Map<String, Object> configs) {
		super(configs, null, null);
		pollTimeout(configs);
	}
	
	public Consumer(Map<String, Object> configs,
	                Deserializer<K> keyDeserializer,
	                Deserializer<V> valueDeserializer) {
		super(configs, keyDeserializer, valueDeserializer);
		pollTimeout(configs);
	}
	
	public Consumer(Properties properties) {
		this(properties, null, null);
		pollTimeout(properties);
	}
	
	public Consumer(Properties properties,
	                Deserializer<K> keyDeserializer,
	                Deserializer<V> valueDeserializer) {
		super(properties, keyDeserializer, valueDeserializer);
		pollTimeout(properties);
	}
	
	private void pollTimeout(Map<String, Object> configs) {
		pollTimeout = (Integer) configs.get("poll.timeout");
		pollDuration = Duration.ofMillis(pollTimeout.longValue());
	}
	
	private void pollTimeout(Properties properties) {
		String timeout = (String) properties.get("poll.timeout");
		Integer timeoutMs = Transformers.convert(timeout, Integer.class);
		pollTimeout = (timeoutMs == null ? 5000 : timeoutMs);
		pollDuration = Duration.ofMillis(pollTimeout.longValue());
	}
	
	/**
	 * 订阅Topic, 并另起线程拉取消息, 消息来了之后另起线程异步处理
	 *
	 * @param topic
	 * @param listener
	 */
	public void subscribe(String topic, ConsumerListener listener) {
		subscribe(asList(topic));
		try {
			while (true) {
				ConsumerRecords<String, String> consumerRecords = poll(pollDuration);
				List messages = new ArrayList(consumerRecords.count());
				for (ConsumerRecord record : consumerRecords) {
					messages.add(record.value());
				}
				log.debug("拉取到{}条消息", messages.size());
				if (messages.isEmpty()) {
					continue;
				}
				
				try {
					listener.onMessage(messages);
					commitSync();
				} catch (Exception e) {
					log.error("消费消息失败, 不提交", e);
				}
			}
		} finally {
			log.warn("Consumer 停止 {}", DateUtils.format(new Date()));
			close();
		}
	}
}