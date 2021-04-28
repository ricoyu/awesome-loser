package com.loserico.messaging.listener;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-04-28 14:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface ConsumerListener {
	
	public void onMessage(List messages);
}
