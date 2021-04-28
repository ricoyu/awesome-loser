package com.loserico.messaging.builder;

import com.loserico.messaging.consumer.Consumer;
import com.loserico.messaging.enums.OffsetReset;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.loserico.common.lang.utils.Assert.notNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
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
 * KafkaConsumer 构造器
 * <p>
 * Copyright: (C), 2021-04-27 14:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ConsumerBuilder extends BaseBuilder {
	
	
	/**
	 * 默认 true<p>
	 * Whether the consumer's offset is periodically committed in the background.<p>
	 * 如果消费端是自动提交, 万一消费的数据还没处理完, 就自动提交offset了, 如果此时Consumer直接宕机, 未处理完的数据就丢失了, 而且下次也消费不到了
	 */
	private Boolean enableAutoCommit;
	
	/**
	 * 自动提交间隔的毫秒数, 默认 5000 毫秒<p>
	 * Frequency with which the consumer offsets are auto-committed to Kafka if 'enable.auto.commit' is set to true.
	 */
	private Duration autoCommitInterval;
	
	/**
	 * 默认 latest<p>
	 * What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server:
	 * <ul>
	 * <li>earliest</li>       automatically reset the offset to the earliest offset
	 * <li>latest</li>         automatically reset the offset to the latest offset
	 * <li>none</li>           throw exception to the consumer if no previous offset is found for the consumer's group
	 * <li>anything else</li>  throw exception to the consumer.
	 * </ul>
	 */
	private OffsetReset autoOffsetReset;
	
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
	 * 默认 500 ms
	 * <p>
	 * The maximum amount of time the server will block before answering the fetch request
	 * if there isn't sufficient data to immediately satisfy the requirement given by `fetch.min.bytes`.
	 */
	private Integer fetchMaxWait;
	
	/**
	 * Minimum amount of data the server should return for a fetch request.
	 */
	private Long fetchMinSize;
	
	/**
	 * 默认 3000, 要小于 1/3 of session.timeout.ms(默认 10000)
	 * <p>
	 * The expected time between heartbeats to the consumer coordinator when using Kafka's group management facilities.
	 * Heartbeats are used to ensure that the consumer's session stays active and to facilitate rebalancing when new consumers join or leave the group.
	 * <p>
	 * The value must be set lower than session.timeout.ms, but typically should be set no higher than 1/3 of that value.
	 * It can be adjusted even lower to control the expected time for normal rebalances.
	 */
	private Integer heartbeatInterval;
	
	/**
	 * Consumer poll的超时时间, Spring Kafka也是默认5000 ms<p>
	 * Set the max time to block in the consumer waiting for records.
	 */
	private Integer pollTimeout = 5000;
	
	/**
	 * Deserializer class for keys.
	 */
	private Class<? extends Deserializer> keyDeserializer = StringDeserializer.class;
	
	/**
	 * Deserializer class for values.
	 */
	private Class<? extends Deserializer> valueDeserializer = StringDeserializer.class;
	
	/**
	 * message 类型, 如果指定, 自动帮你反序列化成该对象
	 */
	private Class messageClass;
	
	/**
	 * 默认 500
	 * Maximum number of records returned in a single call to poll().
	 * max.poll.records
	 */
	private Integer maxPollRecords;
	
	@Override
	public ConsumerBuilder bootstrapServers(String bootstrapServers) {
		super.bootstrapServers(bootstrapServers);
		return this;
	}
	
	@Override
	public ConsumerBuilder bootstrapServer(String host, String port) {
		super.bootstrapServer(host, port);
		return this;
	}
	
	/**
	 * 开启消费端自动提交<p>
	 * 如果消费端是自动提交, 万一消费的数据还没处理完, 就自动提交offset了, 如果此时Consumer直接宕机, 未处理完的数据就丢失了, 而且下次也消费不到了
	 *
	 * @param enableAutoCommit
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder autoCommit(Boolean enableAutoCommit) {
		this.enableAutoCommit = enableAutoCommit;
		return this;
	}
	
	/**
	 * 开启消费端自动提交<p>
	 * 如果消费端是自动提交, 万一消费的数据还没处理完, 就自动提交offset了, 如果此时Consumer直接宕机, 未处理完的数据就丢失了, 而且下次也消费不到了
	 *
	 * @param enableAutoCommit
	 * @param autoCommitInterval
	 * @param timeUnit
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder autoCommit(Boolean enableAutoCommit, Long autoCommitInterval, TimeUnit timeUnit) {
		notNull(enableAutoCommit, "autoCommit cannot be null!");
		notNull(autoCommitInterval, "autoCommitInterval cannot be null!");
		notNull(timeUnit, "timeUnit cannot be null!");
		this.enableAutoCommit = enableAutoCommit;
		this.autoCommitInterval = Duration.ofMillis(timeUnit.toMillis(autoCommitInterval));
		return this;
	}
	
	/**
	 * 默认 latest<p>
	 * What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server:
	 * <ul>
	 * <li>earliest</li>       automatically reset the offset to the earliest offset
	 * <li>latest</li>         automatically reset the offset to the latest offset
	 * <li>none</li>           throw exception to the consumer if no previous offset is found for the consumer's group
	 * <li>anything else</li>  throw exception to the consumer.
	 * </ul>
	 *
	 * @param autoOffsetReset
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder autoOffsetReset(OffsetReset autoOffsetReset) {
		this.autoOffsetReset = autoOffsetReset;
		return this;
	}
	
	public ConsumerBuilder clientId(String clientId) {
		super.clientId(clientId);
		return this;
	}
	
	/**
	 * Unique string that identifies the consumer group to which this consumer belongs.
	 *
	 * @param groupId
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}
	
	/**
	 * 默认 500 ms
	 * <p>
	 * The maximum amount of time the server will block before answering the fetch request
	 * if there isn't sufficient data to immediately satisfy the requirement given by `fetch.min.bytes`.
	 *
	 * @param fetchMaxWait
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder fetchMaxWait(Integer fetchMaxWait) {
		notNull(fetchMaxWait, "fetchMaxWait cannot be null!");
		this.fetchMaxWait = fetchMaxWait;
		return this;
	}
	
	/**
	 * 设置fetch.min.bytes参数<p>
	 * <p>
	 * The minimum amount of data the server should return for a fetch request. <p>
	 * If insufficient data is available the request will wait for that much data to accumulate before answering the request.
	 * The default setting of 1 byte means that fetch requests are answered as soon as
	 * a single byte of data is available or the fetch request times out waiting for data to arrive.
	 * Setting this to something greater than 1 will cause the server to wait for larger amounts of
	 * data to accumulate which can improve server throughput a bit at the cost of some additional latency.
	 * <p>
	 * 设置大于1可以提供吞吐量但是延迟会高
	 *
	 * @param fetchMinSize
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder fetchMinSzie(Long fetchMinSize) {
		this.fetchMinSize = fetchMinSize;
		return this;
	}
	
	/**
	 * 默认 3000, 要小于 1/3 of session.timeout.ms(默认 10000)
	 * <p>
	 * The expected time between heartbeats to the consumer coordinator when using Kafka's group management facilities.
	 * Heartbeats are used to ensure that the consumer's session stays active and to facilitate rebalancing when new consumers join or leave the group.
	 * <p>
	 * The value must be set lower than session.timeout.ms, but typically should be set no higher than 1/3 of that value.
	 * It can be adjusted even lower to control the expected time for normal rebalances.
	 */
	public ConsumerBuilder heartbeatInterval(Integer interval) {
		notNull(interval, "interval cannot be null!");
		this.heartbeatInterval = interval;
		return this;
	}
	
	/**
	 * Consumer poll的超时时间, Spring Kafka也是默认5000 ms<p>
	 * Set the max time to block in the consumer waiting for records.
	 * 
	 * @param pollTimeout
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder pollTimeout(Integer pollTimeout) {
		this.pollTimeout= pollTimeout;
		return this;
	}
	
	/**
	 * 默认 StringDeserializer
	 *
	 * @param keyDeserializer
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder keyDeserializer(Class<? extends Deserializer> keyDeserializer) {
		notNull(keyDeserializer, "keyDeserializer cannot null!");
		this.keyDeserializer = keyDeserializer;
		return this;
	}
	
	/**
	 * StringDeserializer
	 *
	 * @param valueDeserializer
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder valueDeserializer(Class<? extends Deserializer> valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
		return this;
	}
	
	/**
	 * 默认 500<p>
	 * Maximum number of records returned in a single call to poll().<p>
	 * 设置 max.poll.records 属性
	 *
	 * @param maxPollRecords
	 * @return
	 */
	public ConsumerBuilder maxPollRecords(Integer maxPollRecords) {
		notNull(maxPollRecords, "maxPollRecords cannot be null!");
		this.maxPollRecords = maxPollRecords;
		return this;
	}
	
	/**
	 * 拉取到消息后是否要反序列化成对象
	 * @param messageClass
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder messageClass(Class messageClass) {
		this.messageClass = messageClass;
		return this;
	}
	
	
	/**
	 * 添加一些其他的通用属性
	 *
	 * @param properties
	 * @return
	 */
	public ConsumerBuilder addProperties(Map<String, String> properties) {
		super.addProperties(properties);
		return this;
	}
	
	public Consumer<String, String> build() {
		Map<String, Object> properties = buildProperties();
		return new Consumer(properties);
	}
	
	private Map<String, Object> buildProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers());
		if (autoOffsetReset != null) {
			properties.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		}
		if (isNotBlank(clientId)) {
			properties.put(CLIENT_ID_CONFIG, clientId);
		}
		if (enableAutoCommit != null) {
			properties.put(ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
		}
		if (autoCommitInterval != null) {
			properties.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
		}
		if (fetchMaxWait != null) {
			properties.put(FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWait);
		}
		if (fetchMinSize != null) {
			properties.put(FETCH_MIN_BYTES_CONFIG, fetchMinSize);
		}
		if (isNotBlank(groupId)) {
			properties.put(GROUP_ID_CONFIG, groupId);
		}
		if (heartbeatInterval != null) {
			properties.put(HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
		}
		if (keyDeserializer != null) {
			properties.put(KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
		}
		if (valueDeserializer != null) {
			properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
		}
		if (maxPollRecords != null) {
			properties.put(MAX_POLL_RECORDS_CONFIG, maxPollRecords);
		}
		
		if (messageClass != null) {
			properties.put("message.class", messageClass);
		}
		
		if (pollTimeout != null) {
			properties.put("poll.timeout", pollTimeout);
		}
		
		properties.putAll(this.properties);
		
		return properties;
	}
}
