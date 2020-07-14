package com.loserico.redis;

import redis.clients.jedis.JedisPubSub;

public class KeyExpiredListener extends JedisPubSub {

	@Override
	public void onMessage(String channel, String message) {
		super.onMessage(channel, message);
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		super.onPMessage(pattern, channel, message);
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		super.onSubscribe(channel, subscribedChannels);
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		super.onPSubscribe(pattern, subscribedChannels);
	}

}
