package com.loserico.messaging.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

import static java.util.Arrays.asList;

/**
 * <p>
 * Copyright: (C), 2021-02-24 17:16
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class Consumer {
	
	private static final String SERVER_URLS = "192.168.100.104:8989";
	private static final String TOPIC_NAME = "test-topic";
	
	public static void main(String[] args) {
		t1();
	}
	
	private static void t1() {
		Properties props = new Properties();
		props.put("bootstrap.servers", SERVER_URLS);
		
		props.put("group.id", "g2");
		
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		
		props.put("session.timeout.ms", "30000");
		
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(asList(TOPIC_NAME));
		try {
			while (true) {
				ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
				for (ConsumerRecord<String, String> record : consumerRecords) {
					System.out.println("Partition="+record.partition()+", offset="+ record.offset()+", key="+record.key()+", value="+record.value());
				}
			}
		} finally {
			consumer.close();
		}
	}
}
