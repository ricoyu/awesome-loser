package com.loserico.netty.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler implementation for the echo server.
 * <p>
 * Copyright: (C), 2021-06-20 8:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.write(msg);//把收到的消息原样返回给client
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("", cause);
		ctx.close();
	}
}
