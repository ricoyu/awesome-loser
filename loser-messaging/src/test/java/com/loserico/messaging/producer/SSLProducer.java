package com.loserico.messaging.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * <p>
 * Copyright: (C), 2021-02-24 17:45
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SSLProducer {
	
	private static final String SERVER_URLS = "192.168.100.104:8989";
	private static final String TOPIC_NAME = "test-topic";
	
	public static void main(String[] args) {
		t1();
		//t2();
	}
	
	/**
	 * 走SSL方式
	 */
	public static void t1() {
		Properties props = new Properties();
		props.put("bootstrap.servers", SERVER_URLS);
		
		props.put("security.protocol", "SSL");
		props.put("ssl.truststore.location", "D:\\Learning\\awesome-loser\\loser-messaging\\src\\test\\resources\\client.truststore.jks");
		props.put("ssl.truststore.password", "111111");
		
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		Producer<String, String> producer = new KafkaProducer<>(props);
		
		for (int i = 0; i < 100; i++) {
			//异步发送
			producer.send(new ProducerRecord<String, String>(TOPIC_NAME, Integer.toString(i), Integer.toString(i)));
			System.out.println("发送 " + i);
		}
		
		producer.close();
	}
	
	public static void t2() {
		Properties props = new Properties();
		props.put("bootstrap.servers", SERVER_URLS);
		
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		Producer<String, String> producer = new KafkaProducer<>(props);
		
		for (int i = 0; i < 100; i++) {
			//异步发送
			producer.send(new ProducerRecord<String, String>(TOPIC_NAME, Integer.toString(i), Integer.toString(i)));
			System.out.println("发送 " + i);
		}
		
		producer.close();
	}
}
