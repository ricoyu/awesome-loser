package com.loserico.netty.chat;

import com.loserico.common.lang.utils.DateUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * <p>
 * Copyright: (C), 2020-09-04 8:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
	
	//GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
	private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	/*
	 * 表示 channel 处于就绪状态, 提示上线
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		/*
		 * 将该客户加入聊天的信息推送给其它在线的客户端
		 * 该方法会将 channelGroup 中所有的 channel 遍历, 并发送消息
		 */
		channelGroup.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 上线了" + DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss") + "\n");
		// 将当前 channel 加入到 channelGroup
		channelGroup.add(channel);
		log.info(ctx.channel().remoteAddress() + " 上线了");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		//将客户离开信息推送给当前在线的客户
		channelGroup.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 下线了\n");
		log.info(channel.remoteAddress() + " 下线了");
		log.info("channelGroup size=" + channelGroup.size());
	}
	
	/*
	 * 读取数据
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		//获取到当前 channel
		Channel channel = ctx.channel();
		//这时我们遍历 channelGroup, 根据不同的情况, 回送不同的消息
		channelGroup.forEach((ch) -> {
			//不是当前的 channel, 转发消息
			if (channel != ch) {
				ch.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 发送了消息: " + msg + "\n");
			} else {
				//回显自己发送的消息给自己
				ch.writeAndFlush("[ 自己 ]发送了消息: " + msg + "\n");
			}
		});
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//关闭通道
		ctx.close();
	}
}
