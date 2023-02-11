package com.loserico.rocket.consumer;

import com.loserico.common.lang.utils.DateUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2023-01-22 17:41
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DelayedConsumer {
	private static final String NAME_SERVERS = "localhost:9876";
	
	public static void main(String[] args) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delaygroup");
		consumer.setNamesrvAddr(NAME_SERVERS);
		try {
			consumer.subscribe("delaytopic", "*");
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for (MessageExt msg : msgs) {
					System.out.println("消息ID: [" + msg.getMsgId()+"], 接收时间: ["+ DateUtils.format(new Date()) +"], 延迟时间: [" 
							+(System.currentTimeMillis() - msg.getStoreTimestamp())+"ms]");
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		try {
			consumer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
}
