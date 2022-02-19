package com.loserico.messaging.callback;

/**
 * <p>
 * Copyright: (C), 2022-02-19 17:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface TopicCreationCallback {
	
	/**
	 * Topic创建完成后回调, 如果创建成功, e 为null
	 * @param e
	 * @param topicNames
	 */
	public void onComplete(Throwable e, String... topicNames);
}
