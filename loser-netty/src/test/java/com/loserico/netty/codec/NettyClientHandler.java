package com.loserico.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-11 9:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("收到服务器消息: {}", msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("NettyClientHandler 发送数据");
		//ctx.writeAndFlush("测试String编解码");
		//测试对象编解码
		ctx.writeAndFlush(new User(1, "zhuge"));
		//测试自定义Long数据编解码器
		//ctx.writeAndFlush(1000L);
	}
}
