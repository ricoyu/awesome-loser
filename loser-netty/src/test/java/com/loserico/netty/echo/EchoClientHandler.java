package com.loserico.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2021-06-20 8:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	
	private final ByteBuf firstMessage;
	
	public EchoClientHandler() {
		firstMessage = Unpooled.wrappedBuffer("I am echo message".getBytes(UTF_8));
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(firstMessage);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.writeAndFlush(msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		TimeUnit.SECONDS.sleep(3);
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("", cause);
		ctx.close();
	}
}
