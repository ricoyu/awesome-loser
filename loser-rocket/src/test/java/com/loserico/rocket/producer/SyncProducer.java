package com.loserico.rocket.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * 发送同步消息
 * <p>
 * Copyright: (C), 2023-01-16 10:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SyncProducer {
	
	public static void main(String[] args) {
		DefaultMQProducer producer = new DefaultMQProducer("group0");
		producer.setNamesrvAddr("192.168.2.171:9876;192.168.2.172:9876");
		try {
			producer.start();
			for (int i = 0; i < 10; i++) {
				Message msg = new Message("base", "tag2", "", ("sync message"+i).getBytes(StandardCharsets.UTF_8));
				SendResult sendResult = producer.send(msg);
				//通过sendResult检查消息是否成功送达
				System.out.printf("%s%n", sendResult);
			}
			producer.shutdown();
		} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
