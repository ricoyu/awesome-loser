package com.loserico.rocket.producer;

import com.loserico.rocketmq.MessageBuilder;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2023-01-22 18:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BatchProducer {
	private static final String NAME_SERVERS = "localhost:9876";
	
	public static void main(String[] args) {
		DefaultMQProducer producer = new DefaultMQProducer("batchgroup");
		producer.setNamesrvAddr(NAME_SERVERS);
		
		List<Message> msgs = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			Message msg = MessageBuilder.create("batchtopic")
					.body("hello batch" + i)
					.build();
			msgs.add(msg);
		}
		
		try {
			producer.start();
			SendResult sendResult = producer.send(msgs);
			System.out.println("发送结果: [" + sendResult.getSendStatus()+"], 发送目标队列: [" + sendResult.getMessageQueue().getQueueId()+"]");
		} catch (MQClientException e) {
			e.printStackTrace();
		} catch (RemotingException e) {
			e.printStackTrace();
		} catch (MQBrokerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
