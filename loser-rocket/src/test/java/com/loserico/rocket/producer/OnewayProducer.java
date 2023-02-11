package com.loserico.rocket.producer;

import com.loserico.rocketmq.MessageBuilder;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * <p>
 * Copyright: (C), 2023-01-16 15:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OnewayProducer {
	
	public static void main(String[] args) {
		DefaultMQProducer producer = new DefaultMQProducer("group0");
		producer.setNamesrvAddr("192.168.2.171:9876;192.168.2.172:9876");
		try {
			producer.start();
			for (int i = 0; i < 3; i++) {
				Message message = MessageBuilder.create("base")
						.tags("tag1")
						.body("One way message" + i)
						.build();
				producer.sendOneway(message);
				System.out.println("sent"+i);
			}
		} catch (MQClientException | RemotingException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		producer.shutdown();
	}
}
