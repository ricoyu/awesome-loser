package com.loserico.rocket.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2023-01-22 18:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BatchConsumer {
	private static final String NAME_SERVERS = "localhost:9876";
	
	public static void main(String[] args) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("batchgroup");
		consumer.setNamesrvAddr(NAME_SERVERS);
		consumer.setConsumeMessageBatchMaxSize(9); //默认每次拉取1条消息
		consumer.setPullBatchSize(9); //每次拉取消息的最大条数, 默认32
		try {
			consumer.subscribe("batchtopic", "*");
			consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
				System.out.println("一次拉取消息大小: ["+ msgs.size()+"]");
				for (MessageExt msg : msgs) {
					System.out.println("消费消息: ["+ new String(msg.getBody(), UTF_8)+"], 消息队列: ["+msg.getQueueId()+"]");
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			});
			consumer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
}
