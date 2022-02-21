package com.loserico.messaging;

import com.loserico.messaging.admin.KafkaAdmin;
import com.loserico.messaging.consumer.Consumer;
import com.loserico.messaging.producer.Producer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2022-02-20 11:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class KafkaPartitionTest {
	
	@Before
	public void testInit() {
		KafkaAdmin admin = KafkaUtils.admin("192.168.100.101:9092");
		boolean exists = admin.existsTopic("partition-topic");
		if (!exists) {
			admin.createTopic("partition-topic")
					.partitions(3)
					.replications(2)
					.create();
		}
	}
	
	@SneakyThrows
	@Test
	public void testSend0Partition() {
		Producer<String, Object> producer = KafkaUtils.newProducer("192.168.100.101:9092").build();
		producer.send("partition-topic", 0, "hi").get();
	}
	
	@SneakyThrows
	@Test
	public void testSend1Partition() {
		Producer<String, Object> producer = KafkaUtils.newProducer("192.168.100.101:9092").build();
		producer.send("partition-topic", 1, "hi").get();
	}
	
	@SneakyThrows
	@Test
	public void testContinualSend1Partition() {
		Producer<String, Object> producer = KafkaUtils.newProducer("192.168.100.101:9092").build();
		for (int i = 0; i < 1000; i++) {
			producer.send("partition-topic", 1, "hi " + i).get();
		}
	}
	
	@SneakyThrows
	@Test
	public void testConsumeSpecifiedPartition() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("192.168.100.101:9092").groupId("partition-group1")
				.maxPollRecords(1)
				.build();
		
		consumer.subscribe("partition-topic", 1, (msgs) -> {
			msgs.forEach(System.out::println);
		});
		Thread.currentThread().join();
	}
	
	@SneakyThrows
	@Test
	public void testConsumeSpecifiedPartitionAndAutoCommitFalse() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("192.168.100.101:9092").groupId("partition-group1")
				.maxPollRecords(1)
				.autoCommit(false)
				.build();
		
		consumer.subscribe("partition-topic", 1, (msgs) -> {
			msgs.forEach(System.out::println);
		});
		Thread.currentThread().join();
	}
}
