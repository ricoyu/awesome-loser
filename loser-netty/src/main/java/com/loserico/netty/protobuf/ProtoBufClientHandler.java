package com.loserico.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>
 * Copyright: (C), 2023-11-07 16:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Prepare to make data...");
		for (int i = 0; i < 100; i++) {
			PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
			builder.setName("三少爷"+i);
			builder.setId(1);
			builder.setEmail("ricoyu520@gmial.com");
			PersonProto.Person person = builder.build();
			System.out.println("Send data...");
			ctx.writeAndFlush(person);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
