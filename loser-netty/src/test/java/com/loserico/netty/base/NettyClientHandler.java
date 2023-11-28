package com.loserico.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2020-09-02 11:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	
	private AtomicInteger counter = new AtomicInteger();
	/**
	 * 当客户端连接服务器完成就会触发该方法
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf buf = Unpooled.copiedBuffer(("HelloServer"+ counter.incrementAndGet()).getBytes(UTF_8));
		ctx.writeAndFlush(buf);
	}
	
	/**
	 * 当通道有读取事件时会触发, 即服务端发送数据给客户端
	 *
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//ByteBuf buf = (ByteBuf) msg;
		ReferenceCountUtil.release(msg);
		log.info("收到服务端的消息:{}", msg);
		log.info("服务端的地址: {}", ctx.channel().remoteAddress());
		SECONDS.sleep(3);
		ByteBuf buf = Unpooled.copiedBuffer(("HelloServer"+ counter.incrementAndGet()).getBytes(UTF_8));
		ctx.writeAndFlush(buf);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("", cause);
		ctx.close();
	}
}
