package com.loserico.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * RocketMQ 单次发送消息大小不能超过4M, 这个类帮助在发送批量消息的时候控制消息大小<4M
 * <p/>
 * <b>用法:</b>
 * <pre> {@code
 * MessageSplitter splitter = new MessageSplitter(messages);
 * producer.send(splitter.next)
 * }</pre>
 * <p>
 * Copyright: (C), 2023-01-22 20:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class MessageSplitter implements Iterator<List<Message>> {
	
	private static final int SIZE_LIMIT = 1024 * 1024 * 4; //rocketmq限制批量发送消息大小不能大于4M
	
	private final List<Message> messages;
	
	private int currentIndex;
	
	public MessageSplitter(List<Message> messages) {
		this.messages = messages;
	}
	
	@Override
	public boolean hasNext() {
		return currentIndex < messages.size();
	}
	
	@Override
	public List<Message> next() {
		int nextIndex = currentIndex;
		int totalSize = 0;
		
		for (; nextIndex < messages.size(); nextIndex++) {
			Message message = messages.get(nextIndex);
			int tempSize = message.getTopic().length() + message.getBody().length;
			Map<String, String> properties = message.getProperties();
			for (Map.Entry<String, String> entry : properties.entrySet()) {
				tempSize += entry.getKey().length() + entry.getValue().length();
			}
			
			tempSize = tempSize + 20; //增加日志的开销20字节
			if (tempSize > SIZE_LIMIT) {
				//单个消息超过了最大限制
				//忽略; 否则会增加阻塞分裂的过程
				if (nextIndex - currentIndex == 0) {
					//假如下一个子列表没有元素, 则添加这个子列表然后退出循环, 否则只是退出循环
					nextIndex++;
				}
				break;
			}
			
			if (tempSize + totalSize > SIZE_LIMIT) {
				break;
			} else {
				totalSize +=tempSize;
			}
		}
		
		log.info("当前消息大小: {}字节", totalSize);
		List<Message> subList = messages.subList(currentIndex, nextIndex);
		currentIndex = nextIndex;
		return subList;
	}
}
