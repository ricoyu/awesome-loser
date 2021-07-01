package com.loserico.messaging;

import com.loserico.common.lang.constants.Units;
import com.loserico.common.lang.enums.SizeUnit;
import com.loserico.messaging.consumer.Consumer;
import com.loserico.messaging.deserialzier.JsonDeserializer;
import com.loserico.messaging.enums.Acks;
import com.loserico.messaging.enums.Compression;
import com.loserico.messaging.enums.OffsetReset;
import com.loserico.messaging.producer.Producer;
import com.loserico.messaging.serializer.JsonSerializer;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.loserico.json.jackson.JacksonUtils.toJson;

/**
 * <p>
 * Copyright: (C), 2021-04-28 10:07
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class KafkaUtilsTest {
	
	@SneakyThrows
	@Test
	public void testNewProducer() {
		Producer<String, String> producer = KafkaUtils.newProducer("10.10.17.31:9092")
				.acks(Acks.LEADER)
				.batchSize(1001, SizeUnit.KB)
				.bufferMemory(32, SizeUnit.MB)
				.retries(99)
				.maxBlocks(1L, TimeUnit.MINUTES)
				.keySerializer(StringSerializer.class)
				.valueSerializer(JsonSerializer.class)
				.compression(Compression.GZIP)
				.build();
		
		List<User> messages = new ArrayList<>();
		for (int i = 0; i < 10001; i++) {
			messages.add(new User("俞雪华", i+1));
		}
		List<Future<RecordMetadata>> futures = producer.send("custom-event", messages);
		
		for (Future<RecordMetadata> future : futures) {
			RecordMetadata recordMetadata = future.get();
			log.info(toJson(recordMetadata));
		}
	}
	
	@Test
	public void testProducerSendLimit() {
		Consumer consumer = KafkaUtils.newConsumer("10.10.17.31:9092")
				.groupId("group-kkk222")
				.autoOffsetReset(OffsetReset.EARLIEST)
				.autoCommit(false)
				.fetchMaxWait(500)
				.fetchMaxBytes(50 * Units.MB)
				.maxPartitionFetchBytes(50 * Units.MB)
				.maxPollRecords(30000)
				.heartbeatInterval(3000)
				.keyDeserializer(StringDeserializer.class)
				.valueDeserializer(JsonDeserializer.class)
				.build();
		
		Producer<String, String> producer = KafkaUtils.newProducer("10.10.17.31:9092")
				.acks(Acks.LEADER)
				.batchSize(1001, SizeUnit.KB)
				.bufferMemory(32, SizeUnit.MB)
				.retries(99)
				.maxBlocks(1L, TimeUnit.MINUTES)
				.keySerializer(StringSerializer.class)
				.valueSerializer(JsonSerializer.class)
				.compression(Compression.GZIP)
				.build();
		
		consumer.subscribe("ids-event", (messages) -> {
			producer.send("ids-event-back", messages);
		});
	}
	
	@Test
	public void testConsumer() {
		Consumer consumer = KafkaUtils.newConsumer("10.10.17.31:9092")
				.groupId("group-kkk222")
				.autoOffsetReset(OffsetReset.EARLIEST)
				.autoCommit(false)
				.fetchMaxWait(500)
				.fetchMaxBytes(50 * Units.MB)
				.maxPartitionFetchBytes(50 * Units.MB)
				.maxPollRecords(30000)
				.heartbeatInterval(3000)
				.keyDeserializer(StringDeserializer.class)
				.valueDeserializer(JsonDeserializer.class)
				.build();
		
		consumer.subscribe("ids-event", (messages) -> {
			messages.forEach(System.out::println);
		});
	}
	
	@Data
	private static class User {
		
		public User(String name, int age) {
			this.name = name;
			this.age = age;
		}
		
		private String name;
		
		private int age;
	}
}
