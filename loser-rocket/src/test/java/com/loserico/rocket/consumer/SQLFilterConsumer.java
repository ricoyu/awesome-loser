package com.loserico.rocket.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2023-01-23 11:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SQLFilterConsumer {
	private static final String NAME_SERVERS = "localhost:9876";
	
	public static void main(String[] args) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("sqlGroup");
		consumer.setNamesrvAddr(NAME_SERVERS);
		consumer.setConsumeMessageBatchMaxSize(100);
		try {
			consumer.subscribe("SQLFilterTopic", MessageSelector.bySql("i > 5"));
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
					System.out.println("--------------> 本次拉取消息:["+msgs.size()+"], 线程ID:["+Thread.currentThread().getId()+"]<--------------");
					for (MessageExt msg : msgs) {
						System.out.println("queue: ["+msg.getQueueId()+"], 消息:[" + new String(msg.getBody(), UTF_8)+"]");
					}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			consumer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		
	}
}
