package com.loserico.rocket.consumer;

import com.loserico.common.lang.utils.DateUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Date;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 循序消息消费者
 * <p>
 * Copyright: (C), 2023-01-20 12:48
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OrderlyConsumer {
	private static final String NAME_SERVERS = "localhost:9876";
	//private static final String NAME_SERVERS = "192.168.100.101:9876;192.168.100.102:9876";
	//private static final String NAME_SERVERS = "192.168.2.171:9876;192.168.2.172:9876";
	
	public static void main(String[] args) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group2");
		consumer.setNamesrvAddr(NAME_SERVERS);
		consumer.setPullBatchSize(12);
		try {
			consumer.subscribe("orderly", "*");
			consumer.setMessageModel(MessageModel.CLUSTERING);
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
					System.out.println("------------------------------------消费 " + msgs.size()+" 条");
					System.out.println("收到消息时间点: " + DateUtils.format(new Date()));
					for (MessageExt msg : msgs) {
						System.out.println("Broker: "+msg.getBrokerName()+msg.getStoreHost().toString()+", 消费队列: ["+msg.getQueueId()+"], 线程名称:["+Thread.currentThread().getId()+"], 消费消息: [" + new String(msg.getBody(), UTF_8)+"]");
					}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
/*
			consumer.registerMessageListener(new MessageListenerOrderly() {
				
				@Override
				public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
					System.out.println("------------------------------------消费 " + msgs.size()+" 条");
					System.out.println("收到消息时间点: " + DateUtils.format(new Date()));
					for (MessageExt msg : msgs) {
						System.out.println("Broker: "+msg.getBrokerName()+msg.getStoreHost().toString()+", 消费队列: ["+msg.getQueueId()+"], 线程名称:["+Thread.currentThread().getId()+"], 消费消息: [" + new String(msg.getBody(), UTF_8)+"]");
					}
					return ConsumeOrderlyStatus.SUCCESS;
				}
			});
*/
			consumer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
}
