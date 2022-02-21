package com.loserico.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loserico.common.lang.constants.Units;
import com.loserico.common.lang.enums.SizeUnit;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.messaging.admin.KafkaAdmin;
import com.loserico.messaging.consumer.Consumer;
import com.loserico.messaging.enums.Acks;
import com.loserico.messaging.enums.Compression;
import com.loserico.messaging.enums.OffsetReset;
import com.loserico.messaging.producer.Producer;
import com.loserico.messaging.serializer.JsonSerializer;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.Assert.assertTrue;

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
		for (int i = 0; i < 100010; i++) {
			messages.add(new User("俞雪华", i + 1));
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
				.build();
		
		Producer<String, Object> producer = KafkaUtils.newProducer("10.10.17.31:9092")
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
	
	@SneakyThrows
	@Test
	public void testConsumer() {
		/*Consumer<String, String> consumer = KafkaUtils.newConsumer("172.23.12.65:9092")
				.groupId("group-kkk333")
				.autoOffsetReset(OffsetReset.LATEST)
				.autoCommit(false)
				.fetchMaxWait(500)
				.fetchMaxBytes(50 * Units.MB)
				.maxPartitionFetchBytes(50 * Units.MB)
				.maxPollRecords(1)
				.heartbeatInterval(3000)
				.build();*/
		
		Consumer consumer = KafkaUtils.newConsumer("192.168.100.105:9092")
				.groupId("metadata-group")
				.autoCommit(false)
				.fetchMaxWait(500)
				.fetchMaxBytes(500 * Units.KB)
				.maxPollRecords(3000)
				.heartbeatInterval(3000)
				.sessionTimeout(5, MINUTES)
				.build();
		
		consumer.subscribe("ids-metadata", (messages) -> {
			messages.forEach(System.out::println);
		});
		Thread.currentThread().join();
	}
	
	@Test
	public void testSendIdsEvent() throws ExecutionException, InterruptedException {
		//Producer<String, String> producer = KafkaUtils.newProducer("10.10.17.31:9092").build();
		Producer<String, String> producer = KafkaUtils.newProducer("172.23.12.65:9092").build();
		producer.send("ids-event", IOUtils.readClassPathFileAsString("ids-event.json")).get();
	}
	
	
	@Test
	public void testSendPcapEvent() throws ExecutionException, InterruptedException {
		Producer<String, String> producer = KafkaUtils.newProducer("172.23.12.65:9092").build();
		producer.send("pcap-event", IOUtils.readFileAsString("C:\\Users\\ricoy\\Documents\\pcap-event.json")).get();
		//producer.send("pcap-event", IOUtils.readFileAsString("C:\\Users\\ricoy\\Documents\\pcap-event2.json")).get();
	}
	
	@Test
	public void testSendIntelEvent() {
		Producer<String, String> producer = KafkaUtils.newProducer("172.23.12.65:9092").build();
		producer.send("intel-event", IOUtils.readClassPathFileAsString("intel-event.json"));
	}
	
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}
	
	@SneakyThrows
	@Test
	public void testSendSandboxEvent() {
		//Producer<String, String> producer = KafkaUtils.newProducer("10.10.17.31:9092").build();
		Producer<String, String> producer = KafkaUtils.newProducer("172.23.12.65:9092").build();
		String event = IOUtils.readFileAsString("C:\\Users\\ricoy\\Documents\\sandbox-event-ids2.txt");
		producer.send("sandbox-event", event).get();
	}
	
	@SneakyThrows
	@Test
	public void testNetlog() {
		Producer<String, String> producer = KafkaUtils.newProducer("10.10.17.31:9092").build();
		String event = IOUtils.readFileAsString("C:\\Users\\ricoy\\Documents\\ids-metadata.txt");
		producer.send("ids-metadata", event).get();
	}
	
	@Test
	public void testReceive() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("172.23.12.65", "9092")
				.groupId("group666").build();
		consumer.subscribe("ids-metadata2", (messages) -> {
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
	
	@SneakyThrows
	@Test
	public void testSendPerformance() {
		AtomicLong countAtomicLong = new AtomicLong();
		Producer<String, String> producer = KafkaUtils.newProducer("192.168.100.101:9092").build();
		String message = IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\NTA测试数据\\ids-metadata-http.json");
		while (true) {
			long num = countAtomicLong.incrementAndGet();
			producer.send("ids-metadata", message);
			System.out.println("发送第" + num + "条消息");
			if (num == 3) {
				break;
			}
		}
		Thread.currentThread().join();
	}
	
	@SneakyThrows
	@Test
	public void test() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("192.168.100.101:9092")
				.groupId("metadata-group")
				.autoCommit(false)
				.fetchMaxWait(500)
				.fetchMaxBytes(50 * Units.MB)
				.maxPartitionFetchBytes(50 * Units.MB)
				.maxPollRecords(10000)
				//.workerThreads(500)
				//.queueSize(6000)
				.autoOffsetReset(OffsetReset.EARLIEST)
				.heartbeatInterval(3000)
				.sessionTimeout(1, TimeUnit.MINUTES)
				.messageClass(NetLog.class)
				.build();
		
		consumer.subscribe("ids-metadata", (messages) -> {
			log.info("消费{}条消息", messages.size());
		});
		
		Thread.currentThread().join();
	}
	
	@Test
	public void testConsumePerformance() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("192.168.100.105:9092")
				.groupId("perf-group")
				.pollTimeout(5000)
				//.autoOffsetReset(OffsetReset.EARLIEST)
				.maxPollRecords(5000)
				.fetchMinBytes(1024)
				.autoCommit(true)
				.build();
		consumer.subscribe("test-topic", (messages) -> {
			System.out.println("==================== 拉取到" + messages.size() +" 条=================================");
		});
	}
	
	@Test
	public void testDuplicateConsume() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("192.168.100.101:9092")
				.groupId("dup-group")
				.pollTimeout(3000)
				.maxPollRecords(5000)
				.fetchMinBytes(1024)
				.autoCommit(true)
				.build();
		
		consumer.subscribe("dup-topic", (messages) -> {
			for (Object message : messages) {
				throw new RuntimeException((String) message);
			}
		});
	}
	
	@Data
	static class NetLog {
		/**
		 * 日志编号
		 */
		private String id;
		
		/**
		 * 探针唯一id
		 */
		@JsonProperty("dev_id")
		private String devId;
		
		/**
		 * 事件发生时间, 时间戳形式
		 */
		@JsonProperty("create_time")
		private Long createTime;
		
	}
	
	@SneakyThrows
	@Test
	public void test111() {
		Consumer consumer = KafkaUtils.newConsumer("192.168.100.105:9092")
				.groupId("metadata-index-group")
				.fetchMaxBytes(2 * Units.MB)
				.maxPartitionFetchBytes(2 * Units.MB)
				.maxPollRecords(1000)
				.build();
		
		consumer.subscribe("metadata-index", (messages) -> {
			log.info("收到{}条消息", messages.size());
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		Thread.currentThread().join();
	}
	
	@SneakyThrows
	@Test
	public void testMetadataSharing() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("10.10.26.240:9092")
				.groupId("loser-group")
				.build();
		
		consumer.subscribe("index-metadata", (messages) -> {
			messages.forEach(System.out::println);
		});
		
		Thread.currentThread().join();
	}
	
	@SneakyThrows
	@Test
	public void testConsuming() {
		Consumer<String, String> consumer = KafkaUtils.newConsumer("10.20.26.240:9092")
				.groupId("loser-group")
				.build();
		
		consumer.subscribe("index-metadata", (messages) -> {
			messages.forEach(System.out::println);
		});
		
		Thread.currentThread().join();
	}
	
	@SneakyThrows
	@Test
	public void testCreateTopic() {
		KafkaAdmin admin = KafkaUtils.admin("192.168.100.101:9092, 192.168.100.102:9092,192.168.100.103:9092");
		admin.createTopic("java-topic")
				.minInsyncReplicas(2)
				.partitions(3)
				.replications(2)
				.callback((e, topicName) -> {
					if (e == null) {
						log.info("{} 创建成功!", topicName);
					} else {
						log.info("{} 创建失败!", topicName);
					}
				})
				.create();
	}
	
	@SneakyThrows
	@Test
	public void testDescribeTopic() {
		KafkaAdmin admin = KafkaUtils.admin("192.168.100.101:9092, 192.168.100.102:9092,192.168.100.103:9092");
		TopicDescription topicDescription = admin.describeTopic("java-topic");
		log.info(toJson(topicDescription));
	}
	
	@Test
	public void testDeleteTopic() {
		KafkaAdmin admin = KafkaUtils.admin("192.168.100.101:9092, 192.168.100.102:9092,192.168.100.103:9092");
		boolean deleted = admin.deleteTopic("java-topic");
		assertTrue(deleted);
	}
	
	@Test
	public void testListTopics() {
		KafkaAdmin admin = KafkaUtils.admin("192.168.100.101:9092, 192.168.100.102:9092,192.168.100.103:9092");
		List<String> topics = admin.listTopics();
		topics.forEach(System.out::println);
	}
}
