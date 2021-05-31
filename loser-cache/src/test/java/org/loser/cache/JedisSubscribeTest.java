package org.loser.cache;

import com.loserico.cache.JedisUtils;
import com.loserico.cache.auth.AuthUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2021-05-28 10:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JedisSubscribeTest {
	
	public static void main(String[] args) {
		JedisUtils.subscribe((channel, message) -> {
			log.info("Channel: {}, Message: {}", channel, message);
		}, AuthUtils.AUTH_LOGOUT_CHANNEL);
	}
}
