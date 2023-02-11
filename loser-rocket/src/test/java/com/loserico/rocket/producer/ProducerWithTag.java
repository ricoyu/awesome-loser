package com.loserico.rocket.producer;

import com.loserico.rocketmq.MessageBuilder;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * <p>
 * Copyright: (C), 2023-01-22 21:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProducerWithTag {
	private static final String NAME_SERVERS = "localhost:9876";
	
	public static void main(String[] args) {
		DefaultMQProducer producer = new DefaultMQProducer("taggroup");
		producer.setNamesrvAddr(NAME_SERVERS);
		try {
			producer.start();
			for (int i = 0; i < 10; i++) {
				MessageBuilder messageBuilder = MessageBuilder.create("tagtopic");
				if (i % 2 != 0) {
					messageBuilder.tags("tagA");
				}
				Message message = messageBuilder
						.body("打Tag的消息" + i)
						.build();
				SendResult sendResult = producer.send(message);
				System.out.println("发送结果: [" + sendResult.getSendStatus()+"], 目标队列: [" + sendResult.getMessageQueue().getQueueId()+"]");
			}
			producer.shutdown();
		} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
