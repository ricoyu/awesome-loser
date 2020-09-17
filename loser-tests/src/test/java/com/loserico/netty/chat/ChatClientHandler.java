package com.loserico.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-09 8:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		log.info(msg);
	}
}
