package com.loserico.messaging.producer;

import com.loserico.common.lang.utils.CollectionUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Serializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * <p>
 * Copyright: (C), 2021-04-28 10:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Producer<K, V> extends KafkaProducer {
	
	public Producer(final Map<String, Object> configs) {
		super(configs);
	}
	
	public Producer(Map<String, Object> configs, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
		super(configs, keySerializer, valueSerializer);
	}
	
	/**
	 * A producer is instantiated by providing a set of key-value pairs as configuration. Valid configuration strings
	 * are documented <a href="http://kafka.apache.org/documentation.html#producerconfigs">here</a>.
	 * <p>
	 * Note: after creating a {@code KafkaProducer} you must always {@link #close()} it to avoid resource leaks.
	 *
	 * @param properties The producer configs
	 */
	public Producer(Properties properties) {
		super(properties);
	}
	
	public Producer(Properties properties, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
		super(properties, keySerializer, valueSerializer);
	}
	
	/**
	 * 发送一条消息
	 *
	 * @param topic
	 * @param value
	 * @return Future<RecordMetadata>
	 */
	public Future<RecordMetadata> send(String topic, Object value) {
		return send(new ProducerRecord(topic, value));
	}
	
	/**
	 * 发送一批消息, 会将List中每一条消息挨个取出发送
	 *
	 * @param topic
	 * @param values
	 * @return Future<RecordMetadata>
	 */
	public List<Future<RecordMetadata>> send(String topic, List<?> values) {
		if (CollectionUtils.isEmpty(values)) {
			return Collections.emptyList();
		}
		
		List<Future<RecordMetadata>> results = new ArrayList<>(values.size());
		for (Object value : values) {
			Future<RecordMetadata> result = send(new ProducerRecord(topic, value));
			results.add(result);
		}
		
		return results;
	}
	
	/**
	 * 发送一条消息
	 *
	 * @param topic
	 * @param value
	 * @return Future<RecordMetadata>
	 */
	public Future<RecordMetadata> send(String topic, Object value, Callback callback) {
		return send(new ProducerRecord(topic, value), callback);
	}
	
	/**
	 * 发送一条消息, 指定key
	 *
	 * @param topic
	 * @param key
	 * @param value
	 * @return Future<RecordMetadata>
	 */
	public Future<RecordMetadata> send(String topic, K key, Object value) {
		return send(new ProducerRecord(topic, key, value));
	}
	
	/**
	 * 发送一条消息, 指定key
	 *
	 * @param topic
	 * @param key
	 * @param value
	 * @return Future<RecordMetadata>
	 */
	public Future<RecordMetadata> send(String topic, K key, Object value, Callback callback) {
		return send(new ProducerRecord(topic, key, value), callback);
	}
	
	/**
	 * 指定分区发送一条消息
	 *
	 * @param topic
	 * @param partition
	 * @param value
	 * @return Future<RecordMetadata>
	 */
	public Future<RecordMetadata> send(String topic, Integer partition, Object value) {
		return send(new ProducerRecord(topic, partition, null, value));
	}
	
	/**
	 * 指定分区发送一条消息
	 *
	 * @param topic
	 * @param partition
	 * @param value
	 * @return Future<RecordMetadata>
	 */
	public Future<RecordMetadata> send(String topic, Integer partition, Object value, Callback callback) {
		return send(new ProducerRecord(topic, partition, null, value), callback);
	}
	
	/**
	 * 指定分区发送一条消息, 指定key
	 *
	 * @param topic
	 * @param partition
	 * @param key
	 * @param value
	 * @return Future<RecordMetadata>
	 */
	public Future<RecordMetadata> send(String topic, Integer partition, K key, Object value) {
		return send(new ProducerRecord(topic, partition, key, value));
	}
	
	/**
	 * 指定分区发送一条消息, 指定key
	 *
	 * @param topic
	 * @param partition
	 * @param key
	 * @param value
	 * @return Future<RecordMetadata>
	 */
	public Future<RecordMetadata> send(String topic, Integer partition, K key, Object value, Callback callback) {
		return send(new ProducerRecord(topic, partition, key, value), callback);
	}
}
