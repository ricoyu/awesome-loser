package com.loserico.messaging.config;

import com.loserico.messaging.enums.OffsetReset;
import lombok.Data;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ConsumerProperties {
	
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
	 * <p>
	 * The maximum amount of time the server will block before answering the fetch request
	 * if there isn't sufficient data to immediately satisfy the requirement given by `fetch.min.bytes`.
	 */
	private Duration fetchMaxWait;
	
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
	private Duration heartbeatInterval;
	
	/**
	 * Deserializer class for keys.
	 */
	private Class<? extends Deserializer> keyDeserializer = StringDeserializer.class;
	
	/**
	 * Deserializer class for values.
	 */
	private Class<? extends Deserializer> valueDeserializer = StringDeserializer.class;
	
	/**
	 * 默认 500
	 * Maximum number of records returned in a single call to poll().
	 */
	private Integer maxPollRecords;
	
	/**
	 * Additional consumer-specific properties used to configure the client.
	 */
	protected final Map<String, String> properties = new HashMap<>();
}
