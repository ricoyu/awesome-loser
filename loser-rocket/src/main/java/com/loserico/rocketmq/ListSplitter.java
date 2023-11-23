package com.loserico.rocketmq;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 将一批消息分拆成1M大小的若干个小批次
 * <p>
 * Copyright: (C), 2019/12/15 15:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ListSplitter implements Iterator<List<Message>> {
	
	private static final int SIZE_LIMIT = 1000 * 1000 * 1; //1M
	private final List<Message> messages;
	private int currentIndex;
	
	public ListSplitter(List<Message> messages) {
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
		
		//遍历消息准备分拆
		for (; nextIndex < messages.size(); nextIndex++) {
			Message message = messages.get(nextIndex);
			int tmpSize = message.getTopic().length() + message.getBody().length;
			Map<String, String> properties = message.getProperties();
			for (Map.Entry<String, String> entry : properties.entrySet()) {
				tmpSize += entry.getKey().length() + entry.getValue().length();
			}
			tmpSize = tmpSize + 20; //for log overhead
			if (tmpSize > SIZE_LIMIT) {
				if (nextIndex - currentIndex == 0) {
					nextIndex++;
				}
				break;
			}
			if (tmpSize + totalSize > SIZE_LIMIT) {
				break;
			} else {
				totalSize += tmpSize;
			}
		}
		List<Message> subList = messages.subList(currentIndex, nextIndex);
		currentIndex = nextIndex;
		return subList;
	}
}
