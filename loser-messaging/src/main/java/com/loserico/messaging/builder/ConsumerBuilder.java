package com.loserico.messaging.builder;

import com.loserico.messaging.consumer.Consumer;
import com.loserico.messaging.deserialzier.JsonDeserializer;
import com.loserico.messaging.enums.OffsetReset;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.loserico.common.lang.utils.Assert.notNull;
import static com.loserico.messaging.enums.OffsetReset.LATEST;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MAX_BYTES_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MIN_BYTES_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG;
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
	private OffsetReset autoOffsetReset = LATEST;
	
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
	 * 默认57671680 55M
	 * The maximum number of bytes we will return for a fetch request. Must be at least 1024.
	 * <p>
	 * fetch.max.bytes
	 */
	private Integer fetchMaxBytes;
	
	/**
	 * Minimum amount of data the server should return for a fetch request.
	 */
	private Integer fetchMinBytes;
	
	/**
	 * 默认 1048576 1M
	 * <p>
	 * The maximum amount of data per-partition the server will return. Records are fetched in batches by the consumer.
	 * If the first record batch in the first non-empty partition of the fetch is larger than this limit,
	 * the batch will still be returned to ensure that the consumer can make progress.
	 * The maximum record batch size accepted by the broker is defined via message.max.bytes (broker config) or max.message.bytes (topic config).
	 * See fetch.max.bytes for limiting the consumer request size.
	 * <p>
	 * max.partition.fetch.bytes
	 */
	private Integer maxPartitionFetchBytes;
	
	/**
	 * 默认 500
	 * Maximum number of records returned in a single call to poll().
	 * max.poll.records
	 */
	private Integer maxPollRecords;
	
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
	 * session.timeout.ms 默认 10000
	 * <p/>
	 * The timeout used to detect consumer failures when using Kafka's group management facility. The consumer sends periodic heartbeats
	 * to indicate its liveness to the broker. If no heartbeats are received by the broker before the expiration of this session timeout,
	 * then the broker will remove this consumer from the group and initiate a rebalance. Note that the value must be in the allowable
	 * range as configured in the broker configuration by group.min.session.timeout.ms and group.max.session.timeout.ms.
	 * <p/>
	 * 简言之就是consumer客户端的session超时时间 <br/>
	 * 该属性指定了消费者在被认为死亡之前可以与服务器断开连接的时间，默认是 3s。 <br/>
	 * 如果消费者没有在 session.timeout.ms 指定的时间内发送心跳给群组协调器, 就被认为已经死亡, 协调器就会触发再均衡, 把它的分区分配给群组里的其他消费者。
	 * <p/>
	 * 该属性与 heartbeat.interval.ms 紧密相关。heartbeat.interval.ms 指定了 poll() 方法向协调器发送心跳的频率, session.timeout.ms 则指定了消费者可以多久不发送心跳。
	 * <p/>
	 * 所以, 一般需要同时修改这两个属性, heartbeat.interval.ms 必须比 session.timeout.ms 小, 一般是 session.timeout.ms 的三分之一。
	 * 如果session.timeout.ms 是 3s, 那么 heartbeat.interval.ms 应该是 1s。
	 * <p/>
	 * 把 session.timeout.ms 值设得比默认值小，可以更快地检测和恢复崩溃的节点, 不过长时间的轮询或垃圾收集可能导致非预期的再均衡。
	 * 把该属性的值设置得大一些, 可以减少意外的再均衡, 不过检测节点崩溃需要更长的时间。
	 */
	private Integer sessionTimeout;
	
	/**
	 * Consumer 调用poll(long)的超时时间, Spring Kafka也是默认5000 ms<p>
	 * Set the max time to block in the consumer waiting for records.
	 */
	private Integer pollTimeout = 5000;
	
	/**
	 * max.poll.interval.ms 300000 5m
	 * <p>
	 * The maximum delay between invocations of poll() when using consumer group management.
	 * This places an upper bound on the amount of time that the consumer can be idle before fetching more records.
	 * If poll() is not called before expiration of this timeout, then the consumer is considered failed and the
	 * group will rebalance in order to reassign the partitions to another member.
	 * <p>
	 * Consumer两次调用poll的间隔, 如果超过这个值仍没有调用poll, 会发生rebalance
	 */
	private Integer maxPollInterval = 300000;
	/**
	 * message 类型, 如果指定, 自动帮你反序列化成该对象
	 */
	private Class messageClass;
	
	/**
	 * 记录每次拉取消息的统计信息, 如一次拉取到多少条, 总共占多少字节
	 */
	private Boolean enableStatistic;
	
	/**
	 * 同步还是异步提交offset, 默认异步
	 * <ul>
	 *     <li/>The commitSync is a blocking method. Calling it will block your thread until it either succeeds or fails. 阻塞方法, 高并发下延迟会高, 但是保证数据一致性
	 *     <li/>The commitAsync is a non-blocking method. Calling it will not block your thread. 异步方法, 低延迟, 但是不保证数据一致性 <p/>
	 *     Instead, it will continue processing the following instructions, no matter whether it will succeed or fail eventually.
	 * </ul>
	 */
	private boolean commitAsync = true;
	
	/**
	 * Consumer拉取消息后直接丢到一个BlockingQueue里面, 然后直接拉取下一批消息
	 * 工作线程从BlockingQueue拉取消息进行处理
	 */
	private Integer queueSize;
	
	/**
	 * 工作线程的核心线程数, 默认CPU核数+1
	 */
	private Integer workerThreads;
	
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
	 * 开启消费端自动提交, 默认true<p>
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
	 * 同步还是异步提交offset, 默认异步
	 * <ul>
	 *     <li/>The commitSync is a blocking method. Calling it will block your thread until it either succeeds or fails. 阻塞方法, 高并发下延迟会高, 但是保证数据一致性
	 *     <li/>The commitAsync is a non-blocking method. Calling it will not block your thread. 异步方法, 低延迟, 但是不保证数据一致性
	 *     Instead, it will continue processing the following instructions, no matter whether it will succeed or fail eventually.
	 * </ul>
	 *
	 * @param commitAsync
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder commitAsync(boolean commitAsync) {
		this.commitAsync = commitAsync;
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
	 * 默认57671680 55M
	 * The maximum number of bytes we will return for a fetch request. Must be at least 1024.
	 * <p>
	 * fetch.max.bytes
	 *
	 * @param fetchMaxBytes
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder fetchMaxBytes(Integer fetchMaxBytes) {
		this.fetchMaxBytes = fetchMaxBytes;
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
	 * @param fetchMinBytes
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder fetchMinBytes(Integer fetchMinBytes) {
		this.fetchMinBytes = fetchMinBytes;
		return this;
	}
	
	/**
	 * 默认 1048576 1M
	 * <br/>
	 * 从一个Partition上一次拉取消息的bytes上限, 如果消息都在一个Partition上, 那么限制了一次拉取消息的大小
	 * <p/>
	 * The maximum amount of data per-partition the server will return. Records are fetched in batches by the consumer.
	 * If the first record batch in the first non-empty partition of the fetch is larger than this limit,
	 * the batch will still be returned to ensure that the consumer can make progress.
	 * The maximum record batch size accepted by the broker is defined via message.max.bytes (broker config) or max.message.bytes (topic config).
	 * See fetch.max.bytes for limiting the consumer request size.
	 * <p>
	 * max.partition.fetch.bytes
	 *
	 * @param maxPartitionFetchBytes
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder maxPartitionFetchBytes(Integer maxPartitionFetchBytes) {
		this.maxPartitionFetchBytes = maxPartitionFetchBytes;
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
	 * session.timeout.ms 默认 10000 <br/>
	 * 如果在这个期间内没有发送心跳, 就会发生Rebalance
	 * <p/>
	 * 绝对要注意: 这个参数如果调大了, 比如调成5分钟, 那么你的客户端关掉后重开, 可能会一时半会收不到消息 <p/>
	 * 然后你后一脸懵逼, 会抓狂 <p/>
	 * 原因是前一个客户端关掉后, 因为你设置了比较大的session超时时间, Broker会认为前一个客户端连接还在, 不一定会马上把消息发给新客户端 <p/>
	 * The timeout used to detect consumer failures when using Kafka's group management facility. The consumer sends periodic heartbeats
	 * to indicate its liveness to the broker. If no heartbeats are received by the broker before the expiration of this session timeout,
	 * then the broker will remove this consumer from the group and initiate a rebalance. Note that the value must be in the allowable
	 * range as configured in the broker configuration by group.min.session.timeout.ms and group.max.session.timeout.ms.
	 * <p/>
	 * 简言之就是consumer客户端的session超时时间 <br/>
	 * 该属性指定了消费者在被认为死亡之前可以与服务器断开连接的时间，默认是 3s。 <br/>
	 * 如果消费者没有在 session.timeout.ms 指定的时间内发送心跳给群组协调器, 就被认为已经死亡, 协调器就会触发再均衡, 把它的分区分配给群组里的其他消费者。
	 * <p/>
	 * 该属性与 heartbeat.interval.ms 紧密相关。heartbeat.interval.ms 指定了 poll() 方法向协调器发送心跳的频率, session.timeout.ms 则指定了消费者可以多久不发送心跳。
	 * <p/>
	 * 所以, 一般需要同时修改这两个属性, heartbeat.interval.ms 必须比 session.timeout.ms 小, 一般是 session.timeout.ms 的三分之一。
	 * 如果session.timeout.ms 是 3s, 那么 heartbeat.interval.ms 应该是 1s。
	 * <p/>
	 * 把 session.timeout.ms 值设得比默认值小，可以更快地检测和恢复崩溃的节点, 不过长时间的轮询或垃圾收集可能导致非预期的再均衡。
	 * 把该属性的值设置得大一些, 可以减少意外的再均衡, 不过检测节点崩溃需要更长的时间。
	 */
	public ConsumerBuilder sessionTimeout(Integer sessionTimeout, TimeUnit timeUnit) {
		this.sessionTimeout = (int) timeUnit.toMillis(sessionTimeout.longValue());
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
		this.pollTimeout = pollTimeout;
		return this;
	}
	
	/**
	 * max.poll.interval.ms 300000 5m
	 * <p>
	 * The maximum delay between invocations of poll() when using consumer group management.
	 * This places an upper bound on the amount of time that the consumer can be idle before fetching more records.
	 * If poll() is not called before expiration of this timeout, then the consumer is considered failed and the
	 * group will rebalance in order to reassign the partitions to another member.
	 * <p>
	 * Consumer两次调用poll的间隔, 如果超过这个值仍没有调用poll, 会发生rebalance
	 *
	 * @param maxPollInterval
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder maxPollInterval(Integer maxPollInterval) {
		this.maxPollInterval = maxPollInterval;
		return this;
	}
	
	/**
	 * 默认 500<p> 最多一次能拉取多少条消息
	 * Maximum number of records returned in a single call to poll().<p>
	 * 设置 max.poll.records 属性
	 *
	 * @param maxPollRecords
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder maxPollRecords(Integer maxPollRecords) {
		notNull(maxPollRecords, "maxPollRecords cannot be null!");
		this.maxPollRecords = maxPollRecords;
		return this;
	}
	
	/**
	 * message 类型, 如果指定, 拉取到消息后自动帮你反序列化成该对象
	 *
	 * @param messageClass
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder messageClass(Class messageClass) {
		this.messageClass = messageClass;
		return this;
	}
	
	/**
	 * 记录每次拉取消息的统计信息, 如一次拉取到多少条, 总共占多少字节
	 *
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder enableStatistic() {
		this.enableStatistic = true;
		return this;
	}
	
	/**
	 * 工作线程的核心线程数, 默认CPU核数+1
	 *
	 * @param workerThreads
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder workerThreads(Integer workerThreads) {
		this.workerThreads = workerThreads;
		return this;
	}
	
	/**
	 * Consumer拉取消息后直接丢到一个BlockingQueue里面, 然后直接拉取下一批消息
	 * 工作线程从BlockingQueue拉取消息进行处理
	 *
	 * @param queueSize
	 * @return ConsumerBuilder
	 */
	public ConsumerBuilder queueSize(Integer queueSize) {
		this.queueSize = queueSize;
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
		Map<String, Object> configs = buildProperties();
		Deserializer keyDeserializer = new StringDeserializer();
		Deserializer valueDeserializer = new JsonDeserializer();
		valueDeserializer.configure(configs, false);
		return new Consumer(configs, keyDeserializer, valueDeserializer);
	}
	
	private Map<String, Object> buildProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers());
		properties.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put("commitAsync", commitAsync);
		
		if (enableStatistic != null) {
			properties.put("enableStatistic", enableStatistic);
		}
		
		if (autoOffsetReset != null) {
			properties.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset.toString());
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
		if (maxPollRecords != null) {
			properties.put(MAX_POLL_RECORDS_CONFIG, maxPollRecords);
		}
		if (fetchMaxBytes != null) {
			properties.put(FETCH_MAX_BYTES_CONFIG, fetchMaxBytes);
		}
		if (fetchMinBytes != null) {
			properties.put(FETCH_MIN_BYTES_CONFIG, fetchMinBytes);
		}
		if (maxPartitionFetchBytes != null) {
			properties.put(MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
		}
		if (isNotBlank(groupId)) {
			properties.put(GROUP_ID_CONFIG, groupId);
		}
		if (heartbeatInterval != null) {
			properties.put(HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
		}
		if (sessionTimeout != null) {
			properties.put(SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
		}
		if (messageClass != null) {
			properties.put("message.class", messageClass);
		}
		
		if (pollTimeout != null) {
			properties.put("poll.timeout", pollTimeout);
		}
		if (maxPollInterval != null) {
			properties.put(MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
		}
		
		if (workerThreads != null) {
			properties.put("workerThreads", workerThreads);
		}
		if (queueSize != null) {
			properties.put("queueSize", queueSize);
		}
		
		properties.putAll(this.properties);
		
		return properties;
	}
}
