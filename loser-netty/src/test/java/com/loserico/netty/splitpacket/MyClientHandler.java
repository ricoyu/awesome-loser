package com.loserico.netty.splitpacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2020-09-16 9:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MyMessageProtocol> {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 200; i++) {
			String msg = "你好, 我是三少爷";
			//创建协议包对象
			MyMessageProtocol messageProtocol = new MyMessageProtocol();
			messageProtocol.setLen(msg.getBytes(UTF_8).length);
			messageProtocol.setContent(msg.getBytes(UTF_8));
			ctx.writeAndFlush(messageProtocol);
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MyMessageProtocol msg) throws Exception {
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
