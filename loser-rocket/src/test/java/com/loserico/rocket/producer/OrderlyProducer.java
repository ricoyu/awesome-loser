package com.loserico.rocket.producer;

import com.loserico.common.lang.utils.DateUtils;
import com.loserico.rocket.producer.order.OrderStep;
import com.loserico.rocketmq.MessageBuilder;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 顺序消息发送者
 * <p>
 * Copyright: (C), 2023-01-19 9:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OrderlyProducer {
	
	//private static final String NAME_SERVERS = "192.168.2.171:9876;192.168.2.172:9876";
	private static final String NAME_SERVERS = "localhost:9876";
	//private static final String NAME_SERVERS = "192.168.100.101:9876;192.168.100.102:9876";
	
	public static void main(String[] args) {
		DefaultMQProducer producer = new DefaultMQProducer("specifyQueueGroup");
		producer.setNamesrvAddr(NAME_SERVERS);
		try {
			producer.start();
			List<OrderStep> orderSteps = OrderStep.buildOrders();
			for (OrderStep order : orderSteps) {
				Message message = MessageBuilder.create("orderly")
						.body(order)
						.build();
				SendResult sendResult = producer.send(message, new MessageQueueSelector() {
					@Override
					public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
						long orderId = (long) arg;
						int index = (int) (orderId % mqs.size());
						MessageQueue queue = mqs.get(index);
						System.out.print("OrderId: " +orderId+", queueId: " + queue.getQueueId());
						return queue;
					}
				}, order.getOrderId());
				System.out.println( "发送结果:"+ sendResult.getSendStatus());
			}
			System.out.println("发送完成时间点: " + DateUtils.format(new Date()));
			TimeUnit.SECONDS.sleep(1);
			producer.shutdown();
		} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
