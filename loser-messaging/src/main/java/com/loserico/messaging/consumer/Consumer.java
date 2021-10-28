package com.loserico.messaging.consumer;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.loserico.common.lang.concurrent.LoserExecutors;
import com.loserico.common.lang.concurrent.LoserThreadExecutor;
import com.loserico.common.lang.constants.Units;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

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
	
	private Boolean enableStatistic;
	
	/**
	 * 用来消息去重的布隆过滤器
	 */
	private BloomFilter<Long> filter = BloomFilter.create(Funnels.longFunnel(), 10000, 0.01);
	
	private LoserThreadExecutor POOL = new LoserThreadExecutor(Runtime.getRuntime().availableProcessors() + 1,
			20,
			100, TimeUnit.SECONDS);
	private ThreadPoolExecutor executor = LoserExecutors.of("loser-kafka-consumer")
			.queueSize(3000)
			.build();
	
	public Consumer(Map<String, Object> configs) {
		super(configs, null, null);
		this.enableStatistic = (Boolean) configs.get("enableStatistic");
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
	 * 订阅Topic, 消息来了之后处理
	 *
	 * @param topic
	 * @param listener
	 */
	public void subscribe(String topic, ConsumerListener listener) {
		subscribe(asList(topic));
		messageProcess(listener);
	}
	
	/**
	 * 订阅Topic, 消息来了之后处理
	 *
	 * @param topicPattern
	 * @param listener
	 */
	public void subscribePattern(String topicPattern, ConsumerListener listener) {
		subscribe(Pattern.compile(topicPattern));
		messageProcess(listener);
	}
	
	private void messageProcess(ConsumerListener listener) {
		try {
			while (true) {
				ConsumerRecords<String, String> consumerRecords = poll(pollDuration);
				int count = consumerRecords.count();
				if (count == 0) {
					continue;
				}
				
				List messages = new ArrayList(count);
				Set<Long> offsets = new HashSet<>();
				for (ConsumerRecord record : consumerRecords) {
					/*
					 * 这个offset位置的还没有消费过的话才会消费
					 */
					if (!filter.mightContain(record.offset())) {
						messages.add(record.value());
						offsets.add(record.offset());
					} else {
						log.info("过滤掉重复消息, Offset: {}", record.offset());
					}
				}
				
				if (isStatistic()) {
					List<Long> sortedOffsets = offsets.stream().sorted().collect(toList());
					log.info("本次拉取到Offset范围: {} ~ {}", sortedOffsets.get(0), sortedOffsets.get(sortedOffsets.size() - 1));
					
					StringBuilder sb = new StringBuilder();
					messages.forEach(msg -> sb.append(msg));
					byte[] bytes = sb.toString().getBytes();
					int messageInKB = bytes.length / Units.KB;
					log.info("拉取到消息大小{}KB", messageInKB);
					log.info("拉取到{}条消息", messages.size());
				}
				
				if (messages.isEmpty()) {
					continue;
				}
				
				try {
					executor.execute(() -> listener.onMessage(messages));
					commitSync();
					//将已消费的偏移量加入布隆过滤器
					offsets.forEach(offset -> filter.put(offset));
				} catch (Exception e) {
					log.info("消费消息失败, 不提交", e);
				}
			}
		} finally {
			log.warn("Consumer 停止 {}", DateUtils.format(new Date()));
			close();
		}
	}
	
	private boolean isStatistic() {
		return enableStatistic != null && enableStatistic.booleanValue();
	}
}
