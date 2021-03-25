package com.loserico.messaging.config;

import lombok.Data;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MIN_BYTES_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

/**
 * 消费者属性
 * <p>
 * Copyright: Copyright (c) 2021-01-27 17:47
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class Consumer {
	
	/**
	 * 默认 true
	 * Whether the consumer's offset is periodically committed in the background.
	 */
	private Boolean enableAutoCommit;
	
	/**
	 * 自动提交间隔的毫秒数, 默认 5000 毫秒
	 * Frequency with which the consumer offsets are auto-committed to Kafka if 'enable.auto.commit' is set to true.
	 */
	private Duration autoCommitInterval;
	
	/**
	 * 默认 latest
	 * What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server:
	 * <ul>
	 * <li>earliest</li>       automatically reset the offset to the earliest offset
	 * <li>latest</li>         automatically reset the offset to the latest offset
	 * <li>none</li>           throw exception to the consumer if no previous offset is found for the consumer's group
	 * <li>anything else</li>  throw exception to the consumer.
	 * </ul>
	 */
	private String autoOffsetReset;
	
	/**
	 * Comma-delimited list of host:port pairs to use for establishing the initial
	 * connections to the Kafka cluster. Overrides the global property, for consumers.
	 */
	private List<String> bootstrapServers;
	
	/**
	 * 默认 ""
	 * ID to pass to the server when making requests. Used for server-side logging.
	 * The purpose of this is to be able to track the source of requests beyond just ip/port by allowing
	 * a logical application name to be included in server-side request logging.
	 */
	private String clientId;
	
	/**
	 * Unique string that identifies the consumer group to which this consumer belongs.
	 */
	private String groupId;
	
	/**
	 * 默认 500
	 *
	 * The maximum amount of time the server will block before answering the fetch request 
	 * if there isn't sufficient data to immediately satisfy the requirement given by `fetch.min.bytes`.
	 */
	private Duration fetchMaxWait;
	
	/**
	 * Minimum amount of data the server should return for a fetch request.
	 */
	private long fetchMinSize;
	
	/**
	 * 默认 3000, 要小于 1/3 of session.timeout.ms(默认 10000)
	 * 
	 * The expected time between heartbeats to the consumer coordinator when using Kafka's group management facilities. 
	 * Heartbeats are used to ensure that the consumer's session stays active and to facilitate rebalancing when new consumers join or leave the group. 
	 * 
	 * The value must be set lower than session.timeout.ms, but typically should be set no higher than 1/3 of that value. 
	 * It can be adjusted even lower to control the expected time for normal rebalances. 
	 */
	private Duration heartbeatInterval;
	
	/**
	 * Deserializer class for keys.
	 */
	private Class<?> keyDeserializer = StringDeserializer.class;
	
	/**
	 * Deserializer class for values.
	 */
	private Class<?> valueDeserializer = StringDeserializer.class;
	
	/**
	 * 默认 500
	 * Maximum number of records returned in a single call to poll().
	 */
	private Integer maxPollRecords;
	
	/**
	 * Additional consumer-specific properties used to configure the client.
	 */
	private final Map<String, String> properties = new HashMap<>();
	
	public Map<String, Object> buildProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		properties.put(CLIENT_ID_CONFIG, clientId);
		properties.put(ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
		properties.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
		properties.put(FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWait.toMillis());
		properties.put(FETCH_MIN_BYTES_CONFIG, fetchMinSize);
		properties.put(GROUP_ID_CONFIG, groupId);
		properties.put(HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval.toMillis());
		properties.put(KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
		properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
		properties.put(MAX_POLL_RECORDS_CONFIG, maxPollRecords);
		
		properties.putAll(this.properties);
		
		return properties;
	}
	
}
