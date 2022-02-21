package com.loserico.messaging.consumer;

import com.loserico.common.lang.concurrent.LoserExecutors;
import com.loserico.common.lang.concurrent.Policy;
import com.loserico.common.lang.constants.Units;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.messaging.consumer.listener.AutoCommitListener;
import com.loserico.messaging.consumer.listener.Listener;
import com.loserico.messaging.consumer.listener.ManualCommitListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static com.loserico.common.lang.utils.Assert.notNull;
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
	 * Consumer 调用poll(long)的超时时间, Spring Kafka也是默认5000 ms<p>
	 * Set the max time to block in the consumer waiting for records.
	 */
	private Integer pollTimeout = 5000;
	
	private Duration pollDuration;
	
	private Boolean enableStatistic;
	
	private boolean autoCommit = true;
	
	/**
	 * 同步还是异步提交offset
	 * <ul>
	 *     <li/>The commitSync is a blocking method. Calling it will block your thread until it either succeeds or fails.
	 *     <li/>The commitAsync is a non-blocking method. Calling it will not block your thread.
	 *     Instead, it will continue processing the following instructions, no matter whether it will succeed or fail eventually.
	 * </ul>
	 */
	//private boolean commitAsync = true;
	
	private ThreadPoolExecutor executor = null;
	
	public Consumer(Map<String, Object> configs, Deserializer<String> keyDeserializer, Deserializer valueDeserializer) {
		super(configs, keyDeserializer, valueDeserializer);
		this.enableStatistic = (Boolean) configs.get("enableStatistic");
		//this.commitAsync = (Boolean) configs.get("commitAsync");
		Boolean autoCommit = (Boolean) configs.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);
		if (autoCommit != null) {
			this.autoCommit = autoCommit;
		}
		pollTimeout(configs);
		buildWorkingPool(configs);
	}
	
	private void buildWorkingPool(Map<String, Object> configs) {
		LoserExecutors loserExecutors = LoserExecutors.of("kafka-worker")
				.queueSize(100)
				.rejectPolicy(Policy.CALLER_RUNS);
		Integer corePoolSize = LoserExecutors.NCPUS + 1;
		loserExecutors.corePoolSize(corePoolSize);
		
		Integer queueSize = (Integer) configs.get("queueSize");
		if (queueSize != null) {
			loserExecutors.queueSize(queueSize); //覆盖默认配置
		}
		Integer maxPoolSize = (Integer) configs.get("workerThreads");
		if (maxPoolSize == null) {
			maxPoolSize = corePoolSize * 2;
		} else if (maxPoolSize < corePoolSize) {
			maxPoolSize = corePoolSize * 2;
		}
		loserExecutors.maximumPoolSize(maxPoolSize);
		executor = loserExecutors.build();
	}
	
	private void pollTimeout(Map<String, Object> configs) {
		pollTimeout = (Integer) configs.get("poll.timeout");
		pollDuration = Duration.ofMillis(pollTimeout.longValue());
	}
	
	/**
	 * 订阅Topic, 消息来了之后处理
	 *
	 * @param topic
	 * @param listener AutoCommitListener#onMessage(msgs)
	 */
	public void subscribe(String topic, AutoCommitListener listener) {
		subscribe(topic, null, listener);
	}
	
	/**
	 * 订阅Topic, 消息来了之后处理
	 *
	 * @param topic
	 * @param listener ManualCommitListener#onMessage(msgs, consumer)
	 */
	public void subscribe(String topic, ManualCommitListener listener) {
		subscribe(topic, null, listener);
	}
	
	/**
	 * 订阅Topic, 消息来了之后处理
	 *
	 * @param topic
	 * @param partition 消费指定的分区
	 * @param listener AutoCommitListener#onMessage(msgs)
	 */
	public void subscribe(String topic, Integer partition, AutoCommitListener listener) {
		notNull(topic, "topic cannot be null!");
		notNull(listener, "listener cannot be null!");
		if (!this.autoCommit) {
			throw new IllegalArgumentException("You'v disabled auto commit message, so you should use a ManualCommitListener");
		}
		/*
		 * 如果显式指定了消费某个分区的数据, 消费组的rebalance机制是不生效的
		 */
		if (partition != null) {
			assign(asList(new TopicPartition(topic, partition)));
		} else {
			subscribe(asList(topic));
		}
		startPolling(listener);
	}
	
	/**
	 * 订阅Topic, 消息来了之后处理
	 *
	 * @param topic
	 * @param partition 消费指定的分区
	 * @param listener ManualCommitListener#onMessage(msgs, consumer)
	 */
	public void subscribe(String topic, Integer partition, ManualCommitListener listener) {
		notNull(topic, "topic cannot be null!");
		notNull(listener, "listener cannot be null!");
		if (this.autoCommit) {
			throw new IllegalArgumentException("You'v enabled auto commit message, so you should use a AutoCommitListener");
		}
		/*
		 * 如果显式指定了消费某个分区的数据, 消费组的rebalance机制是不生效的
		 */
		if (partition != null) {
			assign(asList(new TopicPartition(topic, partition)));
		} else {
			subscribe(asList(topic));
		}
		startPolling(listener);
	}
	
	/**
	 * 订阅Topic, 消息来了之后处理
	 *
	 * @param topicPattern
	 * @param listener
	 */
	public void subscribePattern(String topicPattern, Listener listener) {
		subscribe(Pattern.compile(topicPattern));
		startPolling(listener);
	}
	
	/**
	 * 异步拉取消息
	 */
	private void startPolling(Listener listener) {
		new Thread(() -> {
			while (true) {
				try {
					//poll() 方法只能由单个线程调用, 不能多线程去poll
					ConsumerRecords<String, String> consumerRecords = poll(pollDuration);
					int count = consumerRecords.count();
					if (count == 0) {
						continue;
					}
					
					List messages = new ArrayList(count);
					for (ConsumerRecord record : consumerRecords) {
						messages.add(record.value());
					}
					
					if (messages.isEmpty()) {
						continue;
					}
					
					if (isStatistic()) {
						AtomicInteger totalBytes = new AtomicInteger();
						messages.forEach(msg -> totalBytes.addAndGet(JacksonUtils.toBytes(msg).length));
						int messageInKB = totalBytes.get() / Units.KB;
						log.info("拉取到消息大小{}KB", messageInKB);
						log.info("拉取到{}条消息", messages.size());
					}
					
					/*
					 * 自动提交的话, 框架基于性能考虑, 直接在多线程环境下调用listener处理消息
					 * 如果是手工提交, 那么传入Consumer实例, 方便再listener里面执行提交offset
					 * 并且这个listener与拉取消息的线程是同一个线程, 有需要的话自行在listener里面启用多线程方式处理消息, 框架就不帮你做了
					 */
					if (this.autoCommit) {
						starWorker((AutoCommitListener) listener, messages);
					} else {
						((ManualCommitListener) listener).onMessage(messages, this);
					}
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}, "kafka-main").start();
		
	}
	
	/**
	 * 异步处理拉取到的消息
	 *
	 * @param listener
	 */
	private void starWorker(AutoCommitListener listener, List messages) {
		executor.execute(() -> {
			try {
				listener.onMessage(messages);
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}
	
	private boolean isStatistic() {
		return enableStatistic != null && enableStatistic.booleanValue();
	}
}
