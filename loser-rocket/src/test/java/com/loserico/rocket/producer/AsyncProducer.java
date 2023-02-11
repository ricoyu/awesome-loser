package com.loserico.rocket.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2023-01-16 11:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AsyncProducer {
	
	public static void main(String[] args) {
		DefaultMQProducer producer = new DefaultMQProducer("group3");
		producer.setNamesrvAddr("192.168.100.101:9876;192.168.100.102:9876");
		try {
			producer.start();
			//设置发送失败重试次数
			producer.setRetryTimesWhenSendFailed(0);
			for (int i = 0; i < 100; i++) {
				final int index = i;
				Message msg = new Message("base", "tag2", "", ("async msg"+i).getBytes(StandardCharsets.UTF_8));
				producer.send(msg, new SendCallback() {
					@Override
					public void onSuccess(SendResult sendResult) {
						System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
					}
					
					@Override
					public void onException(Throwable e) {
						System.out.printf("%-10d Exception %s %n", index, e);
						e.printStackTrace();
					}
				});
				SECONDS.sleep(1);
			}
			
		} catch (MQClientException | RemotingException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
