package com.loserico.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;

/**
 * <p>
 * Copyright: (C), 2023-11-07 16:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProtoBufServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		PersonProto.Person request = (PersonProto.Person)msg;
		System.out.println("get data name="+ request.getName());
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException) {
			System.out.println("远程主机强迫关闭了一个现有的连接");
		}
		ctx.close();
	}
}
