package com.loserico.messaging;

import com.loserico.common.lang.enums.SizeUnit;
import com.loserico.messaging.consumer.Consumer;
import com.loserico.messaging.deserialzier.JsonDeserializer;
import com.loserico.messaging.enums.Acks;
import com.loserico.messaging.enums.Compression;
import com.loserico.messaging.producer.Producer;
import com.loserico.messaging.serializer.JsonSerializer;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.Arrays.asList;

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
		Producer<String, String> producer = KafkaUtils.newProducer("192.168.100.104:9092")
		//Producer<String, String> producer = KafkaUtils.newProducer("10.10.26.13:29092")
		//Producer<String, String> producer = KafkaUtils.newProducer("172.16.0.63:29092")
				.acks(Acks.LEADER)
				.batchSize(16, SizeUnit.KB)
				.clientId("sexyuncle")
				.bufferMemory(32, SizeUnit.MB)
				.retries(99)
				.maxBlocks(1L, TimeUnit.MINUTES)
				.keySerializer(StringSerializer.class)
				.valueSerializer(JsonSerializer.class)
				.compression(Compression.GZIP)
				.build();
		
		/*Future<RecordMetadata> metadataFuture = producer.send("sexy-uncle", "这是来自三少爷的第一条消息");
		System.out.println(toJson(metadataFuture));
		
		List<String> messages = asList("这是来自三少爷的第一条消息", "这是来自三少爷的第一条消息", "这是来自三少爷的第一条消息", "这是来自三少爷的第一条消息");
		List<Future<RecordMetadata>> futures = producer.send("sexy-uncle", messages);*/
		
		/*User user = new User("俞雪华", 28);
		producer.send("sexy-uncle", user);*/
		
		List<User> messages = asList(new User("俞雪华", 28), new User("三少爷", 40));
		List<Future<RecordMetadata>> futures = producer.send("test", messages);
		
		for (Future<RecordMetadata> future : futures) {
			RecordMetadata recordMetadata = future.get();
			log.info(toJson(recordMetadata));
		}
	}
	
	@Test
	public void testConsumer() {
		//Consumer consumer = KafkaUtils.newConsumer("172.16.0.63:29092")
		Consumer consumer = KafkaUtils.newConsumer("192.168.100.104:9092")
				.clientId("机皇")
				.groupId("group1")
				.autoCommit(false)
				.fetchMaxWait(500)
				.heartbeatInterval(3000)
				.maxPollRecords(1000)
				.messageClass(User.class)
				.keyDeserializer(StringDeserializer.class)
				.valueDeserializer(JsonDeserializer.class)
				.build();
		
		consumer.subscribe("sexy-uncle", (messages) -> {
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
