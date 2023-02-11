package com.loserico.rocket.producer;

import com.loserico.common.lang.mq.DelayLevel;
import com.loserico.common.lang.utils.DateUtils;
import com.loserico.rocketmq.MessageBuilder;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Date;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2023-01-22 17:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DelayedProducer {
	private static final String NAME_SERVERS = "localhost:9876";
	
	public static void main(String[] args) {
		DefaultMQProducer producer = new DefaultMQProducer("delaygroup");
		producer.setNamesrvAddr(NAME_SERVERS);
		try {
			producer.start();
			for (int i = 0; i < 5; i++) {
				Message message = MessageBuilder.create("delaytopic")
						.delayLevel(DelayLevel.S_10)
						.body("延迟消息" + i)
						.build();
				try {
					SendResult sendResult = producer.send(message);
					System.out.println("发送结果: " + sendResult.getSendStatus()+", 发送到队列:"+sendResult.getMessageQueue().getQueueId()+"发送时间: ["+ DateUtils.format(new Date())+"]");
					SECONDS.sleep(1);
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
			producer.shutdown();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
}
