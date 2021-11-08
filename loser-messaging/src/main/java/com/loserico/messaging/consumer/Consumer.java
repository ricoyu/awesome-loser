package com.loserico.messaging.consumer;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.loserico.common.lang.concurrent.LoserExecutors;
import com.loserico.common.lang.concurrent.Policy;
import com.loserico.common.lang.constants.Units;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.messaging.listener.ConsumerListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
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
	 * Consumer 调用poll(long)的超时时间, Spring Kafka也是默认5000 ms<p>
	 * Set the max time to block in the consumer waiting for records.
	 */
	private Integer pollTimeout = 5000;
	
	private Duration pollDuration;
	
	private Boolean enableStatistic;
	
	private BlockingQueue<List> queue;
	
	private int workerThreads = LoserExecutors.NCPUS + 1;
	
	/**
	 * 用来消息去重的布隆过滤器
	 */
	private BloomFilter<Long> filter = BloomFilter.create(Funnels.longFunnel(), 10000, 0.01);
	
	private ThreadPoolExecutor executor = null;
	
	public Consumer(Map<String, Object> configs, Deserializer<String> keyDeserializer, Deserializer valueDeserializer) {
		super(configs, keyDeserializer, valueDeserializer);
		this.enableStatistic = (Boolean) configs.get("enableStatistic");
		pollTimeout(configs);
		if (configs.get("queueSize") == null) {
			queue = new ArrayBlockingQueue(1000);
		} else {
			Integer size = (Integer) configs.get("queueSize");
			queue = new ArrayBlockingQueue(size);
		}
		
		Integer workerThreads = (Integer) configs.get("workerThreads");
		if (workerThreads != null) {
			this.workerThreads = workerThreads;
		}
		
		buildWorkingPool(configs);
	}
	
	private void buildWorkingPool(Map<String, Object> configs) {
		LoserExecutors loserExecutors = LoserExecutors.of("loser-consumer-worker").rejectPolicy(Policy.CALLER_RUNS);
		Integer corePoolSize = LoserExecutors.NCPUS + 1;
		loserExecutors.corePoolSize(corePoolSize);
		Integer maxPoolSize = (Integer) configs.get("workerThreads");
		if (maxPoolSize == null) {
			maxPoolSize = corePoolSize * 3;
		} else if (maxPoolSize < corePoolSize) {
			maxPoolSize = corePoolSize * 3;
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
	 * @param listener
	 */
	public void subscribe(String topic, ConsumerListener listener) {
		subscribe(asList(topic));
		startPolling();
		starWorker(listener);
	}
	
	/**
	 * 订阅Topic, 消息来了之后处理
	 *
	 * @param topicPattern
	 * @param listener
	 */
	public void subscribePattern(String topicPattern, ConsumerListener listener) {
		subscribe(Pattern.compile(topicPattern));
		startPolling();
		starWorker(listener);
	}
	
	/**
	 * 异步拉取消息
	 */
	private void startPolling() {
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
					Set<Long> offsets = new HashSet<>();
					for (ConsumerRecord record : consumerRecords) {
						messages.add(record.value());
					}
					
					//提高性能?
					//commitAsync();
					commitSync();
					
					if (messages.isEmpty()) {
						continue;
					}
					
					queue.put(messages);
					
					if (isStatistic()) {
						List<Long> sortedOffsets = offsets.stream().sorted().collect(toList());
						if (!sortedOffsets.isEmpty()) {
							log.info("本次拉取到Offset范围: {} ~ {}", sortedOffsets.get(0), sortedOffsets.get(sortedOffsets.size() - 1));
						}
						
						AtomicInteger totalBytes = new AtomicInteger();
						messages.forEach(msg -> totalBytes.addAndGet(JacksonUtils.toBytes(msg).length));
						int messageInKB = totalBytes.get() / Units.KB;
						log.info("拉取到消息大小{}KB", messageInKB);
						log.info("拉取到{}条消息", messages.size());
						log.info("当前队列消息数量: {}", queue.size());
					}
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}, "loser-consumer-main").start();
		
	}
	
	/**
	 * 异步处理拉取到的消息
	 *
	 * @param listener
	 */
	private void starWorker(ConsumerListener listener) {
		for (int i = 0; i < workerThreads; i++) {
			executor.execute(() -> {
				while (true) {
					try {
						List messages = queue.take();
						listener.onMessage(messages);
					} catch (Exception e) {
						log.error("", e);
					}
				}
			});
		}
	}
	
	private boolean isStatistic() {
		return enableStatistic != null && enableStatistic.booleanValue();
	}
}
