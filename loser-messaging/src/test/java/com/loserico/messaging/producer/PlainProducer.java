package com.loserico.messaging.producer;

import com.loserico.common.lang.utils.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * <p>
 * Copyright: (C), 2021-03-02 11:35
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PlainProducer {
	
	private static final String SERVER_URLS = "172.16.0.63:29092";
	private static final String TOPIC_NAME = "nta-event-thr";
	
	public static void main(String[] args) {
		t1();
	}
	
	public static void t1() {
		Properties props = new Properties();
		props.put("bootstrap.servers", SERVER_URLS);
		
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		org.apache.kafka.clients.producer.Producer<String, String> producer = new KafkaProducer<>(props);
		
		//异步发送
		ProducerRecord<String, String> record =
				new ProducerRecord<>(TOPIC_NAME,
						IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\输出给第三方的syslog.txt")
				);
		producer.send(record);
		System.out.println("Sent!");
		producer.close();
	}
}
