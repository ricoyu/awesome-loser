package com.loserico.messaging.consumer.listener;

import org.apache.kafka.clients.consumer.Consumer;

import java.util.List;

/**
 * 如果消费者是手工提交的, 那么用这个listener处理消息 <p/>
 * 这个ManualCommitListener默认跟拉取消息的线程是同一个线程, kafka-main, 所以这个listener里面按需走多线程处理
 * <p>
 * Copyright: (C), 2022-02-20 12:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface ManualCommitListener extends Listener {
	
	public void onMessage(List messages, Consumer consumer);
}
