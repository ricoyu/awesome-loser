package com.loserico.messaging.consumer.listener;

import java.util.List;

/**
 * 如果消费者是自动提交的, 那么用这个listener处理消息 <p/>
 * 这个AutoCommitListener默认就是在多线程环境下处理的, 所以这个listener里面不用再走多线程处理了
 * <p>
 * Copyright: (C), 2022-02-20 12:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface AutoCommitListener extends Listener {
	
	public void onMessage(List messages);
}
