package com.loserico.netty.splitpacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2020-09-16 9:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class MyServerHandler extends SimpleChannelInboundHandler<MyMessageProtocol> {
	
	private int count;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MyMessageProtocol msg) throws Exception {
		log.info("====服务端接收到消息如下====");
		log.info("长度={}", msg.getLen());
		log.info("内容={}", new String(msg.getContent(), UTF_8));
		log.info("服务端接收到消息包数量={}", (++this.count));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("", cause);
		ctx.close();
	}
}
