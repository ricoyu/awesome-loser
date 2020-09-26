package com.loserico.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-17 8:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class HeartBeatServerHandler extends SimpleChannelInboundHandler<String> {
	
	private int readIdleTimes = 0;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("=== " + ctx.channel().remoteAddress() + " is active ===");
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		log.info(" ====== > [server] message received : {}", msg);
		if ("Heartbeat Packet".equals(msg)) {
			ctx.channel().writeAndFlush("ok");
		} else {
			log.info("其他信息处理 ...");
		}
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		IdleStateEvent event = (IdleStateEvent) evt;
		String eventType = null;
		switch (event.state()) {
			case READER_IDLE:
				eventType = "读空闲";
				//读空闲的计数加1
				readIdleTimes++;
				break;
			case WRITER_IDLE:
				eventType = "写空闲";
				//不处理
				break;
			case ALL_IDLE:
				eventType = "读写空闲";
				//不处理
				break;
		}
		
		log.info(ctx.channel().remoteAddress() + "超时事件" + eventType);
		if (readIdleTimes > 3) {
			log.info("[server]读空闲超过3次, 关闭连接, 释放更多资源");
			ctx.channel().writeAndFlush("idle close");
			ctx.channel().close();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("", cause);
		ctx.close();
	}
}
