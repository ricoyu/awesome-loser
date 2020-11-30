package com.loserico.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-17 9:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<String> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		log.info("Client received {}", msg);
		if (msg != null && "idle close".equals(msg)) {
			log.info("服务端关闭连接，客户端也关闭");
			ctx.channel().close();
		}
	}
}
