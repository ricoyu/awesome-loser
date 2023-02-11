package com.loserico.rocket.producer;

import com.loserico.rocketmq.MessageBuilder;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2023-01-23 11:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SQLFilterProducer {
	private static final String NAME_SERVERS = "localhost:9876";
	
	public static void main(String[] args) {
		DefaultMQProducer producer = new DefaultMQProducer("sqlgroup");
		producer.setNamesrvAddr(NAME_SERVERS);
		try {
			producer.start();
			for (int i = 0; i < 10; i++) {
				Message message = MessageBuilder.create("SQLFilterTopic")
						.body("hello" + i)
						.userProperty("i", i + "")
						.build();
				SendResult sendResult = producer.send(message);
				System.out.println("发送结果: [" + sendResult.getSendStatus()+"], 目标Queue: ["+ sendResult.getMessageQueue().getQueueId()+"]");
				SECONDS.sleep(1);
			}
		} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
