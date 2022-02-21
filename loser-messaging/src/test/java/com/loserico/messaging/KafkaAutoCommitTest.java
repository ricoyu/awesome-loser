package com.loserico.messaging;

import com.loserico.messaging.consumer.Consumer;
import com.loserico.messaging.producer.Producer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2022-02-19 21:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class KafkaAutoCommitTest {
	
	@SneakyThrows
	@Test
	public void testProducer() {
		Producer<String, Object> producer =
				KafkaUtils.newProducer("192.168.100.101:9092,192.168.100.102:9092,192.168.100.103:9092").build();
		for (int i = 0; i < 5; i++) {
			producer.send("java-topic", "hello " + i);
		}
		Thread.currentThread().join();
	}
	
	@SneakyThrows
	@Test
	public void testConsumerNotAutoCommit() {
		Consumer<String, String> consumer =
				KafkaUtils.newConsumer("192.168.100.101:9092,192.168.100.102:9092,192.168.100.103:9092")
						.groupId("kafka-learn-group")
						.maxPollRecords(1)
						.autoCommit(false)
						.build();
		
		consumer.subscribe("java-topic", (msgs, kafkaConsumer) -> {
			log.info("拉取到 {} 条消息", msgs.size());
			msgs.forEach(System.out::println);
			if (msgs.get(0).equals("hello 2")) {
				kafkaConsumer.commitAsync();
			}
		});
		Thread.currentThread().join();
	}
}
